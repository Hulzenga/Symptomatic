package com.hulzenga.symptomatic.common.java.api;


import com.hulzenga.symptomatic.common.java.model.checkin.CheckIn;
import com.hulzenga.symptomatic.common.java.model.checkin.Symptom;
import com.hulzenga.symptomatic.common.java.model.convenience.PatientData;
import com.hulzenga.symptomatic.common.java.model.medication.Medication;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by jouke on 11/4/14.
 */
public interface PatientApi {

  public static final String PATIENT_CHECK_IN_PATH = "/patient/check_in";
  public static final String PATIENT_SYMPTOMS_PATH = "/patient/symptoms";
  public static final String PATIENT_MEDICATIONS_PATH = "/patient/medications";
  public static final String PATIENT_DATA_PATH = "/patient/data";

  @POST(PATIENT_CHECK_IN_PATH)
  public CheckIn checkIn(@Body CheckIn checkIn);

  @GET(PATIENT_SYMPTOMS_PATH)
  public List<Symptom> getSymptoms();

  @GET(PATIENT_MEDICATIONS_PATH)
  public List<Medication> getMedications();

  @GET(PATIENT_DATA_PATH)
  public PatientData getPatientData();

}
