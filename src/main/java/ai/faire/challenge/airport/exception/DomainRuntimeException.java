package ai.faire.challenge.airport.exception;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class DomainRuntimeException extends RuntimeException {

  private String code;
  private Object[] args;
  private HttpStatus status;
  private Object additional;
  private String alternativeCode;
  private String alternativeMessage;
  private String forceCode;
  private String forceMessage;

  public DomainRuntimeException(String code, HttpStatus status) {
    this(code, (Object[])null, status);
  }

  public DomainRuntimeException(String code, Object[] args, HttpStatus status) {
    this(code, args, status, (Object)null);
  }

  public DomainRuntimeException(String code, Object[] args, HttpStatus status, Object additional) {
    this(code, args, status, additional, (String)null, (String)null, (String)null, (String)null);
  }

  public DomainRuntimeException(String code, Object[] args, HttpStatus status, Object additional, String alternativeCode, String alternativeMessage) {
    this(code, args, status, additional, alternativeCode, alternativeMessage, (String)null, (String)null);
  }

  public DomainRuntimeException(String code, Object[] args, HttpStatus status, Object additional, String alternativeCode, String alternativeMessage, String forceCode, String forceMessage) {
    super((String) StringUtils.defaultIfEmpty(code, ""));
    this.code = code;
    this.args = args;
    this.status = status;
    this.additional = additional;
    this.alternativeCode = alternativeCode;
    this.alternativeMessage = alternativeMessage;
    this.forceCode = forceCode;
    this.forceMessage = forceMessage;
  }

  public static DomainRuntimeException.DomainRuntimeExceptionBuilder builder() {
    return new DomainRuntimeException.DomainRuntimeExceptionBuilder();
  }

  public void setCode(final String code) {
    this.code = code;
  }

  public void setArgs(final Object[] args) {
    this.args = args;
  }

  public void setStatus(final HttpStatus status) {
    this.status = status;
  }

  public void setAdditional(final Object additional) {
    this.additional = additional;
  }

  public void setAlternativeCode(final String alternativeCode) {
    this.alternativeCode = alternativeCode;
  }

  public void setAlternativeMessage(final String alternativeMessage) {
    this.alternativeMessage = alternativeMessage;
  }

  public void setForceCode(final String forceCode) {
    this.forceCode = forceCode;
  }

  public void setForceMessage(final String forceMessage) {
    this.forceMessage = forceMessage;
  }

  public String getCode() {
    return this.code;
  }

  public Object[] getArgs() {
    return this.args;
  }

  public HttpStatus getStatus() {
    return this.status;
  }

  public Object getAdditional() {
    return this.additional;
  }

  public String getAlternativeCode() {
    return this.alternativeCode;
  }

  public String getAlternativeMessage() {
    return this.alternativeMessage;
  }

  public String getForceCode() {
    return this.forceCode;
  }

  public String getForceMessage() {
    return this.forceMessage;
  }

  public static class DomainRuntimeExceptionBuilder {
    private String code;
    private Object[] args;
    private HttpStatus status;
    private Object additional;
    private String alternativeMessage;
    private String alternativeCode;
    private String forceCode;
    private String forceMessage;

    private DomainRuntimeExceptionBuilder() {
      this.code = "";
      this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public DomainRuntimeException.DomainRuntimeExceptionBuilder code(String code) {
      this.code = code;
      return this;
    }

    public DomainRuntimeException.DomainRuntimeExceptionBuilder args(Object[] args) {
      this.args = args;
      return this;
    }

    public DomainRuntimeException.DomainRuntimeExceptionBuilder status(HttpStatus status) {
      this.status = status;
      return this;
    }

    public DomainRuntimeException.DomainRuntimeExceptionBuilder additional(Object additional) {
      this.additional = additional;
      return this;
    }

    public DomainRuntimeException.DomainRuntimeExceptionBuilder alternativeMessage(String alternativeMessage) {
      this.alternativeMessage = alternativeMessage;
      return this;
    }

    public DomainRuntimeException.DomainRuntimeExceptionBuilder alternativeCode(String alternativeCode) {
      this.alternativeCode = alternativeCode;
      return this;
    }

    public DomainRuntimeException.DomainRuntimeExceptionBuilder forceCodeAndMessage(String code, String message) {
      this.forceCode = code;
      this.forceMessage = message;
      return this;
    }

    public DomainRuntimeException build() {
      return new DomainRuntimeException(this.code, this.args, this.status, this.additional, this.alternativeCode, this.alternativeMessage, this.forceCode, this.forceMessage);
    }
  }

}
