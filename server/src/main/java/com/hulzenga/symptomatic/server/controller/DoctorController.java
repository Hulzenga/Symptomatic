package com.hulzenga.symptomatic.server.controller;

import com.hulzenga.symptomatic.common.java.api.DoctorApi;
import com.hulzenga.symptomatic.common.java.model.checkin.Symptom;
import com.hulzenga.symptomatic.common.java.model.convenience.SimplePatient;
import com.hulzenga.symptomatic.common.java.model.medication.Medication;
import com.hulzenga.symptomatic.common.java.model.user.Doctor;
import com.hulzenga.symptomatic.common.java.model.user.Patient;
import com.hulzenga.symptomatic.server.repository.CheckInRepo;
import com.hulzenga.symptomatic.server.repository.DoctorRepo;
import com.hulzenga.symptomatic.server.repository.MedicationRepo;
import com.hulzenga.symptomatic.server.repository.PatientRepo;
import com.hulzenga.symptomatic.server.repository.SymptomRepo;
import com.hulzenga.symptomatic.server.repository.SymptomStateRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import retrofit.http.Body;

/**
 * Created by jouke on 11/5/14.
 */
@RestController
public class DoctorController implements DoctorApi {

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

  /**
   * Assumes username == doctors first name and finds the first doctor that matches
   *
   * @return the currently authenticated patient
   */
  private Doctor getDoctor() {
    try {
      return doctorRepo.findByFirstName(SecurityContextHolder.getContext().getAuthentication().getName());
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  @RequestMapping(value = DOCTOR_PATIENT, method = RequestMethod.GET)
  public List<SimplePatient> getPatients() {
    try {
      return SimplePatient.simplifyPatientList(getDoctor().getPatients());
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  @RequestMapping(value = DOCTOR_PATIENT + "/lastname/{lastname}", method = RequestMethod.GET)
  public List<SimplePatient> getNamedPatients(@PathVariable String lastname) {
    return SimplePatient.simplifyPatientList(patientRepo.findByLastName(lastname));
  }

  @Override
  @RequestMapping(value = DOCTOR_PATIENT + "/{id}", method = RequestMethod.GET)
  public Patient getPatient(@PathVariable long id) {
    List<Patient> doctorPatients = getDoctor().getPatients();
    for (Patient p : doctorPatients) {
      if (p.getId() == id) {
        return p; //found the patient
      }
    }
    return null; //no patient with that id is being treated by the doctor
  }

  @Override
  @RequestMapping(value = DOCTOR_MEDICATION, method = RequestMethod.GET)
  public List<Medication> getMedications() {
    return medicationRepo.findAll();
  }

  @Override
  @RequestMapping(value = DOCTOR_SYMPTOMS, method = RequestMethod.GET)
  public List<Symptom> getSymptoms() {
    return symptomRepo.findAll();
  }

  @Override
  @RequestMapping(value = DOCTOR_PATIENT + "/{id}/medication", method = RequestMethod.POST)
  public List<Medication> setPatientMedication(@PathVariable long id, @RequestBody List<Medication> medications) {
    Patient patient = getPatient(id);

    if (patient != null) {
      patient.setMedications(medications);
      patientRepo.save(patient);
      return medications;
    } else {
      return null;
    }
  }
}
