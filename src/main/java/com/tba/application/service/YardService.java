package com.tba.application.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.tba.application.entities.YardModule;
import com.tba.application.repository.YardModuleRepository;

@Component
public class YardService {
	
	@Autowired
	private YardModuleRepository yardModuleRepository;

	public List<Long> createYardInstances(Integer numYardModules, Integer numModuleSlots){
		
		List<Long> yardModuleIds = new ArrayList<>();
		
		//Checking if the request is valid
		if (numYardModules <= 0) {
			return yardModuleIds;
		}
		
		try {
			
			List<YardModule> yardModuleRequest = new ArrayList<>();
			for(int i = 0; i < numYardModules; i++) {
				yardModuleRequest.add(new YardModule(numModuleSlots, true, new Date()));
			}
			
			//Persist yard module to DB
			Iterable<YardModule> yardModuleResponse = yardModuleRepository.saveAll(yardModuleRequest);
			
			extractYardModuleIds(yardModuleIds, yardModuleResponse);
			
			
		}
		catch(Exception e) {
			System.out.println("Error while persisting yard modules");
		}
		
		
		return yardModuleIds;
		
	}
	
	private void extractYardModuleIds(List<Long> yardModuleIds, Iterable<YardModule> yardModuleResponse) {
		
		//Check for empty response
		//if (CollectionUtils.isEmpty(yardModuleResponse)) {}
		
		yardModuleResponse
			.forEach(y -> yardModuleIds.add(y.getId()));
		
	}
	
}
