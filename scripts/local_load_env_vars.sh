#!/bin/bash

# Function to add or update key-value pairs in the .env file
add_or_update_env_var() {
  local key=$1
  local value=$2
  local env_file="../.env"

  if grep -q "^${key}=" "$env_file"; then
    # Update the existing key with the new value
    sed -i "s|^${key}=.*|${key}=${value}|" "$env_file"
  else
    # Add the new key-value pair to the .env file
    echo "${key}=${value}" >> "$env_file"
  fi
}

# Replace the JWT secret in the .env file
replace_secret() {
  local jwt_secret_key="$1"
  local new_jwt_secret="$2"

  add_or_update_env_var "$jwt_secret_key" "$new_jwt_secret"
}