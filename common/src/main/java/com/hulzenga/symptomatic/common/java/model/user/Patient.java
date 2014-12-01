package com.hulzenga.symptomatic.common.java.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hulzenga.symptomatic.common.java.model.checkin.CheckIn;
import com.hulzenga.symptomatic.common.java.model.checkin.Symptom;
import com.hulzenga.symptomatic.common.java.model.checkin.SymptomState;
import com.hulzenga.symptomatic.common.java.model.convenience.PatientData;
import com.hulzenga.symptomatic.common.java.model.medication.Medication;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 * Created by jouke on 11/4/14.
 */
@Entity
public class Patient {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private String firstName;
  private String lastName;
  private String dateOfBirth;
  private String medicalRecordNumber;

  /**
   * true if the patients symptoms pose an issue
   */
  private boolean causeForConcern;

  @ManyToMany
  private List<SymptomState> concerningSymptoms;

  @OneToMany
  private List<CheckIn> checkIns;

  @ManyToMany
  private List<Symptom> symptoms;

  @ManyToMany
  private List<Medication> medications;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getMedicalRecordNumber() {
    return medicalRecordNumber;
  }

  public void setMedicalRecordNumber(String medicalRecordNumber) {
    this.medicalRecordNumber = medicalRecordNumber;
  }

  public boolean isCauseForConcern() {
    return causeForConcern;
  }

  public void setCauseForConcern(boolean causeForConcern) {
    this.causeForConcern = causeForConcern;
  }

  public List<SymptomState> getConcerningSymptoms() {
    return concerningSymptoms;
  }

  public void setConcerningSymptoms(List<SymptomState> concerningSymptoms) {
    this.concerningSymptoms = concerningSymptoms;
  }

  public List<CheckIn> getCheckIns() {
    return checkIns;
  }

  public void setCheckIns(List<CheckIn> checkIns) {
    this.checkIns = checkIns;
  }

  public List<Symptom> getSymptoms() {
    return symptoms;
  }

  public void setSymptoms(List<Symptom> symptoms) {
    this.symptoms = symptoms;
  }

  public List<Medication> getMedications() {
    return medications;
  }

  public void setMedications(List<Medication> medications) {
    this.medications = medications;
  }

  public Patient() {
  }

  public Patient(String firstName, String lastName, String dateOfBirth, String medicalRecordNumber, List<CheckIn> checkIns, List<Symptom> symptoms, List<Medication> medications) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.dateOfBirth = dateOfBirth;
    this.medicalRecordNumber = medicalRecordNumber;
    this.causeForConcern = false; //default
    this.concerningSymptoms = new ArrayList<SymptomState>();
    this.checkIns = checkIns;
    this.symptoms = symptoms;
    this.medications = medications;
  }

  @JsonIgnore
  public PatientData getPatientData() {
    return new PatientData(symptoms, medications);
  }
}
