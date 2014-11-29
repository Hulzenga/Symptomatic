package com.hulzenga.symptomatic.server.repository;

import com.hulzenga.symptomatic.common.java.model.checkin.CheckIn;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jouke on 11/8/14.
 */
public interface CheckInRepo extends JpaRepository<CheckIn, Long>{
}
