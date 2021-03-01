package com.tba.application.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.tba.application.entities.YardLock;

/*
 * 
 * Ideally we could use Redis cache for storing this lock info
 * Going with the same mySQL database for now
 * 
 */

public interface YardLockRepository extends JpaRepository<YardLock, Long>{

	@Query(value="select * from yard_lock yl where yl.yard_module_id=:yardModuleId", nativeQuery=true)
    public List<YardLock> findByYardModuleId(Long yardModuleId);
	
	@Query(value="select * from yard_lock yl where yl.crane_id=:craneId", nativeQuery=true)
    public List<YardLock> findUsingCraneId(Long craneId);
	
}
