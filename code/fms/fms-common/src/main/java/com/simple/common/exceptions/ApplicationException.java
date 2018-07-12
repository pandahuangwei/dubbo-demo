package com.simple.common.exceptions;

/**********************************************************************************
 * Copyright(c)2017 Simple-air.com All rights reserved.
 * @Title: ApplicationException.java.
 * @Package com.simple.common.exceptions.
 * @Description: 应用异常：由Service层抛出.
 * 
 * @author guowei.
 * @version 1.0.
 * @created 2017/3/1 17:50.
 **********************************************************************************/
public class ApplicationException extends Exception {
	private static final long serialVersionUID = 1L;

	private int errorCode = 0;

	private String errorMessage;

	private Exception originalException;

	public ApplicationException(int code) {
		super();
		errorCode = code;
	}

	public ApplicationException(int code, String message) {
		super(message);
		errorCode = code;
		errorMessage = message;
	}

	public ApplicationException(int code, Exception exception) {
		super(exception);
		errorCode = code;
		originalException = exception;
	}

	public ApplicationException(int code, String message, Exception exception) {
		super(message, exception);
		errorCode = code;
		originalException = exception;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Exception getOriginalException() {
		return originalException;
	}

	public void setOriginalException(Exception originalException) {
		this.originalException = originalException;
	}
}