package com.auth_service.config;

import com.auth_service.exception.InvalidAwsSecretStringException;
import com.auth_service.exception.InvalidAwsSecretValueException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

/**
 * AwsSecretsManagerConfig class. Used for fetching the AWS secret properties.
 */
@Configuration
public class AwsSecretsManagerConfig {

	private final SecretsManagerClient secretsManagerClient;

	@Value("${aws.secretsmanager.secretArn}")
	private String secretArn;

	public AwsSecretsManagerConfig() {
		this.secretsManagerClient = buildSecretsManagerClient();
	}

	/**
	 * @description Initializes the configuration by fetching the secret properties from
	 * AWS Secrets Manager.
	 * @throws JsonProcessingException if an error occurs while processing JSON
	 */
	@PostConstruct
	public void init() throws JsonProcessingException {
		String secretString = fetchSecretString();

		if (validateSecretString(secretString)) {
			throw new InvalidAwsSecretStringException();
		}

		setSecretProperty(secretString, "authServiceJwtSecret", "JWT_SECRET");
		setSecretProperty(secretString, "authServiceAccessTokenValidity", "AUTH_SERVICE_ACCESS_TOKEN_VALIDITY");
		setSecretProperty(secretString, "authServiceRefreshTokenValidity", "AUTH_SERVICE_REFRESH_TOKEN_VALIDITY");
	}

	/**
	 * @description Validates the secret string.
	 * @param secretString the secret string to validate
	 * @return true if the secret string is valid, false otherwise
	 */
	private boolean validateSecretString(String secretString) {
		return secretString == null || secretString.isEmpty();
	}

	/**
	 * @description Fetches the secret string from AWS Secrets Manager.
	 * @return the secret string
	 */
	private String fetchSecretString() {
		GetSecretValueRequest request = createSecretValueRequest();
		GetSecretValueResponse response = secretsManagerClient.getSecretValue(request);

		return response.secretString();
	}

	/**
	 * @description Creates a request to fetch the secret value from AWS Secrets Manager.
	 * @return the GetSecretValueRequest
	 */
	private GetSecretValueRequest createSecretValueRequest() {
		return GetSecretValueRequest.builder().secretId(secretArn).build();
	}

	/**
	 * @description Validates the secret value.
	 * @param secretValue the secret value to validate
	 * @return true if the secret value is valid, false otherwise
	 */
	private boolean validateSecretValue(String secretValue) {
		return secretValue != null && !secretValue.isEmpty();
	}

	/**
	 * @description Sets the secret property in the system properties.
	 * @param secretString the secret string
	 * @param propertyName the property name to extract from the secret string
	 * @param systemPropertyName the system property name to set
	 * @throws JsonProcessingException if an error occurs while processing JSON
	 */
	private void setSecretProperty(String secretString, String propertyName, String systemPropertyName)
			throws JsonProcessingException {
		String secretValue = extractSecretValue(secretString, propertyName);

		if (!validateSecretValue(secretValue)) {
			throw new InvalidAwsSecretValueException();
		}

		System.setProperty(systemPropertyName, secretValue);
	}

	/**
	 * @description Extracts the secret value from the secret string.
	 * @param secretString the secret string
	 * @param propertyName the property name to extract
	 * @return the extracted secret value
	 * @throws JsonProcessingException if an error occurs while processing JSON
	 */
	private String extractSecretValue(String secretString, String propertyName) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode secretJson = objectMapper.readTree(secretString);
		return secretJson.get(propertyName).asText();
	}

	/**
	 * @description Builds the SecretsManagerClient based on the environment credentials.
	 * @return the SecretsManagerClient
	 */
	private SecretsManagerClient buildSecretsManagerClient() {
		if (areEnvironmentCredentialsAvailable()) {
			return createClientWithStaticCredentials();
		}

		return createClientWithWebIdentityCredentials();
	}

	/**
	 * @description Checks if the environment credentials are available.
	 * @return true if environment credentials are available, false otherwise
	 */
	private boolean areEnvironmentCredentialsAvailable() {
		String accessKeyId = System.getenv("AWS_ACCESS_KEY_ID");
		String secretAccessKey = System.getenv("AWS_SECRET_ACCESS_KEY");
		String sessionToken = System.getenv("AWS_SESSION_TOKEN");

		return accessKeyId != null && secretAccessKey != null && sessionToken != null;
	}

	/**
	 * @description Creates a SecretsManagerClient with static credentials.
	 * @return the SecretsManagerClient
	 */
	private SecretsManagerClient createClientWithStaticCredentials() {
		AwsSessionCredentials awsCreds = AwsSessionCredentials.create(System.getenv("AWS_ACCESS_KEY_ID"),
				System.getenv("AWS_SECRET_ACCESS_KEY"), System.getenv("AWS_SESSION_TOKEN"));
		return SecretsManagerClient.builder().region(Region.of(System.getenv("AWS_REGION")))
				.credentialsProvider(StaticCredentialsProvider.create(awsCreds)).build();
	}

	/**
	 * @description Creates a SecretsManagerClient with web identity credentials.
	 * @return the SecretsManagerClient
	 */
	private SecretsManagerClient createClientWithWebIdentityCredentials() {
		return SecretsManagerClient.builder().region(Region.of(System.getenv("AWS_REGION")))
				.credentialsProvider(WebIdentityTokenFileCredentialsProvider.create()).build();
	}

}