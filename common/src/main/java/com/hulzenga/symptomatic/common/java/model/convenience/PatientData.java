package com.hulzenga.symptomatic.common.java.model.convenience;

import com.hulzenga.symptomatic.common.java.model.checkin.Symptom;
import com.hulzenga.symptomatic.common.java.model.medication.Medication;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jouke on 11/26/14.
 */
public class PatientData implements Serializable{

  private List<Symptom> symptoms;
  private List<Medication> medications;

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

  public PatientData() {
  }

  public PatientData(List<Symptom> symptoms, List<Medication> medications) {
    this.symptoms = symptoms;
    this.medications = medications;
  }
}
