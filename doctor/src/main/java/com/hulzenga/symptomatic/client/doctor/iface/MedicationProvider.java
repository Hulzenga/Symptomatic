package com.hulzenga.symptomatic.client.doctor.iface;

import com.hulzenga.symptomatic.common.java.model.medication.Medication;

import java.util.List;

/**
 * USED ONLY in PatientDetailsActivity
 * Created by jouke on 12/1/14.
 */
public interface MedicationProvider {

  public List<Medication> getPossibleMedications();
  public List<Medication> getPrescribedMedications();
  public void setNewMedications(List<Medication> newMedications);
}
