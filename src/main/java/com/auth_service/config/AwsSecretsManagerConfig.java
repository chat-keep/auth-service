package com.auth_service.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

	private final AwsSecretPropsConfig awsSecretPropsConfig;

	@Value("${aws.secretsmanager.secretArn}")
	private String secretArn;

	public AwsSecretsManagerConfig(SecretsManagerClient secretsManagerClient,
			AwsSecretPropsConfig awsSecretPropsConfig) {
		this.secretsManagerClient = secretsManagerClient;
		this.awsSecretPropsConfig = awsSecretPropsConfig;
	}

	/**
	 * Initializes the AWS secret properties by fetching them from AWS Secrets Manager.
	 */
	@PostConstruct
	public void init() throws JsonProcessingException {
		String secretString = getSecretString();
		awsSecretPropsConfig.setJwtSecret(getSecretValue(secretString, "authServiceJwtSecret"));
	}

	/**
	 * Returns the JWT secret from the AWS secret properties.
	 * @return the JWT secret
	 */
	private String getSecretString() throws JsonProcessingException {
		GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder().secretId(secretArn).build();
		GetSecretValueResponse getSecretValueResponse = secretsManagerClient.getSecretValue(getSecretValueRequest);
		return getSecretValueResponse.secretString();
	}

	/**
	 * Returns the value of the specified property from the secret string.
	 * @param secretString the secret string
	 * @param propertyName the name of the property
	 * @return the value of the specified property
	 * @throws JsonProcessingException if an error occurs while processing the JSON
	 */
	private String getSecretValue(String secretString, String propertyName) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode secretJson = objectMapper.readTree(secretString);
		return secretJson.get(propertyName).asText();
	}

	/**
	 * Defines a bean for SecretsManagerClient.
	 * @return the SecretsManagerClient bean
	 */
	@Bean
	public SecretsManagerClient createSecretsManagerClient() {
		return SecretsManagerClient.builder().region(Region.US_EAST_1).build();
	}

}