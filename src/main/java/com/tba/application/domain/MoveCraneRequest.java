package com.tba.application.domain;

import lombok.Data;

@Data
public class MoveCraneRequest {
	
	private Long craneId;
	private Integer startPosition;
	private Integer endPosition;
	
	public MoveCraneRequest(Long craneId, Integer startPosition, Integer endPosition) {
		this.craneId = craneId;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
	}
	
	public Long getCraneId() {
		return craneId;
	}
	public Integer getStartPosition() {
		return startPosition;
	}
	public Integer getEndPosition() {
		return endPosition;
	}

	@Override
	public String toString() {
		return "MoveCraneRequest [craneId=" + craneId + ", startPosition=" + startPosition + ", endPosition="
				+ endPosition + "]";
	}
	

}
