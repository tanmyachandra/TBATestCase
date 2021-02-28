package com.tba.application.domain;

import lombok.Data;

@Data
public class PublisherEvent {
	
	private Long yardModuleId;
	
	private Long craneId;
	
	private Integer startPosition;
	
	private Integer endPosition;
	
	private Boolean canProcessDirectly;
	
	public PublisherEvent() {};
	
	public PublisherEvent(Long yardModuleId, Long craneId, 
			Integer startPosition, Integer endPosition, Boolean canProcessDirectly) {
		this.yardModuleId = yardModuleId;
		this.craneId = craneId;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.canProcessDirectly = canProcessDirectly;
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

	@Override
	public String toString() {
		return "PublisherEvent [yardModuleId=" + yardModuleId + ", craneId=" + craneId + ", startPosition="
				+ startPosition + ", endPosition=" + endPosition + ", canProcessDirectly=" + canProcessDirectly + "]";
	}
	
	
	

}
