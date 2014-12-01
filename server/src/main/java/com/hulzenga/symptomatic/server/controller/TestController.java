package com.hulzenga.symptomatic.server.controller;

import com.hulzenga.symptomatic.common.java.model.checkin.CheckIn;
import com.hulzenga.symptomatic.common.java.model.checkin.Symptom;
import com.hulzenga.symptomatic.common.java.model.checkin.SymptomState;
import com.hulzenga.symptomatic.common.java.model.convenience.SimplePatient;
import com.hulzenga.symptomatic.common.java.model.medication.Medication;
import com.hulzenga.symptomatic.common.java.model.user.Doctor;
import com.hulzenga.symptomatic.common.java.model.user.Patient;
import com.hulzenga.symptomatic.common.java.network.ServerSettings;
import com.hulzenga.symptomatic.server.repository.CheckInRepo;
import com.hulzenga.symptomatic.server.repository.DoctorRepo;
import com.hulzenga.symptomatic.server.repository.MedicationRepo;
import com.hulzenga.symptomatic.server.repository.PatientRepo;
import com.hulzenga.symptomatic.server.repository.SymptomRepo;
import com.hulzenga.symptomatic.server.repository.SymptomStateRepo;
import com.hulzenga.symptomatic.server.security.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jouke on 11/28/14.
 */
@RestController
public class TestController {


  @Autowired
  private SymptomRepo symptomRepo;

  @Autowired
  private SymptomStateRepo symptomStateRepo;

  @Autowired
  private CheckInRepo checkInRepo;

  @Autowired
  private MedicationRepo medicationRepo;

  @Autowired
  private PatientRepo patientRepo;

  @Autowired
  private DoctorRepo doctorRepo;

  @RequestMapping(value = "/test/clear", method = RequestMethod.GET)
  public String clear() {
    checkInRepo.deleteAll();
    symptomRepo.deleteAll();
    symptomStateRepo.deleteAll();
    medicationRepo.deleteAll();
    patientRepo.deleteAll();
    doctorRepo.deleteAll();
    return "clear";
  }



  @RequestMapping(value = "/test/patients", method = RequestMethod.GET)
  public List<Patient> getPatients() {
    return patientRepo.findAll();
  }

  @RequestMapping(value = "/test/simplepatients", method = RequestMethod.GET)
  public List<SimplePatient> getSimplePatients() {
    return SimplePatient.simplifyPatientList(patientRepo.findAll());
  }

  @RequestMapping(value = "/test/doctors", method = RequestMethod.GET)
  public List<Doctor> getDoctors() {
    return doctorRepo.findAll();
  }

  @RequestMapping(value = "/test/checkins", method = RequestMethod.GET)
  public List<CheckIn> getCheckins() {
    return checkInRepo.findAll();
  }

  @RequestMapping(value = "/test/medication", method = RequestMethod.GET)
  public List<Medication> getMedications() {
    return medicationRepo.findAll();
  }

  @RequestMapping(value = "/test/symptomstates", method = RequestMethod.GET)
  public List<SymptomState> getSymptomStates() {
    return symptomStateRepo.findAll();
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String helloWorld() {

    return "Hello Symptomatic World !";
  }
}
