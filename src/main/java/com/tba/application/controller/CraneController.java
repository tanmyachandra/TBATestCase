package com.tba.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tba.application.domain.CraneMovementResponse;
import com.tba.application.domain.InstantiateYardModuleResponse;
import com.tba.application.domain.PositionReachedResponse;
import com.tba.application.domain.Response;
import com.tba.application.entities.Crane;
import com.tba.application.helper.CraneManagerHelper;
import com.tba.application.repository.CraneRepository;

@RestController
@RequestMapping(path="/crane-management")
public class CraneController {
	
	@Autowired
	private CraneRepository craneRepository;
	
	@Autowired
	private CraneManagerHelper craneManagerHelper;
	
	/*
	 * 
	 * This end point corresponds to Case 1
	 * 
	 * User can instantiate "numYardModules" number of yard modules
	 * That will also instantiate double the number of cranes and map it to corresponding yard modules
	 * 
	 * User can also specify "numslotPositions" that is the number of slots in yard module
	 * However, this is a non-required parameter
	 * It will fall back to the default number set
	 * 
	 */
	@PostMapping(path="/instantiateModules")
	public @ResponseBody InstantiateYardModuleResponse instantiateYardModules(
			@RequestParam Integer numYardModules,
			@RequestParam(required = false) Integer numslotPositions){
		
		return craneManagerHelper.instantiateYardModules(numYardModules, numslotPositions);
				
	}
	
	@PostMapping(path="/moveCrane")
	public @ResponseBody CraneMovementResponse moveCrane(
			@RequestParam Long craneId,
			@RequestParam(required = false) Integer startPosition,
			@RequestParam Integer endPosition) {
		
		return craneManagerHelper.moveCrane(craneId, startPosition, endPosition);
	}
	
	@PostMapping(path="/parkCrane")
	public @ResponseBody CraneMovementResponse parkCrane(@RequestParam Long craneId) {
		
		return craneManagerHelper.parkCrane(craneId);
	}
	
	@PostMapping(path="/positionReached")
	public @ResponseBody PositionReachedResponse positionReached(@RequestParam Long craneId) {
		
		return craneManagerHelper.positionReached(craneId);
	}

	@PostMapping(path="/add")
	public @ResponseBody String newCrane(@RequestParam String status, 
			@RequestParam Long yardModuleId, 
			@RequestParam Long parkingSlotId) {
		
		Crane c = new Crane();
		c.setStatus("Available");
		//c.setYardModuleId(yardModuleId);
		craneRepository.save(c);
		
		//Iterable<Crane> cI = craneRepository.findByStatus("Available");
		//cI.forEach(i -> System.out.println("Found"));
		
		return "Success";
	
	}
	
	
	
	
}
