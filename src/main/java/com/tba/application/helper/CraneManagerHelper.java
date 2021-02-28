package com.tba.application.helper;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import com.tba.application.constants.YardModuleConstants;
import com.tba.application.domain.InstantiateYardModuleResponse;
import com.tba.application.domain.MoveCraneRequest;
import com.tba.application.domain.Response;
import com.tba.application.enums.ResponseStatus;
import com.tba.application.servive.CraneManagerService;

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
	
	public Response moveCrane(Long craneId, Integer startPosition, Integer endPosition) {
		
		Response response = new Response();
		
		MoveCraneRequest request = new MoveCraneRequest(craneId, startPosition, endPosition);
		String responseMessage = craneManagerService.evaluateCraneMovement(request);
		
		response.setResponseStatus(ResponseStatus.SUCCESS.getStatus());
		response.setResponseMessage(responseMessage);
		return response;
	}
	
	public Response parkCrane(Long craneId) {
		
		Response response = new Response();
		
		String responseMessage = craneManagerService.parkCrane(craneId);
		
		response.setResponseStatus(ResponseStatus.SUCCESS.getStatus());
		response.setResponseMessage(responseMessage);
		return response;
		
	}

}
