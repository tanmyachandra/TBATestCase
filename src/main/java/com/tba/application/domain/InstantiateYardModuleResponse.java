package com.tba.application.domain;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class InstantiateYardModuleResponse extends Response {
	
	private List<Map<String, Object>> mappingData;
	private String batchId;

	public void setMappingData(List<Map<String, Object>> mappingData) {
		this.mappingData = mappingData;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	
}
