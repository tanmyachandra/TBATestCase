package com.tba.application.servive;

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
		
		//Process - Change yard module mappings
		if (Objects.nonNull(lock)) {
			
			YardMapping yardMappingResponse = yardMappingService.processMovementOfCrane(request, yardMappingDetails);
			if (Objects.isNull(yardMappingResponse)) {
				return APIResponseMessages.MOVEMENT_NOT_PROCESSED;
			}
			
		}
		else {
			System.out.println("Lock could not be acquired, hence exiting");
			return APIResponseMessages.MOVEMENT_NOT_PROCESSED_DUE_TO_LOCK;
		}
		
		//Release lock
		Boolean isLockReleased = yardLockingService.releaseLock(lock);
		if (!isLockReleased) {
			System.out.println("WARNING!! Lock could not be released. Please release manually.");
		}
		
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
			evaluateCraneMovement(request);
		}
		
	}
	
	public String parkCrane(Long craneId) {

		//Get yard module id and current crane position
		YardMapping yardMappingDetails = yardMappingService.getCraneMapping(craneId);
		if (Objects.isNull(yardMappingDetails)) {
			return APIResponseMessages.MISSING_CRANE_MAPPING;
		}

		//Check if crane is already parked
		if(yardMappingDetails.getCraneParkingSlotPosition().equals(yardMappingDetails.getCraneSlotPosition())) {
			return APIResponseMessages.CRANE_ALREADY_PARKED;
		}

		//check for lock
		Boolean isAlreadyLocked = yardLockingService.isYardAlreadyLocked(yardMappingDetails, null);
		System.out.println("evaluateCraneMovement :: isAlreadyLocked: " + isAlreadyLocked);

		MoveCraneRequest moveCraneRequest = new MoveCraneRequest(craneId, 
				yardMappingDetails.getCraneSlotPosition(), yardMappingDetails.getCraneParkingSlotPosition());
		
		//if locked, push to queue
		if (isAlreadyLocked) {
			yardModuleMessagePublisher.addMoveRequestToQueue(moveCraneRequest, yardMappingDetails, false);
			return APIResponseMessages.CRANE_MOVEMENT_QUEUED;
		}
		
		return moveCrane(moveCraneRequest, yardMappingDetails);

	}
	
	public String evaluateCraneMovement(MoveCraneRequest request) {
		
		//Get yard module id and current crane position
		YardMapping yardMappingDetails = yardMappingService.getCraneMapping(request.getCraneId());
		if (Objects.isNull(yardMappingDetails)) {
			return APIResponseMessages.MISSING_CRANE_MAPPING;
		}
		
		//Check if the position is same
		if (yardMappingDetails.getCraneSlotPosition().equals(request.getEndPosition())) {
			return APIResponseMessages.CRANE_ALREADY_AT_DESIRED_POSITION;
		}
		
		
		//check for lock
		Boolean isAlreadyLocked = yardLockingService.isYardAlreadyLocked(yardMappingDetails, request);
		System.out.println("evaluateCraneMovement :: isAlreadyLocked: " + isAlreadyLocked);
		
		//if locked, push to queue
		if (isAlreadyLocked) {
			yardModuleMessagePublisher.addMoveRequestToQueue(request, yardMappingDetails, false);
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
			
			yardModuleMessagePublisher.addMoveRequestToQueue(secondCraneParkingRequest, yardMappingDetailsForSecondCrane, false);
			yardModuleMessagePublisher.addMoveRequestToQueue(request, yardMappingDetails, false);
			return APIResponseMessages.CRANE_MOVEMENT_QUEUED_POST_PARKING;
		}
		
		return moveCrane(request, yardMappingDetails);
				
	}

}
