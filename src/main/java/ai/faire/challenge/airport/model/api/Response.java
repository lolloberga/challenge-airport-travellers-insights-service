package ai.faire.challenge.airport.model.api;

import ai.faire.challenge.airport.model.i18n.Translator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.Instant;
import java.util.Locale;

public class Response<T> {

  private String code;
  private String message;
  private Object additional;
  private String language;
  private Long timestamp;
  private T data;

  public static <T> Response.ResponseBuilder<T> builder() {
    return new Response.ResponseBuilder();
  }

  private Response() {
  }

  public String getCode() {
    return this.code;
  }

  public String getMessage() {
    return this.message;
  }

  public Object getAdditional() {
    return this.additional;
  }

  public String getLanguage() {
    return this.language;
  }

  public Long getTimestamp() {
    return this.timestamp;
  }

  public T getData() {
    return this.data;
  }

  public String toString() {
    return "Response(code=" + this.getCode() + ", message=" + this.getMessage() + ", additional=" + this.getAdditional() + ", language=" + this.getLanguage() + ", timestamp=" + this.getTimestamp() + ", data=" + this.getData() + ")";
  }

  public static class ResponseBuilder<T> {
    private final Response<T> response = new Response();
    private Object[] args;
    private String defaultCode;
    private String defaultMessage;
    private String forceCode;
    private String forceMessage;

    public ResponseBuilder() {
    }

    public Response<T> build() {
      Locale locale = LocaleContextHolder.getLocale();
      this.response.timestamp = Instant.now().getEpochSecond();
      this.response.language = locale.getLanguage();
      if (StringUtils.isNotEmpty(this.forceCode) && StringUtils.isNotEmpty(this.forceMessage)) {
        this.response.code = this.forceCode;
        this.response.message = this.forceMessage;
        return this.response;
      } else {
        String[] codeAndMessage = Translator.getInstance().getMessage(this.response.code, this.args, this.defaultCode, this.defaultMessage, locale);
        this.response.code = codeAndMessage[0];
        this.response.message = codeAndMessage[1];
        return this.response;
      }
    }

    public Response.ResponseBuilder<T> code(String code) {
      this.response.code = code;
      return this;
    }

    public Response.ResponseBuilder<T> code(String code, Object[] args) {
      this.response.code = code;
      this.args = args;
      return this;
    }

    public Response.ResponseBuilder<T> data(T data) {
      this.response.data = data;
      return this;
    }

    public Response.ResponseBuilder<T> additional(Object additional) {
      this.response.additional = additional;
      return this;
    }

    public Response.ResponseBuilder<T> defaultCodeAndMessage(String defaultCode, String defaultMessage) {
      this.defaultCode = defaultCode;
      return this.defaultMessage(defaultMessage);
    }

    public Response.ResponseBuilder<T> forceCodeAndMessage(String forceCode, String forceMessage) {
      this.forceCode = forceCode;
      this.forceMessage = forceMessage;
      return this;
    }

    public Response.ResponseBuilder<T> defaultMessage(String defaultMessage) {
      this.defaultMessage = defaultMessage;
      return this;
    }

    public Response.ResponseBuilder<T> defaultCode(String defaultCode) {
      this.defaultCode = defaultCode;
      return this;
    }
  }
}
