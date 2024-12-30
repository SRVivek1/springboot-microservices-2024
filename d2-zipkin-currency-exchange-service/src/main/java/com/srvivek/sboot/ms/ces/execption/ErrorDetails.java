package com.srvivek.sboot.ms.ces.execption;

import java.time.LocalDateTime;

public class ErrorDetails {

	private LocalDateTime timestamp;
	private String messages;
	private String description;

	public ErrorDetails(String messages, String description) {
		super();
		this.timestamp = LocalDateTime.now();
		this.messages = messages;
		this.description = description;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "ErrorDetails [timestamp=" + timestamp + ", messages=" + messages + ", description=" + description + "]";
	}
}
