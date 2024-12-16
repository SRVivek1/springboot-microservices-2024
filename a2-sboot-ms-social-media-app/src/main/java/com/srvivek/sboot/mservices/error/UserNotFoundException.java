package com.srvivek.sboot.mservices.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 4882099180124262207L;

	public UserNotFoundException(String message) {
		super(message);
	}

}
