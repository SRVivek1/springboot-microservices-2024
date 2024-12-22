package com.srvivek.sboot.mservices.error;

import java.time.LocalDateTime;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Generic exception handling.
	 * 
	 * @param ex
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ExceptionHandler(exception = Exception.class)
	public final ResponseEntity<ErrorDetails> handleAllException(Exception ex, WebRequest request) throws Exception {

		logger.error("Error:  {}", ex);
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(),
				request.getDescription(false));

		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Return HTTP 404 for UserNotFoundException.
	 * 
	 * @param ex
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ExceptionHandler(exception = UserNotFoundException.class)
	public final ResponseEntity<ErrorDetails> handleUserNotFoundException(Exception ex, WebRequest request)
			throws Exception {

		logger.error("Error:  {}", ex);
		
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(),
				request.getDescription(false));

		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		logger.error("Error:  {}", ex);
		
		final StringBuilder fieldErros = new StringBuilder();

		Consumer<? super FieldError> consumer = fe -> fieldErros.append(fe.getDefaultMessage()).append(" | ");
		ex.getFieldErrors().stream().forEach(consumer);

		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), fieldErros.toString(),
				request.getDescription(false));

		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
}
