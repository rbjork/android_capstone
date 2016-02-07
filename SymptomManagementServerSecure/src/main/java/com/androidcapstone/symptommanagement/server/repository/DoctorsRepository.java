package com.androidcapstone.symptommanagement.server.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface DoctorsRepository extends CrudRepository<Doctor, Long> {
	public Collection<Doctor> findByUsername(String name);
}
