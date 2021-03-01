package com.tba.application.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tba.application.constants.APIResponseMessages;
import com.tba.application.domain.MoveCraneRequest;
import com.tba.application.entities.YardLock;
import com.tba.application.entities.YardMapping;
import com.tba.application.publisher.YardModuleMessagePublisher;

@Component
public class CraneManagerService {
	
	@Autowired
	private UtilService utilService;
	
	@Autowired
	private YardService yardService;
	
	@Autowired
	private CraneService craneService;
	
	@Autowired
	private YardMappingService yardMappingService;
	
	@Autowired
	private YardLockingService yardLockingService;
	
	@Autowired
	private YardModuleMessagePublisher yardModuleMessagePublisher;
	
	public List<Map<String, Object>> instantiateYardModules(Integer numYardModules, Integer numslotPositions, String batchId){
		
		//Create yard modules
		List<Long> yardModuleIds = yardService.createYardInstances(numYardModules, numslotPositions);
		System.out.println("yardModuleIds: " + yardModuleIds);
		
		//Create cranes
		List<Long> craneIds = craneService.createCraneInstances(numYardModules);
		System.out.println("craneIds: " + craneIds);
		
		//Map cranes to yard
		List<Map<String, Object>> yardMappingResponse = yardMappingService.establishInstantiationMapping(numslotPositions, 
				yardModuleIds, craneIds, batchId);
		
		if (CollectionUtils.isEmpty(yardMappingResponse)) {
			System.out.println("Mapping could not be established!!");
			return null;
		}
		
		return yardMappingResponse;
		
	}
	
	public String moveCrane(MoveCraneRequest request, YardMapping yardMappingDetails) {
		
		//Acquire lock
		YardLock lock = yardLockingService.acquireYardLock(yardMappingDetails, request);
		
		//Change yard module mappings and Release lock would happen as part of another call
		//that is made when the crane reaches its desired position
		
		return APIResponseMessages.MOVEMENT_PROCESSED;
	}
	
	public void evaluateCraneMovementFromEvent(MoveCraneRequest request, Boolean canProcessDirectly) {
		
		System.out.println("evaluateCraneMovementFromEvent :: From event : " + request);
		
		if (canProcessDirectly) {
			
			//Get yard module id and current crane position
			YardMapping yardMappingDetails = yardMappingService.getCraneMapping(request.getCraneId());
			moveCrane(request, yardMappingDetails);
			
		}
		else {
			
			//Evaluate the crane movement once again and proceed
			System.out.println("Re-evaluate! ");
			Map<Integer, Object> processSteps = new HashMap<>();
			evaluateCraneMovement(request, processSteps);
		}
		
	}
	
	/*
	 * 
	 * 
	 * Don't need to check if the other crane is in the way 
	 * As it will park always on respective sides and cranes cannot cross each other
	 */
	public String parkCrane(Long craneId, Map<Integer, Object> processSteps) {

		//Get yard module id and current crane position
		YardMapping yardMappingDetails = yardMappingService.getCraneMapping(craneId);
		if (Objects.isNull(yardMappingDetails)) {
			return APIResponseMessages.MISSING_CRANE_MAPPING;
		}

		//Check if crane is already parked
		if(yardMappingDetails.getCraneParkingSlotPosition().equals(yardMappingDetails.getCraneSlotPosition())) {
			processSteps.put(1, "No Movement");
			return APIResponseMessages.CRANE_ALREADY_PARKED;
		}

		//check for lock
		Boolean isAlreadyLocked = yardLockingService.isYardAlreadyLocked(yardMappingDetails, null);
		System.out.println("evaluateCraneMovement :: isAlreadyLocked: " + isAlreadyLocked);

		MoveCraneRequest moveCraneRequest = new MoveCraneRequest(craneId, 
				yardMappingDetails.getCraneSlotPosition(), yardMappingDetails.getCraneParkingSlotPosition());
		
		//if locked, push to queue
		if (isAlreadyLocked) {
			yardModuleMessagePublisher.addMoveRequestToQueue(moveCraneRequest, yardMappingDetails, false, true);
			processSteps.put(1, "Wait for other process to finish");
			processSteps.put(2, "Process this crane");
			return APIResponseMessages.CRANE_MOVEMENT_QUEUED;
		}
		
		//Request to park this crane
		yardModuleMessagePublisher.addMoveRequestToQueue(moveCraneRequest, yardMappingDetails, true, true);
		processSteps.put(1, "Process this crane");
		return APIResponseMessages.MOVEMENT_PROCESSED;

	}
	
