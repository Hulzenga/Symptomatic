package com.hulzenga.symptomatic.server.repository;

import com.hulzenga.symptomatic.common.java.model.checkin.Symptom;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jouke on 11/6/14.
 */
public interface SymptomRepo extends JpaRepository<Symptom, Long> {


}
