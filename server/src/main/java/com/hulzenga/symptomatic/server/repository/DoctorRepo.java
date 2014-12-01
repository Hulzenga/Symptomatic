package com.hulzenga.symptomatic.server.repository;

import com.hulzenga.symptomatic.common.java.model.user.Doctor;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jouke on 11/30/14.
 */
public interface DoctorRepo extends JpaRepository<Doctor, Long>{

  public Doctor findByFirstName(String firstName);


}
