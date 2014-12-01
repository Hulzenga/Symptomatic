package com.hulzenga.symptomatic.server.repository;

import com.hulzenga.symptomatic.common.java.model.user.Patient;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by jouke on 11/29/14.
 */
public interface PatientRepo extends JpaRepository<Patient, Long>{

  public Patient findByFirstName(String firstName);

  public List<Patient> findByLastName(String lastName);

}
