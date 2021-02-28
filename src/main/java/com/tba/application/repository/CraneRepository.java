package com.tba.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.tba.application.entities.Crane;

public interface CraneRepository extends CrudRepository<Crane, Integer> {
	
	@Query("from Crane c where c.status=:status")
    public Iterable<Crane> findByStatus(String status);
	
	void deleteByIdIn(List<Long> ids);

}