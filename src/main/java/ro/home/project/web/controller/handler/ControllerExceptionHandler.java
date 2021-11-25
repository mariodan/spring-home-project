package ro.home.project.web.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ro.home.project.exception.BadRequestException;
import ro.home.project.exception.UnauthorizedException;
import ro.home.project.model.RequestError;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    private boolean hideExceptionMessages;

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<RequestError> handleBadRequest(final MethodArgumentTypeMismatchException ex) {
        LOGGER.error("MethodArgumentTypeMismatchException occurred!", ex);
        return buildResponse(ex.getValue() + " is an invalid value for parameter " + ex.getName(), true, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<RequestError> handleBadRequest(final HttpRequestMethodNotSupportedException ex,
                                                         final HttpServletRequest request) {
        final String message = request.getRequestURI() + " cannot be called with " + request.getMethod() + "!";
        LOGGER.error("HttpRequestMethodNotSupportedException occurred: " + message, ex);
        return buildResponse(message, true, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, TypeMismatchException.class})
    public ResponseEntity<RequestError> handleBadRequest(final Exception ex) {
        LOGGER.error("Bad request mapped exception occurred!", ex);
        return buildResponse(ex.getMessage(), true, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RequestError> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        LOGGER.error("MethodArgumentNotValidException exception occurred!", ex);
        final String errors = extractBindingResultErrors(ex.getMessage(), ex.getBindingResult());
        return buildResponse(errors, false, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<RequestError> handleBindException(final BindException ex) {
        LOGGER.error("BindException exception occurred!", ex);
        final String errors = extractBindingResultErrors(ex.getMessage(), ex.getBindingResult());
        return buildResponse(errors, false, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<RequestError> handleParameterValidationException(final ServletRequestBindingException ex) {
        LOGGER.error("ServletRequestBindingException exception occurred!", ex);
        return buildResponse(ex.getMessage(), true, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UnauthorizedException.class, BadRequestException.class})
    public ResponseEntity<RequestError> handleBusinessException(final Exception e) {
        LOGGER.error("Business exception occurred!", e);
        return buildResponse(e.getMessage(), false, e.getClass().getAnnotation(ResponseStatus.class).value());
    }

    /*@ExceptionHandler({HystrixRuntimeException.class, HystrixBadRequestException.class})
    public ResponseEntity<RequestError> handleHystrixException(final Throwable e) {
        LOGGER.error("Hystrix exception occurred!", e);
        return buildResponse(e.getMessage(), true, HttpStatus.I_AM_A_TEAPOT);
    }*/

    @ExceptionHandler({HttpClientErrorException.Unauthorized.class, HttpClientErrorException.BadRequest.class})
    public ResponseEntity<String> handleHttpClientBusinessExceptions(final HttpClientErrorException e) {
        LOGGER.error("Http client exception occurred!", e);
        return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RequestError> handleAppException(final Exception e) {
        LOGGER.error("Handling encountered exception...", e);
        final HttpStatus status;
        final String message = e.getMessage() == null ?
                e.getClass().getName() : e.getMessage();
        if (e.getClass().isAnnotationPresent(ResponseStatus.class)) {
            status = e.getClass().getAnnotation(ResponseStatus.class).value();
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return buildResponse(message, true, status);
    }

    private String extractBindingResultErrors(final String errors, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String defaultCombinedErrMessages = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList()).toString();
            if (bindingResult.hasFieldErrors()) {
                defaultCombinedErrMessages = bindingResult
                        .getFieldErrors()
                        .stream()
                        .map(fld -> fld.getField().concat(":").concat(fld.getDefaultMessage()))
                        .collect(Collectors.toList()).toString();
            }
            return defaultCombinedErrMessages;
        }
        return errors;
    }

    private ResponseEntity<RequestError> buildResponse(final String message,
                                                       final boolean hideableMessage,
                                                       final HttpStatus status) {

        final String errorMessage;
        if (hideableMessage && hideExceptionMessages) {
            errorMessage = null;
        } else {
            errorMessage = message;
        }
        final RequestError requestError = new RequestError(errorMessage, status.value());
        LOGGER.info("Returning request error is {}.", requestError);
        return new ResponseEntity<>(requestError, status);
    }

    @Value("${ath.hide-exception-messages:false}")
    public void setHideExceptionMessages(final boolean hideExceptionMessages) {
        this.hideExceptionMessages = hideExceptionMessages;
    }

/*    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<String> handleAccessDeniedException(final AccessDeniedException accessDeniedException) {
        LOGGER.error("User access denied! Check user roles and authorities.", accessDeniedException);
        return new ResponseEntity<>("ACC00", HttpStatus.UNAUTHORIZED);
    }*/

}
