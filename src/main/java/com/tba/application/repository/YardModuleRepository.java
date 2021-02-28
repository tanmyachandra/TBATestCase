package com.tba.application.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.tba.application.entities.YardModule;

public interface YardModuleRepository extends CrudRepository<YardModule, Integer>{
	
	void deleteByIdIn(List<Long> ids);

}
