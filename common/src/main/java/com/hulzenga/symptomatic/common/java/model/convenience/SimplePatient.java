package com.hulzenga.symptomatic.common.java.model.convenience;

import com.hulzenga.symptomatic.common.java.model.user.Patient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jouke on 11/30/14.
 */
public class SimplePatient {

  private long id;
  private String firstName;
  private String lastName;
  private String dateOfBirth;
  private String medicalRecordNumber;

  private boolean causeForConcern;

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

  public SimplePatient() {
  }

  public SimplePatient(Patient patient) {
    id = patient.getId();
    firstName = patient.getFirstName();
    lastName = patient.getLastName();
    dateOfBirth = patient.getMedicalRecordNumber();
    medicalRecordNumber = patient.getDateOfBirth();
    causeForConcern = patient.isCauseForConcern();
  }

  public static List<SimplePatient> simplifyPatientList(List<Patient> patients) {
    List<SimplePatient> simplePatients = new ArrayList<SimplePatient>();

    for (Patient p: patients) {
      simplePatients.add(new SimplePatient(p));
    }

    return simplePatients;
  }
}
