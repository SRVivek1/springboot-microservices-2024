package com.srvivek.sboot.ms.ces.execption;

public class CurrencyNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -68073690974918027L;

	private String message;

	public CurrencyNotFoundException(String message) {
		super(message);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "CurrencyNotFoundException [message=" + message + "]";
	}

}
