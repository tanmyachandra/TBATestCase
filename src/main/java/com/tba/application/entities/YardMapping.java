package com.tba.application.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Setter;

@Entity
public class YardMapping {
	
	@Override
	public String toString() {
		return "YardMapping [yardModuleId=" + yardModuleId + ", craneId=" + craneId + ", craneSlotPosition=" + craneSlotPosition
				+ ", craneParkingSlotPosition=" + craneParkingSlotPosition + "]";
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Setter
	private Long yardModuleId;

	@Setter
	private Long craneId;
	
	@Setter
	private Integer craneSlotPosition;

	@Setter
	private Integer craneParkingSlotPosition;
	
	@Setter
	private String batchId;
	
	public YardMapping() {
		
	}
	
	public YardMapping(Long yardModuleId, Long craneId, 
			Integer craneSlotPosition, Integer craneParkingSlotPosition, String batchId) {
		this.yardModuleId = yardModuleId;
		this.craneId = craneId;
		this.craneSlotPosition = craneSlotPosition;
		this.craneParkingSlotPosition = craneParkingSlotPosition;
		this.batchId = batchId;
	}
	
	public YardMapping(Long craneId) {
		this.craneId = craneId;

	}
	
	public Long getYardModuleId() {
		return yardModuleId;
	}

	public Long getCraneId() {
		return craneId;
	}
	
	public Integer getCraneSlotPosition() {
		return craneSlotPosition;
	}

	public Integer getCraneParkingSlotPosition() {
		return craneParkingSlotPosition;
	}
	
	public void setCraneSlotPosition(Integer craneSlotPosition) {
		this.craneSlotPosition = craneSlotPosition;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public void setYardModuleId(Long yardModuleId) {
		this.yardModuleId = yardModuleId;
	}

	public void setCraneId(Long craneId) {
		this.craneId = craneId;
	}

	public void setCraneParkingSlotPosition(Integer craneParkingSlotPosition) {
		this.craneParkingSlotPosition = craneParkingSlotPosition;
	}


}
