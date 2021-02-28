package com.tba.application.servive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.tba.application.constants.YardModuleConstants;
import com.tba.application.domain.MoveCraneRequest;
import com.tba.application.entities.YardMapping;
import com.tba.application.repository.YardMappingRepository;

@Component
public class YardMappingService {
	
	@Autowired
	private YardMappingRepository yardMappingRepository;
	
	public List<Map<String, Object>> establishInstantiationMapping(Integer numSlotPositions, 
			List<Long> yardModuleIds, List<Long> craneIds, String batchId){
		
		System.out.println("establishInstantiationMapping:: yardModuleIds: " + yardModuleIds
				+ " craneIds: " + craneIds + " numSlotPositions: " + numSlotPositions);
		
		Map<Long, Object> yardMapping = new HashMap<>();
		List<Map<String, Object>> response  = new ArrayList<>();
		int numYardsInstantiated = yardModuleIds.size();
		
		if (numSlotPositions <= 0 || 2*yardModuleIds.size() != craneIds.size()) {
			System.out.println("Incorrect mapping inputs!!!");
			return response;
		}
				
		try {
			List<YardMapping> yardMappingRequest = new ArrayList<>();
			for(int i = 0; i < numYardsInstantiated; i++) {
				
				//Assiging same values for craneSlotPosition and craneParkingSlotPosition 
				//assuming that the the crane is parked after instantiation
				yardMappingRequest.add(new YardMapping(yardModuleIds.get(i), craneIds.get(i), 0, 0, batchId));
				yardMappingRequest.add(new YardMapping(yardModuleIds.get(i), craneIds.get((numYardsInstantiated-1)*2-i+1), 
						numSlotPositions-1, numSlotPositions-1, batchId));
			}
			
			System.out.println("yardMappingRequest:: " + yardMappingRequest);
			Iterable<YardMapping> yardMappingResponse = yardMappingRepository.saveAll(yardMappingRequest);
			
			yardMappingResponse.forEach(y -> System.out.println("XXXX " + y));
			
			response = extractMappingResponse(yardMapping, yardMappingResponse);
			
		}
		catch(Exception e) {
			System.out.println("Error while establishing yard mapping");
		}
		
		
		return response;
		
	}
	
	private List<Map<String, Object>> extractMappingResponse(Map<Long, Object> yardMapping, Iterable<YardMapping> yardMappingResponse) {
		
		yardMappingResponse.forEach(y -> {
			Long yardModuleId = y.getYardModuleId();
			
			if (yardMapping.containsKey(yardModuleId)) {
				List<Long> craneList = (List<Long>) yardMapping.get(yardModuleId);
				craneList.add(y.getCraneId());
			}
			else {
				List<Long> craneList = new ArrayList<>();
				craneList.add(y.getCraneId());
				yardMapping.put(yardModuleId, craneList);
			}
		});
		
		if (MapUtils.isEmpty(yardMapping)) {
			return null;
		}
		
		List<Map<String, Object>> response = new ArrayList<>();
		for(Long y : yardMapping.keySet()) {
			Map<String, Object> yardResponse = new HashMap<>();
			yardResponse.put(YardModuleConstants.YARD_MODULE_ID, y);
			yardResponse.put(YardModuleConstants.YARD_MODULE_CRANES, yardMapping.get(y));
			response.add(yardResponse);
		}
		
		return response;
		
	}
	
	public YardMapping getCraneMapping(Long craneId) {
		
		System.out.println("craneId: " + craneId);
				
		YardMapping craneMappingDetails = null;
		try {
			List<Long> ids = new ArrayList<>();
			ids.add(craneId);
			List<YardMapping> craneMappingDetailsList = yardMappingRepository.findUsingCraneId(craneId);
			System.out.println("craneMappingDetailsList: "+ craneMappingDetailsList);
			
			if (CollectionUtils.isEmpty(craneMappingDetailsList)) {
				return null;
			}

			for (YardMapping y : craneMappingDetailsList) {
				craneMappingDetails = y;
				break;
			}
		}
		catch(Exception e) {
			System.out.println("Error while fetching crane mapping details!!");
		}
		 
		System.out.println("getCraneMapping :: craneId: " + craneId + " craneMappingDetails: " + craneMappingDetails);
		
		return craneMappingDetails;
		
	}
	
	/*
	 * Method to find information corresponding to the other crane
	 * Given that we have details for the first crane
	 * 
	 */
	public YardMapping getCraneMappingForOtherCrane(YardMapping firstCraneMappingDetails) {
		
		YardMapping secondCraneMappingDetails = null;
		try {
			List<YardMapping> secondCraneMappingDetailsList = yardMappingRepository
					.findOtherCrane(firstCraneMappingDetails.getYardModuleId(), firstCraneMappingDetails.getCraneId());
			
			if (CollectionUtils.isEmpty(secondCraneMappingDetailsList)) {
				return null;
			}
			
			for (YardMapping y : secondCraneMappingDetailsList) {
				secondCraneMappingDetails = y;
				break;
			}
		}
		catch(Exception e) {
			System.out.println("Error while fetching crane mapping details for the second crane!!");
		}
		 
		System.out.println("getCraneMappingForOtherCrane :: firstCraneMappingDetails: " + firstCraneMappingDetails 
				+ " --> secondCraneMappingDetails: " + secondCraneMappingDetails);
		
		return secondCraneMappingDetails;
	}
	
	/*
	 * 
	 * This method sets the new slotPosition according to the end position given in the request
	 * Params : request object and yard mapping details of the crane that is being moved
	 * 
	 * Returns new yard mapping after change is made i.e. movement of crane is completed
	 * 
	 */
	public YardMapping processMovementOfCrane(MoveCraneRequest request, YardMapping yardMappingDetails) {
		
		//Setting the new position from the request
		yardMappingDetails.setCraneSlotPosition(request.getEndPosition());
		
		try {
			YardMapping yardMappingResponse = yardMappingRepository.save(yardMappingDetails);
			return yardMappingResponse;
		}
		catch(Exception e) {
			System.out.println("Error while setting new position");
		}
		
		return null;
		
	}
	
	

}
