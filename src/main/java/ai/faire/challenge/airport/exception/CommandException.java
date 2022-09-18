package ai.faire.challenge.airport.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class CommandException extends DomainRuntimeException {

  public CommandException() {
    this("CME-999");
  }

  public CommandException(String code) {
    this(code, (Object[])null);
  }

  public CommandException(String code, Object[] args) {
    this(code, args, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public CommandException(String code, HttpStatus httpStatus) {
    this(code, (Object[])null, httpStatus, (Object)null);
  }

  public CommandException(String code, Object[] args, HttpStatus httpStatus) {
    this(code, args, httpStatus, (Object)null);
  }

  public CommandException(String code, Object[] args, HttpStatus httpStatus, Object additional) {
    super(code, args, httpStatus, additional);
  }

}
