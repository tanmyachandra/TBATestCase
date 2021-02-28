package com.tba.application.enums;

import lombok.Getter;

public enum ResponseStatus {

	SUCCESS("Success"),
	FAILURE("Failure");
	
	@Getter
    private String statusKey;
	
	public String getStatus() {
		return statusKey;
	}

	ResponseStatus(String statusKey) {
        this.statusKey = statusKey;
    }
}
