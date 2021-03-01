package com.tba.application.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.tba.application.entities.Crane;
import com.tba.application.repository.CraneRepository;

@Component
public class CraneService {

	@Autowired
	private CraneRepository craneRepository;

	/*
	 * This method creates crane instances
	 * 
	 * yardModuleIds are the list of yard
	 * 
	 * Persists the 
	 */
	public List<Long> createCraneInstances(Integer numYardModules) {

		List<Long> craneIds = new ArrayList<>();

		if (numYardModules <= 0) {
			System.out.println("No yard module id defined to create crane instances");
			return craneIds;
		}

		try {

			List<Crane> craneRequest = new ArrayList<>();
			//Creating 2 times number of cranes
			for(int i = 0; i < numYardModules * 2; i++) {
				craneRequest.add(new Crane("Available", true, new Date()));
			}

			//Persist yard module to DB
			Iterable<Crane> craneResponse = craneRepository.saveAll(craneRequest);

			extractCraneIds(craneIds, craneResponse);


		}
		catch(Exception e) {
			System.out.println("Error while persisting cranes");
		}


		return craneIds;
	}

	private void extractCraneIds(List<Long> craneIds, Iterable<Crane> craneResponse) {

		//Check for empty response
		//if (CollectionUtils.isEmpty(yardModuleResponse)) {}

		craneResponse
			.forEach(c -> craneIds.add(c.getId()));

	}
}
