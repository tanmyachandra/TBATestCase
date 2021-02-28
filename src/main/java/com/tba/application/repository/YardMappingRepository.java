package com.tba.application.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.tba.application.entities.YardMapping;

public interface YardMappingRepository extends JpaRepository<YardMapping, Long>{

	//@Query("select ym from YardMapping ym where ym.craneId = :craneId")
	@Query(value="select * from yard_mapping ym where ym.crane_id=:craneId", nativeQuery=true)
    public List<YardMapping> findUsingCraneId(Long craneId);
	
	//@Query("from YardMapping ym where ym.yardModuleId=:yardModuleId and ym.craneId!=:craneId")
	@Query(value="select * from yard_mapping ym where ym.crane_id!=:craneId and ym.yard_module_id=:yardModuleId", nativeQuery=true)
    public List<YardMapping> findOtherCrane(Long yardModuleId, Long craneId);
	
	public List<YardMapping> findByCraneIdIn(List<Long> ids);
	
}
