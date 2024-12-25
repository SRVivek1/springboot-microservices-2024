package com.srvivek.sboot.mservices.cb.exception;

public class DefaultRetryRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 5542097379620049866L;

	public DefaultRetryRuntimeException(String message) {
		super(message);
	}
}
