package com.androidcapstone.symptommanagement.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface MedicationTakenRepository extends CrudRepository<MedicationTaken, Long> {

}
