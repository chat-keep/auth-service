#!/bin/bash

set -e

NAMESPACE="auth-service-dev"
DEPLOYMENT_NAME="auth-service"
SERVICE_NAME="auth-service"
CONFIG_MAP_NAME="auth-service-config"

# Function to delete Kubernetes deployment
delete_deployment() {
  echo "Deleting deployment..."
  kubectl delete deployment $DEPLOYMENT_NAME -n $NAMESPACE
}

# Function to delete Kubernetes service
delete_service() {
  echo "Deleting service..."
  kubectl delete service $SERVICE_NAME -n $NAMESPACE
}

# Function to delete Kubernetes ConfigMap
delete_configmap() {
  echo "Deleting ConfigMap..."
  kubectl delete configmap $CONFIG_MAP_NAME -n $NAMESPACE
}

# Function to delete Kubernetes namespace
delete_namespace() {
  echo "Deleting namespace..."
  kubectl delete namespace $NAMESPACE
}

# Main script execution
main() {
  delete_deployment
  delete_service
  delete_configmap
  delete_namespace
}

main