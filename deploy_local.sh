#!/bin/bash

set -e

KIND_CLUSTER_NAME="local-cluster"
AUTH_SERVICE_NAMESPACE="auth-service-dev"
AUTH_SERVICE_DOCKER_IMAGE_TAG="0.0.1"
AUTH_SERVICE_DOCKER_IMAGE="auth-service-dev:$AUTH_SERVICE_DOCKER_IMAGE_TAG"

# Load utility functions
source ./scripts/local_util.sh

# Source scripts
source_scripts() {
  echo_header "Sourcing AWS manager script..."

  if [ -f ./scripts/local_aws_manager.sh ]; then
    source ./scripts/local_aws_manager.sh
    echo "AWS manager script sourced successfully."
  else
    echo "AWS manager script not found!"
    exit 1
  fi
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
}

# Load Docker image into kind cluster
load_docker_image() {
  echo_header "Loading Docker image into kind cluster..."
  kind load docker-image $AUTH_SERVICE_DOCKER_IMAGE --name $KIND_CLUSTER_NAME
}

# Create Kubernetes namespace
create_namespace() {
  echo_header "Creating namespace..."
  kubectl create namespace "$AUTH_SERVICE_NAMESPACE"
}

# Apply Kubernetes configuration using Helm
apply_k8s_configuration() {
  echo_header "Applying Kubernetes configuration using Helm..."
  local deploymentName
  deploymentName="$(yq r helm/values-dev.yaml 'deploymentName')"
  helm upgrade --install "$deploymentName" helm -f helm/values-dev.yaml --namespace "$AUTH_SERVICE_NAMESPACE"
}

# Wait for deployment rollout
wait_for_rollout() {
  echo_header "Waiting for deployment rollout to complete..."
  kubectl rollout status deployment/auth-service-dev -n "$AUTH_SERVICE_NAMESPACE"
}

# Get the service URL
get_service_url() {
  echo_header "Getting the service URL..."
  SERVICE_URL=$(kubectl get svc auth-service -n "$AUTH_SERVICE_NAMESPACE" -o jsonpath='{.status.loadBalancer.ingress[0].ip}')
  echo "Service is available at http://$SERVICE_URL:8080"
}

# Main script execution
main() {
  source_scripts
#  create_kind_cluster
#  build_docker_image
#  load_docker_image
#  create_namespace
  apply_k8s_configuration
  wait_for_rollout
  get_service_url
}

main