	public String evaluateCraneMovement(MoveCraneRequest request, Map<Integer, Object> processSteps) {
		
		//Get yard module id and current crane position
		YardMapping yardMappingDetails = yardMappingService.getCraneMapping(request.getCraneId());
		if (Objects.isNull(yardMappingDetails)) {
			return APIResponseMessages.MISSING_CRANE_MAPPING;
		}
		
		//Check if the position is same
		if (yardMappingDetails.getCraneSlotPosition().equals(request.getEndPosition())) {
			processSteps.put(1, "No Movement");
			return APIResponseMessages.CRANE_ALREADY_AT_DESIRED_POSITION;
		}
		
		//Set the startPosition from the current mapping
		request.setStartPosition(yardMappingDetails.getCraneSlotPosition());
		
		
		//check for lock
		Boolean isAlreadyLocked = yardLockingService.isYardAlreadyLocked(yardMappingDetails, request);
		System.out.println("evaluateCraneMovement :: isAlreadyLocked: " + isAlreadyLocked);
		
		//if locked, push to queue
		if (isAlreadyLocked) {
			yardModuleMessagePublisher.addMoveRequestToQueue(request, yardMappingDetails, false, false);
			processSteps.put(1, "Wait for other process to finish");
			processSteps.put(2, "Process this crane");
			return APIResponseMessages.CRANE_MOVEMENT_QUEUED;
		}
		
		//Check position of other crane and if it is blocking
		YardMapping yardMappingDetailsForSecondCrane = yardMappingService.getCraneMappingForOtherCrane(yardMappingDetails);
		System.out.println("evaluateCraneMovement :: yardMappingDetailsForSecondCrane: " + yardMappingDetailsForSecondCrane);
		if (Objects.isNull(yardMappingDetailsForSecondCrane)) {
			return APIResponseMessages.MISSING_SECONDARY_CRANE_MAPPING;
		}
		
		Boolean isBlockedByOtherCrane = utilService.isSecondCraneInTheWay(request, yardMappingDetailsForSecondCrane);
		System.out.println("evaluateCraneMovement :: isBlockedByOtherCrane: " + isBlockedByOtherCrane);
		
		//If blocked, park the second crane
		if (isBlockedByOtherCrane) {
			MoveCraneRequest secondCraneParkingRequest = new MoveCraneRequest(
					yardMappingDetailsForSecondCrane.getCraneId(), 
					yardMappingDetailsForSecondCrane.getCraneSlotPosition(),
					yardMappingDetailsForSecondCrane.getCraneParkingSlotPosition());
			
			yardModuleMessagePublisher.addMoveRequestToQueue(secondCraneParkingRequest, yardMappingDetailsForSecondCrane, false, true);
			yardModuleMessagePublisher.addMoveRequestToQueue(request, yardMappingDetails, false, false);
			processSteps.put(1, "Park other crane");
			processSteps.put(2, "Process this crane");
			return APIResponseMessages.CRANE_MOVEMENT_QUEUED_POST_PARKING;
		}
		
		//Request to move this crane
		yardModuleMessagePublisher.addMoveRequestToQueue(request, yardMappingDetails, true, false);
		processSteps.put(1, "Process this crane");
		return APIResponseMessages.MOVEMENT_PROCESSED;
				
	}
	
	/*
	 * Service to process information when the crane reaches its desired stop
	 * 
	 */
	public YardMapping positionReached(Long craneId) {

		YardLock yardLockForCrane = yardLockingService.getLock(craneId);
		if (Objects.isNull(yardLockForCrane)) {
			System.out.println("Yard lock response could not be fetched");
			return null;
		}

		//Get yard module id and current crane position
		YardMapping yardMappingDetails = yardMappingService.getCraneMapping(craneId);
		if (Objects.isNull(yardMappingDetails)) {
			System.out.println("Yard mapping response could not be fetched");
			return null;
		}

		MoveCraneRequest request = new MoveCraneRequest(craneId, yardLockForCrane.getEndPosition());
		YardMapping yardMappingResponse = yardMappingService.processMovementOfCrane(request, yardMappingDetails);
		if (Objects.isNull(yardMappingResponse)) {
			System.out.println("Process movement could not be completed");
			return null;
		}

		//Release lock
		Boolean isLockReleased = yardLockingService.releaseLock(yardLockForCrane);
		if (!isLockReleased) {
			System.out.println("WARNING!! Lock could not be released. Please release manually.");
		}

		return yardMappingResponse;

	}

}
