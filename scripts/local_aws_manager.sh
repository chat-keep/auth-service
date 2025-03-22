#!/bin/bash

set -e

AWS_ACCOUNT_ID=$AWS_ACCOUNT_ID

# Load utility functions
source "$(dirname "$0")/scripts/local_util.sh"

# Unset existing AWS environment variables
unset_aws_env_vars() {
  echo_header "Unsetting existing AWS environment variables..."
  unset AWS_ACCESS_KEY_ID
  unset AWS_SECRET_ACCESS_KEY
  unset AWS_SESSION_TOKEN
  unset AWS_DEFAULT_REGION
  echo "AWS environment variables unset successfully."
}

# Assume AWS role
assume_aws_role() {
  local role_arn=$1
  local session_name=$2

  echo_header "Assuming AWS role... "
  aws sts assume-role --role-arn "$role_arn" --role-session-name "$session_name" --query 'Credentials' --output json
  echo "AWS role assumed successfully."
}

# Set AWS environment variables
set_aws_env_vars() {
  echo_header "Setting AWS environment variables..."

  export AWS_ACCESS_KEY_ID=$(aws configure get aws_access_key_id)
  export AWS_SECRET_ACCESS_KEY=$(aws configure get aws_secret_access_key)
  export AWS_SESSION_TOKEN=$(aws configure get aws_session_token)
  export AWS_DEFAULT_REGION="$REGION"

  echo "AWS environment variables set successfully."
}

# Get secret value from AWS Secrets Manager
get_secret_value() {
  local secret_arn=$1

  SECRET_VALUE=$(aws secretsmanager get-secret-value --secret-id "$secret_arn" --query 'SecretString' --output text)
  echo "$SECRET_VALUE"
}

# Set secret as environment variable
set_secret_env_var() {
  local secret_value=$1
  local secret_key=$2
  local authServiceJwtSecret

  echo_header "Replacing Secret in .env file..."
  source "$(dirname "$0")/scripts/local_load_env_vars.sh"
  replace_secret "$secret_key" "$authServiceJwtSecret"
  echo "Secret replaced successfully."
}

# Set secrets
set_secrets() {
  local secret_arn="arn:aws:secretsmanager:$REGION:$AWS_ACCOUNT_ID:secret:$SECRET_NAME"
  local secret_value

  echo_header "Getting secrets..."

  secret_value=$(get_secret_value "$secret_arn")
  authServiceJwtSecret=$(echo "$secret_value" | jq -r '.authServiceJwtSecret')

  set_secret_env_var "$secret_value" "$secret_key"
  echo "Secrets set successfully."
}

# Main script execution
main() {
  local role_arn="arn:aws:iam::$AWS_ACCOUNT_ID:role/$ROLE_NAME"
  local session_name="local-session"
  local secret_key="SPRING_AUTH_SERVICE_JWT_SECRET"

  unset_aws_env_vars
  assume_aws_role "$role_arn" "$session_name"
  set_aws_env_vars
  set_secrets
}

main