package com.tba.application.service;

/*
 * 
 * This class is used to roll-back entries from database to remove "non-required" data
 * Also helps in maintaining consistency
 * 
 * For example, while instantiating new yard modules, if the mapping of modules and cranes fail,
 * we can delete corresponding new data from yard_module and crane tables
 * as they would not be used any time.
 * 
 * Helps in freeing unwanted space from DB
 * 
 */

import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.tba.application.repository.CraneRepository;
import com.tba.application.repository.YardModuleRepository;

@Component
public class RollbackService {
	
	@Autowired
	private YardModuleRepository yardModuleRepository;
	
	@Autowired
	private CraneRepository craneRepository;
	
	/*
	 * 
	 * This method deletes the entries corresponding to the given ids from yard_module table
	 * Return true if it successfully deletes the rows
	 * Return false otherwise
	 * 
	 */
	public boolean rollbackYardModules(List<Long> yardModuleIds) {
		
		if (CollectionUtils.isEmpty(yardModuleIds)) {
			return false;
		}
		
		try {
			yardModuleRepository.deleteByIdIn(yardModuleIds);
			return true;
		}
		catch(Exception e) {
			System.out.println("Error while roll-back of yard modules");
			return false;
		}
		
	}
	
	/*
	 * 
	 * This method deletes the entries corresponding to the given ids from crane table
	 * Return true if it successfully deletes the rows
	 * Return false otherwise
	 * 
	 */
	public boolean rollbackCranes(List<Long> craneIds) {
		
		if (CollectionUtils.isEmpty(craneIds)) {
			return false;
		}
		
		try {
			craneRepository.deleteByIdIn(craneIds);
			return true;
		}
		catch(Exception e) {
			System.out.println("Error while roll-back of cranes");
			return false;
		}
		
	}

}
