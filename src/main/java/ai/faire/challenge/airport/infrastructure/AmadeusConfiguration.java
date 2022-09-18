package ai.faire.challenge.airport.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmadeusConfiguration {

  @Value("${amadeus.api_key}")
  private String apiKey;

  @Value("${amadeus.api_secret}")
  private String apiSecret;

  @Value("${amadeus.environment}")
  private String environment;

  @Value("${amadeus.loglevel}")
  private String loglevel;

  @Bean
  public String getApiKey() {
    return apiKey;
  }

  @Bean
  public String getApiSecret() {
    return apiSecret;
  }

  @Bean
  public String getEnvironment() {
    return environment;
  }

  public String getLoglevel() {
    return loglevel;
  }
}
