package com.androidcapstone.symptommanagement.server.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CheckinRepository extends CrudRepository<Checkin,Long> {
	public Collection<Checkin> findByPatientID(long id);
	public Collection<Checkin> findByPainLevelOrderByCheckinTimeDesc(String level);
	public Collection<Checkin> findByCanEatDrinkOrderByCheckinTimeDesc(boolean can);
}
