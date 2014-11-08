package com.hulzenga.symptomatic.api;


import com.hulzenga.symptomatic.model.checkin.CheckIn;
import com.hulzenga.symptomatic.model.checkin.Symptom;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by jouke on 11/4/14.
 */
public interface PatientApi {

  @POST("/checkin")
  public CheckIn checkIn(@Body CheckIn checkIn);

  @GET("/symptoms")
  public List<Symptom> getSymptoms();
}
