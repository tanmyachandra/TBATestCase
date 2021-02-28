package com.tba.application.enums;

import lombok.Getter;

public enum CraneStatus {
	
	IDLE("Idle"),
	RUNNING("Running");
	
	@Getter
    private String statusKey;

	CraneStatus(String statusKey) {
        this.statusKey = statusKey;
    }

}
