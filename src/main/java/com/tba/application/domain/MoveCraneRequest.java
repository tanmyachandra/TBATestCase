package com.tba.application.domain;

import lombok.Data;

@Data
public class MoveCraneRequest {
	
	private Long craneId;
	private Integer startPosition;
	private Integer endPosition;
	private Boolean isBeingParked;
	
	public MoveCraneRequest(Long craneId, Integer startPosition, 
			Integer endPosition, Boolean isBeingParked) {
		this.craneId = craneId;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.isBeingParked = isBeingParked;
	}
	
	public MoveCraneRequest(Long craneId, Integer startPosition, Integer endPosition) {
		this.craneId = craneId;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
	}
	
	public MoveCraneRequest(Long craneId, Integer endPosition) {
		this.craneId = craneId;
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
	public Boolean getIsBeingParked() {
		return isBeingParked;
	}

	public void setStartPosition(Integer startPosition) {
		this.startPosition = startPosition;
	}

	@Override
	public String toString() {
		return "MoveCraneRequest [craneId=" + craneId + ", startPosition=" + startPosition + ", endPosition="
				+ endPosition + "isBeingParked=" + isBeingParked + "]";
	}
	

}
