package com.hulzenga.symptomatic.server.repository;

import com.hulzenga.symptomatic.common.java.model.medication.Medication;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jouke on 11/26/14.
 */
public interface MedicationRepo extends JpaRepository<Medication, Long> {

}
