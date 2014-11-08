package com.hulzenga.symptomatic.server.repository;

import com.hulzenga.symptomatic.model.checkin.SymptomState;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jouke on 11/7/14.
 */
public interface SymptomStateRepo extends JpaRepository<SymptomState, Long>{
}
