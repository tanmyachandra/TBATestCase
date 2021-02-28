package com.tba.application.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class YardLock {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private Long yardModuleId;

	private Long craneId;
	
	private Integer startPosition;
	
	private Integer endPosition;
	
	public YardLock() {};
	
	public YardLock(Long yardModuleId, Long craneId, 
			Integer startPosition, Integer endPosition) {
		this.yardModuleId = yardModuleId;
		this.craneId = craneId;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
	}
	
	public Long getId() {
		return id;
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

	public void setId(Long id) {
		this.id = id;
	}

	public void setYardModuleId(Long yardModuleId) {
		this.yardModuleId = yardModuleId;
	}

	public void setCraneId(Long craneId) {
		this.craneId = craneId;
	}

	public void setStartPosition(Integer startPosition) {
		this.startPosition = startPosition;
	}

	public void setEndPosition(Integer endPosition) {
		this.endPosition = endPosition;
	}

	@Override
	public String toString() {
		return "YardLock [id=" + id + ", yardModuleId=" + yardModuleId + ", craneId=" + craneId + ", startPosition="
				+ startPosition + ", endPosition=" + endPosition + "]";
	}
	
	

}
