package com.tba.application.domain;

import lombok.Data;

@Data
public class PublisherEvent {
	
	private Long yardModuleId;
	
	private Long craneId;
	
	private Integer startPosition;
	
	private Integer endPosition;
	
	private Boolean canProcessDirectly;
	
	private Boolean isBeingParked;

	public PublisherEvent() {};
	
	public PublisherEvent(Long yardModuleId, Long craneId, 
			Integer startPosition, Integer endPosition, Boolean canProcessDirectly, Boolean isBeingParked) {
		this.yardModuleId = yardModuleId;
		this.craneId = craneId;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.canProcessDirectly = canProcessDirectly;
		this.isBeingParked = isBeingParked;
	}

	public Long getYardModuleId() {
		return yardModuleId;
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

	public Boolean getCanProcessDirectly() {
		return canProcessDirectly;
	}
	
	public Boolean getIsBeingParked() {
		return isBeingParked;
	}

	public void setIsBeingParked(Boolean isBeingParked) {
		this.isBeingParked = isBeingParked;
	}

	@Override
	public String toString() {
		return "PublisherEvent [yardModuleId=" + yardModuleId + ", craneId=" + craneId + ", startPosition="
				+ startPosition + ", endPosition=" + endPosition + ", canProcessDirectly=" + canProcessDirectly 
				+ ", isBeingParked=" + isBeingParked + "]";
	}
	
	
	

}
