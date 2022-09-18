package ai.faire.challenge.airport.infrastructure;

import ai.faire.challenge.airport.exception.DomainRuntimeException;
import ai.faire.challenge.airport.model.api.Response;
import com.amadeus.exceptions.ResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ArchExceptionHandlerManager extends ResponseEntityExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handException(Exception exception) {

    HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    Response<Void> responseError = Response.<Void>builder()
      .code("GER-000")
      .additional(exception.getMessage())
      .build();
    traceException(exception, responseError);
    return new ResponseEntity<>(responseError, new HttpHeaders(), httpStatus);
  }

  @ExceptionHandler(DomainRuntimeException.class)
  private ResponseEntity<Object> handleArchDomainRuntimeException(DomainRuntimeException ex){
    Response<Void> responseError = Response.<Void>builder()
      .code(ex.getCode(), ex.getArgs())
      .additional(ex.getAdditional())
      .defaultCodeAndMessage(ex.getAlternativeCode(), ex.getAlternativeMessage())
      .forceCodeAndMessage(ex.getForceCode(), ex.getForceMessage())
      .build();
    traceException(ex, responseError);
    return new ResponseEntity<>(responseError, new HttpHeaders(), ex.getStatus());
  }

  @ExceptionHandler(ResponseException.class)
  private ResponseEntity<Object> handleAmadeusApiException(ResponseException ex){
    com.amadeus.Response error = ex.getResponse();
    Response<Void> responseError = Response.<Void>builder()
      .code("APIERROR-001")
      .additional(error.getBody())
      .build();
    traceException(ex, responseError);
    return new ResponseEntity<>(responseError, new HttpHeaders(), error.getStatusCode());
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers,
                                                                HttpStatus status,
                                                                WebRequest request) {
    String code = "";
    String alternativeCode = "VAE-999";
    Object[] arguments = null;
    List<FieldError> fieldErrorList = ex.getBindingResult().getFieldErrors();
    List<ObjectError> globalErrorList = ex.getBindingResult().getGlobalErrors();

    if(!globalErrorList.isEmpty()){
      code = globalErrorList.get(0).getDefaultMessage();
      arguments = globalErrorList.get(0).getArguments();
    } else if(!fieldErrorList.isEmpty()){
      code = fieldErrorList.get(0).getDefaultMessage();
      arguments = fieldErrorList.get(0).getArguments();
    }

    List<String> errors = new ArrayList<>();
    for (FieldError error : fieldErrorList) {
      errors.add(error.getField() + ": " + error.getDefaultMessage());
    }
    for (ObjectError error : globalErrorList) {
      errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
    }

    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    Map<String, Object> additional = new HashMap<>();
    additional.put("arguments", arguments);
    additional.put("errors", errors.toString());
    additional.put("localizedMessage", ex.getLocalizedMessage());
    Response<Void> responseError = Response.<Void>builder()
      .code(code)
      .additional(additional)
      .defaultCode(alternativeCode)
      .build();
    traceException(ex, responseError);
    return handleExceptionInternal(ex, responseError, headers, httpStatus, request);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                        HttpHeaders headers,
                                                                        HttpStatus status,
                                                                        WebRequest request) {
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    Object[] params = new Object[1];
    params[0] = ex.getParameterName();
    Response<Void> responseError = Response.<Void>builder()
      .code("GER-001", params)
      .build();
    traceException(ex, responseError);
    return new ResponseEntity<>(responseError, new HttpHeaders(), httpStatus);
  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    Object[] params = new Object[2];
    params[0] = ex.getName();
    params[1] = ex.getRequiredType() != null ? ex.getRequiredType().getName(): "";
    Response<Void> responseError = Response.<Void>builder()
      .code("GER-002", params)
      .build();
    traceException(ex, responseError);
    return new ResponseEntity<>(responseError, new HttpHeaders(), httpStatus);
  }

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                 HttpHeaders headers,
                                                                 HttpStatus status,
                                                                 WebRequest request) {
    HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    Object[] params = new Object[2];
    params[0] = ex.getHttpMethod();
    params[1] =  ex.getRequestURL();
    Response<Void> responseError = Response.<Void>builder()
      .code("GER-003", params)
      .build();
    traceException(ex, responseError);
    return new ResponseEntity<>(responseError, new HttpHeaders(), httpStatus);
  }

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                       HttpHeaders headers,
                                                                       HttpStatus status,
                                                                       WebRequest request) {
    HttpStatus httpStatus = HttpStatus.METHOD_NOT_ALLOWED;
    StringBuilder builder = new StringBuilder();
    if(ex.getSupportedHttpMethods() != null){
      ex.getSupportedHttpMethods().forEach(t -> builder.append(t).append(" "));
    }
    Object[] params = new Object[2];
    params[0] = ex.getMethod();
    params[1] =  builder.toString();

    Response<Void> responseError = Response.<Void>builder()
      .code("GER-004", params)
      .build();
    traceException(ex, responseError);
    return new ResponseEntity<>(responseError, new HttpHeaders(), httpStatus);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                HttpHeaders headers, HttpStatus status, WebRequest request) {
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    Object[] params = new Object[1];
    params[0] = ex.getMessage();
    Response<Void> responseError = Response.<Void>builder()
      .code("GER-005", params)
      .build();
    traceException(ex, responseError);
    return new ResponseEntity<>(responseError, new HttpHeaders(), httpStatus);
  }

  private void traceException(Exception e, Response<?> response){
    log.error("{} {}", response, "eMessage=" + e.getMessage());
  }

}
