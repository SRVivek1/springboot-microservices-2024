package com.srvivek.sboot.mservices.error;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
//@ControllerAdvice(basePackages = "com.srvivek.x.y.z") //for specific package
//@Order(value = 1) // for defining ordering
//@Priority(value = 1) // for defining priority
//@RestControllerAdvice(basePackages = "com.srvivek.x.y.z") // @ControllerAdvice + @ResponseBody
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(CustomizedResponseEntityExceptionHandler.class);

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

		logger.error("Error stacktrace: {}", ex);

		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(),
				request.getDescription(false));

		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
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

		logger.error("Error stacktrace: {}", ex);

		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(),
				request.getDescription(false));

		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND);
	}
}
