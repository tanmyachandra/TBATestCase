package com.tba.application.domain;

import java.util.Map;

public class CraneMovementResponse extends Response {
	
	Map<Integer, Object> processSteps;

	public Map<Integer, Object> getProcessSteps() {
		return processSteps;
	}

	public void setProcessSteps(Map<Integer, Object> processSteps) {
		this.processSteps = processSteps;
	}

}
