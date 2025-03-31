#!/bin/bash

set -e

KIND_CLUSTER_NAME="local-cluster"
AUTH_SERVICE_NAMESPACE="auth-service-dev"
AUTH_SERVICE_DOCKER_IMAGE_TAG="0.0.1"
AUTH_SERVICE_DOCKER_IMAGE="auth-service-dev:$AUTH_SERVICE_DOCKER_IMAGE_TAG"

# Load utility functions
source ./scripts/local_util.sh

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
  echo_header "Applying Kubernetes configuration using Helm..."
  local deploymentName
  deploymentName="$(yq r helm/values-dev.yaml 'deploymentName')"
  helm upgrade --install "$deploymentName" helm -f helm/values-dev.yaml --namespace "$AUTH_SERVICE_NAMESPACE"
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

# Pull PostgreSQL image
pull_postgres_image() {
  echo_header "Pulling PostgreSQL image..."
  local db_name="notes_ai_bot_db"

  docker pull postgres:latest
  docker run -d \
      --name postgres \
      -e POSTGRES_PASSWORD=admin \
      -e POSTGRES_USER=admin \
      -e POSTGRES_DB=$db_name \
      -p 5434:5432 \
      -v postgres_data:/var/lib/postgresql/data \
      postgres:latest

  echo_footer "PostgreSQL image pulled and container started successfully."
}

# Main script execution
main() {
#  pull_postgres_image
  echo_app_title
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