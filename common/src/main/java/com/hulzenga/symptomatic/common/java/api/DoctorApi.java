package com.hulzenga.symptomatic.common.java.api;

import com.hulzenga.symptomatic.common.java.model.checkin.Symptom;
import com.hulzenga.symptomatic.common.java.model.convenience.SimplePatient;
import com.hulzenga.symptomatic.common.java.model.medication.Medication;
import com.hulzenga.symptomatic.common.java.model.user.Patient;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by jouke on 11/4/14.
 */
public interface DoctorApi {

  public static final String DOCTOR_PATIENT = "/doctor/patient";
  public static final String DOCTOR_NAMED_PATIENTS = "/doctor/namedpatients";
  public static final String DOCTOR_MEDICATION = "/doctor/medication";
  public static final String DOCTOR_SYMPTOMS = "/doctor/symptom";

  @GET(DOCTOR_PATIENT)
  public List<SimplePatient> getPatients();

  @GET(DOCTOR_PATIENT + "/lastname/{lastname}")
  public List<SimplePatient> getNamedPatients(@Path("lastname") String lastName);

  @GET(DOCTOR_PATIENT + "/{id}")
  public Patient getPatient(@Path("id") long patientId);

  @GET(DOCTOR_MEDICATION)
  public List<Medication> getMedications();

  @GET(DOCTOR_SYMPTOMS)
  public List<Symptom> getSymptoms();

  @POST(DOCTOR_PATIENT + "/{id}/medication")
  public List<Medication> setPatientMedication(@Path("id") long patientId, @Body List<Medication> medications);
}
