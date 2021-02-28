package com.tba.application.entities;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Crane {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	public Long getId() {
		return id;
	}

	private String status; //CraneStatus
	private Boolean isActive;
	//private Long yardModuleId;
	//private Integer parkingSlotId; //Parking slot is corresponding to yardModuleId
	private Date creationDate;
	
	public Crane() {
		
	}
	
	public Crane(String status, Boolean isActive, Long yardModuleId, 
			Integer parkingSlotId, Date creationDate) {
		this.status = status;
		this.isActive = isActive;
		//this.yardModuleId = yardModuleId;
		//this.parkingSlotId = parkingSlotId;
		this.creationDate = creationDate;
	}
	
	public Crane(String status, Boolean isActive, Date creationDate) {
		this.status = status;
		this.isActive = isActive;
		this.creationDate = creationDate;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/*public void setYardModuleId(Long yardModuleId) {
		this.yardModuleId = yardModuleId;
	}

	public void setParkingSlotId(Integer parkingSlotId) {
		this.parkingSlotId = parkingSlotId;
	}*/



}
