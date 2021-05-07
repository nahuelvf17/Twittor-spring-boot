package com.restapi.app.twittor.restFileUpload.exception;

public class FileNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6733762172480111765L;

	public FileNotFoundException(String message) {
        super(message);
    }

    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
