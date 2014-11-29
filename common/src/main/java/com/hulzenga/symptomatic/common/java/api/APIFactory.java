package com.hulzenga.symptomatic.common.java.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hulzenga.symptomatic.common.java.json.SymptomaticModule;
import com.hulzenga.symptomatic.common.java.network.interceptor.BasicInterceptor;

import retrofit.RestAdapter;
import retrofit.converter.JacksonConverter;

/**
 * Created by jouke on 11/28/14.
 */
public class APIFactory {

  public static PatientApi makePatientApi() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new SymptomaticModule());

    return new RestAdapter.Builder()
        .setEndpoint("http://localhost:8080/")
        .setConverter(new JacksonConverter(mapper))
        .setRequestInterceptor(new BasicInterceptor("steve", "pass"))
        .build().create(PatientApi.class);
  }
}
