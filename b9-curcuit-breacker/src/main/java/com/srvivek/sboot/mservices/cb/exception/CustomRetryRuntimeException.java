package com.srvivek.sboot.mservices.cb.exception;

public class CustomRetryRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 5542097379620049866L;

	public CustomRetryRuntimeException(String message) {
		super(message);
	}
}
