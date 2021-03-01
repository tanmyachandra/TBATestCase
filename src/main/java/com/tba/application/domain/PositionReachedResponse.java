package com.tba.application.domain;

import com.tba.application.entities.YardMapping;

public class PositionReachedResponse extends Response {

	private YardMapping yardMappingResponse;

	public YardMapping getYardMappingResponse() {
		return yardMappingResponse;
	}

	public void setYardMappingResponse(YardMapping yardMappingResponse) {
		this.yardMappingResponse = yardMappingResponse;
	}
	
	
}
