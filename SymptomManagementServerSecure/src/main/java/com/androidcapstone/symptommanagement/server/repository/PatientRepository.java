package com.androidcapstone.symptommanagement.server.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface PatientRepository extends CrudRepository<Patient, Long> {
	public Collection<Patient> findByLastName(String name);
	public Collection<Patient> findByDoctor(Doctor doctor);
	public Collection<Patient> findByUsername(String username);
}
