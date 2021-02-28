package com.tba.application.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Setter;

@Entity
public class YardModule {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	public Long getId() {
		return id;
	}

	@Setter
	private Integer numModuleSlots;
	
	@Setter
	private Boolean isInstantiated;
	
	@Setter
	private Date creationDate;
	
	public YardModule(Integer numModuleSlots, Boolean isInstantiated, Date creationDate) {
		this.numModuleSlots = numModuleSlots;
		this.isInstantiated = isInstantiated;
		this.creationDate = creationDate;
	}
	
}
