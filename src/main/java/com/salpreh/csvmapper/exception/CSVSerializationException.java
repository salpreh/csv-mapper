package com.salpreh.csvmapper.exception;

public class CSVSerializationException extends RuntimeException {

	private static final long serialVersionUID = 206925373920135652L;

	public CSVSerializationException(String message) {
		super(message);
	}

	public CSVSerializationException(String message, Throwable cause) {
		super(message, cause);
	}
}
