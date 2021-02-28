package com.tba.application.servive;

import org.springframework.stereotype.Component;
import com.tba.application.domain.MoveCraneRequest;
import com.tba.application.entities.YardMapping;

@Component
public class UtilService {
	
	/*
	 * 
	 * Method to check if the second crane is in the way for the first crane
	 * Note : first and second cranes are inter-changable
	 * 
	 * Params: YardMapping for crane2 and request for analyzing the start and end positions
	 * 
	 * Returns true if the second crane is in the way
	 * Otherwise false
	 * 
	 */
	public Boolean isSecondCraneInTheWay(MoveCraneRequest request, YardMapping secondCraneMappingDetails) {
		
		Integer positionOfSecondCrane = secondCraneMappingDetails.getCraneSlotPosition();
		if (positionOfSecondCrane >= request.getStartPosition() && positionOfSecondCrane <= request.getEndPosition()) {
			return true;
		}
		
		return false;
		
	}
	
}
