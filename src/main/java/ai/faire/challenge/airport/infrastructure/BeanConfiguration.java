package ai.faire.challenge.airport.infrastructure;

import ai.faire.challenge.airport.model.i18n.Translator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Configuration
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = {"ai.faire.challenge.airport.repository"})
public class BeanConfiguration {

  private static final List<Locale> LOCALES = Arrays.asList(
    Locale.ENGLISH,
    Locale.ITALIAN);

  @Bean
  public MessageSource messageSource() {
    ResourceBundleMessageSource source = new ResourceBundleMessageSource();
    source.setBasenames(
      "i18n/messages/messages");
    source.setUseCodeAsDefaultMessage(true);
    source.setDefaultLocale(Locale.ENGLISH);
    return source;
  }

  @Bean
  public LocaleResolver localeResolver() {
    AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
    localeResolver.setDefaultLocale(Locale.ENGLISH);
    localeResolver.setSupportedLocales(LOCALES);
    return localeResolver;
  }

  @Bean
  public Translator translator(MessageSource messageSource){
    return new Translator(messageSource);
  }

  @Bean
  ObjectMapper yamlReader() {
    return new ObjectMapper(new YAMLFactory());
  }

  @Bean
  @Primary
  ObjectMapper objectMapper() {
    return new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .registerModule(new Jdk8Module())
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  }
}
