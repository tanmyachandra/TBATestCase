package com.tba.application.domain;

import lombok.Data;

@Data
public class Response {
	
	private String responseStatus;
	private String responseMessage;
	
	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	
}
