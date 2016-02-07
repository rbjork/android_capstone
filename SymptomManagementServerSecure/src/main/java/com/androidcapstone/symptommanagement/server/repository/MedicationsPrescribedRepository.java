package com.androidcapstone.symptommanagement.server.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface MedicationsPrescribedRepository extends CrudRepository<MedicationPrescribed, Long> {
	public Collection<MedicationPrescribed> findByPatientIDAndCurrent(long id, boolean current);
}
