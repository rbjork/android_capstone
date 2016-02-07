package com.androidcapstone.symptommanagement.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface MedicationRepository extends CrudRepository<Medication, Long> {

}
