#!/bin/bash

set -e

KIND_CLUSTER_NAME="local-cluster"
AUTH_SERVICE_NAMESPACE="auth-service-dev"
AUTH_SERVICE_DOCKER_IMAGE_TAG="0.0.1"
AUTH_SERVICE_DOCKER_IMAGE="auth-service-dev:$AUTH_SERVICE_DOCKER_IMAGE_TAG"

# Load utility functions
source ./scripts/local_util.sh

# Load aws secrets environment variables
assume_role() {
  local role_arn=$AWS_ROLE_ARN
  local session_name="auth-service-dev-session"

  echo_header "Assuming role $role_arn..."

  local assume_role_output
  assume_role_output=$(aws sts assume-role --role-arn "$role_arn" --role-session-name "$session_name")

  if [ $? -ne 0 ]; then
    echo "Failed to assume role $role_arn"
    return 1
  fi

  set_aws_credentials "$assume_role_output"

  echo_footer "Assumed role $role_arn successfully."
}

# Set AWS credentials from the assumed role
set_aws_credentials() {
  local assume_role_output=$1

  local access_key_id
  local secret_access_key

  echo_header "Setting AWS credentials..."

  access_key_id=$(echo "$assume_role_output" | jq -r '.Credentials.AccessKeyId')
  secret_access_key=$(echo "$assume_role_output" | jq -r '.Credentials.SecretAccessKey')
  session_token=$(echo "$assume_role_output" | jq -r '.Credentials.SessionToken')

  if [ -z "$access_key_id" ] || [ -z "$secret_access_key" ] || [ -z "$session_token" ]; then
    echo "Invalid credentials received"
    return 1
  fi

  export AWS_ACCESS_KEY_ID="$access_key_id"
  export AWS_SECRET_ACCESS_KEY="$secret_access_key"
  export AWS_SESSION_TOKEN="$session_token"

  echo_footer "AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY have been set."
}

# Create kind cluster
create_kind_cluster() {
  echo_header "Creating kind cluster..."
  kind create cluster --name $KIND_CLUSTER_NAME
}

# Build Docker image
build_docker_image() {
  echo_header "Building Docker image..."
  docker build -t $AUTH_SERVICE_DOCKER_IMAGE .
  echo_footer "Docker image built successfully."
}

# Load Docker image into kind cluster
load_docker_image() {
  echo_header "Loading Docker image into kind cluster..."
  kind load docker-image $AUTH_SERVICE_DOCKER_IMAGE --name $KIND_CLUSTER_NAME
  echo_footer "Docker image loaded successfully."
}

# Create Kubernetes namespace
create_namespace() {
  echo_header "Creating namespace..."
  kubectl create namespace "$AUTH_SERVICE_NAMESPACE"
  echo_footer "Namespace created successfully."
}

# Apply Kubernetes configuration using Helm
apply_k8s_configuration() {
  local deploymentName

  echo_header "Applying Kubernetes configuration using Helm..."

  deploymentName="$(yq r helm/values-dev.yaml 'deploymentName')"
  helm upgrade --install "$deploymentName" helm -f helm/values-dev.yaml --namespace "$AUTH_SERVICE_NAMESPACE" \
    --set spring.aws.accessKeyId="$AWS_ACCESS_KEY_ID" \
    --set spring.aws.secretAccessKey="$AWS_SECRET_ACCESS_KEY" \
    --set spring.aws.sessionToken="$AWS_SESSION_TOKEN"

  echo_footer "Kubernetes configuration applied successfully."
}

# Port forward to PostgreSQL service
port_forward_postgres() {
  echo_header "Port forwarding to PostgreSQL service..."
  kubectl port-forward svc/auth-service 5432:5432 -n "$AUTH_SERVICE_NAMESPACE" &
  echo_footer "Port forwarding set up successfully."
}

# Wait for deployment rollout
wait_for_rollout() {
  echo_header "Waiting for deployment rollout to complete..."
  kubectl rollout status deployment/auth-service-dev -n "$AUTH_SERVICE_NAMESPACE"

  if [ $? -eq 0 ]; then
    echo_footer "Deployment rollout completed successfully."
  else
    echo "Deployment rollout failed!"
    exit 1
  fi
}

# Get the service URL
get_service_url() {
  echo_header "Getting the service URL..."
  SERVICE_URL=$(kubectl get svc auth-service -n "$AUTH_SERVICE_NAMESPACE" -o jsonpath='{.status.loadBalancer.ingress[0].ip}')
  echo_footer "Service is available at http://$SERVICE_URL:8080"
}

# Main script execution
main() {
  echo_app_title
  assume_role
  create_kind_cluster
  build_docker_image
  load_docker_image
  create_namespace
  apply_k8s_configuration
  port_forward_postgres
  wait_for_rollout
  get_service_url
}

main