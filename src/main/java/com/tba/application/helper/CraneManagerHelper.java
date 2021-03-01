package com.tba.application.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.tba.application.constants.YardModuleConstants;
import com.tba.application.domain.CraneMovementResponse;
import com.tba.application.domain.InstantiateYardModuleResponse;
import com.tba.application.domain.MoveCraneRequest;
import com.tba.application.domain.PositionReachedResponse;
import com.tba.application.domain.Response;
import com.tba.application.entities.YardMapping;
import com.tba.application.enums.ResponseStatus;
import com.tba.application.service.CraneManagerService;

@Component
public class CraneManagerHelper {
	
	@Autowired
	private CraneManagerService craneManagerService;
	
	public InstantiateYardModuleResponse instantiateYardModules(Integer numYardModules, Integer numslotPositions) {
		
		numslotPositions = (numslotPositions != null && numslotPositions != 0) 
				? numslotPositions
				: YardModuleConstants.DEFAULT_NUM_SLOT_POSITIONS;
		
		String batchId = UUID.randomUUID().toString();
		
		InstantiateYardModuleResponse response = new InstantiateYardModuleResponse();
		response.setBatchId(batchId);
		
		List<Map<String, Object>> yardMappingResponse = craneManagerService.
				instantiateYardModules(numYardModules, numslotPositions+2, batchId); //Adding 2 new slots for parking positions
		
		if (CollectionUtils.isNotEmpty(yardMappingResponse)) {
			response.setMappingData(yardMappingResponse);
			response.setResponseStatus(ResponseStatus.SUCCESS.getStatus());
			response.setResponseMessage("Yard modules instantiated");
		}
		else {
			response.setResponseStatus(ResponseStatus.FAILURE.getStatus());
			response.setResponseMessage("Failed to instantiate yard modules");
		}
		
		return response;
	}
	
	public CraneMovementResponse moveCrane(Long craneId, Integer startPosition, Integer endPosition) {
		
		Map<Integer, Object> processSteps = new HashMap<>();		
		CraneMovementResponse response = new CraneMovementResponse();
				
		MoveCraneRequest request = new MoveCraneRequest(craneId, startPosition, endPosition);
		String responseMessage = craneManagerService.evaluateCraneMovement(request, processSteps);
		
		if (MapUtils.isEmpty(processSteps)) {
			response.setResponseStatus(ResponseStatus.FAILURE.getStatus());
		}
		else {
			response.setResponseStatus(ResponseStatus.SUCCESS.getStatus());
		}
		
		response.setProcessSteps(processSteps);
		response.setResponseMessage(responseMessage);
		return response;
	}
	
	public CraneMovementResponse parkCrane(Long craneId) {
		
		CraneMovementResponse response = new CraneMovementResponse();
		Map<Integer, Object> processSteps = new HashMap<>();
		
		String responseMessage = craneManagerService.parkCrane(craneId, processSteps);
		
		if (MapUtils.isEmpty(processSteps)) {
			response.setResponseStatus(ResponseStatus.FAILURE.getStatus());
		}
		else {
			response.setResponseStatus(ResponseStatus.SUCCESS.getStatus());
		}
		
		response.setProcessSteps(processSteps);
		response.setResponseMessage(responseMessage);
		return response;
		
	}
	
	public PositionReachedResponse positionReached(Long craneId) {

		PositionReachedResponse response = new PositionReachedResponse();
		Map<Integer, Object> processSteps = new HashMap<>();

		YardMapping yardMappingResponse = craneManagerService.positionReached(craneId);

		if (Objects.isNull(yardMappingResponse)) {
			response.setResponseStatus(ResponseStatus.FAILURE.getStatus());
		}
		else {
			response.setResponseStatus(ResponseStatus.SUCCESS.getStatus());
		}

		response.setYardMappingResponse(yardMappingResponse);
		response.setResponseMessage("Processed!");
		return response;

	}

}
