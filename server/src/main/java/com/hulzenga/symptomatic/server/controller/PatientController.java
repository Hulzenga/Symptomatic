package com.hulzenga.symptomatic.server.controller;

import com.google.common.collect.Lists;
import com.hulzenga.symptomatic.common.java.api.PatientApi;
import com.hulzenga.symptomatic.common.java.model.checkin.CheckIn;
import com.hulzenga.symptomatic.common.java.model.checkin.Symptom;
import com.hulzenga.symptomatic.common.java.model.checkin.SymptomState;
import com.hulzenga.symptomatic.common.java.model.convenience.PatientData;
import com.hulzenga.symptomatic.common.java.model.medication.Medication;
import com.hulzenga.symptomatic.server.repository.CheckInRepo;
import com.hulzenga.symptomatic.server.repository.MedicationRepo;
import com.hulzenga.symptomatic.server.repository.SymptomRepo;
import com.hulzenga.symptomatic.server.repository.SymptomStateRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jouke on 11/4/14.
 */
@RestController
public class PatientController implements PatientApi{

  @Autowired
  private SymptomRepo symptomRepo;

  @Autowired
  private SymptomStateRepo symptomStateRepo;

  @Autowired
  private CheckInRepo checkInRepo;

  @Autowired
  private MedicationRepo medicationRepo;

  @Override
  @RequestMapping(value = PATIENT_CHECK_IN_PATH, method = RequestMethod.POST)
  public CheckIn checkIn(@RequestBody CheckIn checkIn) {
    return checkInRepo.save(checkIn);
  }

  @Override
  @RequestMapping(value = PATIENT_SYMPTOMS_PATH, method = RequestMethod.GET)
  public List<Symptom> getSymptoms() {
    return symptomRepo.findAll();
  }

  @Override
  @RequestMapping(value = PATIENT_MEDICATIONS_PATH, method = RequestMethod.GET)
  public List<Medication> getMedications() {
    return medicationRepo.findAll();
  }

  @Override
  @RequestMapping(value = PATIENT_DATA_PATH, method = RequestMethod.GET)
  public PatientData getPatientData() {
    return new PatientData(getSymptoms(), getMedications());
  }
}
