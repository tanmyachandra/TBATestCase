package com.tba.application.servive;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.tba.application.domain.MoveCraneRequest;
import com.tba.application.entities.YardLock;
import com.tba.application.entities.YardMapping;
import com.tba.application.repository.YardLockRepository;

/*
 * 
 * This service is used to manage a custom lock implementation
 * 
 * For example, if there is a crane that is working on a yard module from position 2 to position 10
 * We would like to lock the particular yard and the crane between only these two positions
 * 
 * If there is an another crane that wants to access the same yard from any position (other than 2-10)
 * It will be able to do so
 * However, it will not be able to use the section that is already locked.
 * It will wait for the previous lock to be release (in this case, deleted from database)
 * Then it can use the positions previously locked
 * 
 */

@Component
public class YardLockingService {

	@Autowired
	private YardLockRepository yardLockRepository;
	
	public YardLock acquireYardLock(YardMapping yardMappingDetails, MoveCraneRequest request) {
		
		YardLock lock = new YardLock(yardMappingDetails.getYardModuleId(), request.getCraneId(), 
				request.getStartPosition(), request.getEndPosition());
		
		try {
			YardLock lockResponse = yardLockRepository.save(lock);
			if (Objects.nonNull(lockResponse)) {
				System.out.println("Lock for yard module acquired");
			}
		}
		catch(Exception e) {
			System.out.println("Error while acquiring lock for the yard module");
		}
		
		return lock;
		
	}
	
	public boolean releaseLock(YardLock lock) {
		
		try {
			yardLockRepository.delete(lock);
			return true;
		}
		catch(Exception e) {
			System.out.println("Error while acquiring lock for the yard module");
		}
		
		return false;
		
	}

	public boolean isYardAlreadyLocked(YardMapping yardMappingDetails, MoveCraneRequest request) {
		
		System.out.println("isYardAlreadyLocked :: yardMappingDetails: " + yardMappingDetails);

		List<YardLock> yardLockDetailsForYard = new ArrayList<>();

		try {
			yardLockDetailsForYard = yardLockRepository
					.findByYardModuleId(yardMappingDetails.getYardModuleId());
			
			if (CollectionUtils.isEmpty(yardLockDetailsForYard)) {
				return false;
			}
			
		}
		catch(Exception e) {
			System.out.println("Error while fetching lock data for the yard module");
		}
		
		if (Objects.isNull(request)) {
			
			//Checking only for lock on the crane
			for(YardLock lockEntity : yardLockDetailsForYard) {
				System.out.println("lockEntity :: " + lockEntity);
				if (lockEntity.getCraneId().equals(yardMappingDetails.getCraneId())){
					return true;
				}
			}
		}
		else {
			
			//Checking for lock on both crane and position range
			for(YardLock lockEntity : yardLockDetailsForYard) {
				System.out.println("lockEntity :: " + lockEntity);
				if (lockEntity.getCraneId().equals(yardMappingDetails.getCraneId()) 
						|| (request.getStartPosition() >= lockEntity.getStartPosition() && request.getStartPosition() <= lockEntity.getEndPosition())
						|| (request.getEndPosition() >= lockEntity.getStartPosition() && request.getEndPosition() >= lockEntity.getEndPosition())){
					return true;
				}
			}
		}

		

		return false;

	}
	
	

}
