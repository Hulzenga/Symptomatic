package com.hulzenga.symptomatic.common.java.json;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.hulzenga.symptomatic.common.java.json.deserializer.MedicationDateMapDeserializer;
import com.hulzenga.symptomatic.common.java.json.deserializer.SymptomStateMapDeserializer;
import com.hulzenga.symptomatic.common.java.json.serializer.MedicationDateMapSerializer;
import com.hulzenga.symptomatic.common.java.json.serializer.SymptomStateMapSerializer;

import java.util.Map;

/**
 * Created by jouke on 11/28/14.
 */
public class SymptomaticModule extends SimpleModule {

  public SymptomaticModule() {
    super("Symptomatic");
    addSerializer(Map.class, new SymptomStateMapSerializer());
    addSerializer(Map.class, new MedicationDateMapSerializer());
    addDeserializer(Map.class, new SymptomStateMapDeserializer());
    addDeserializer(Map.class, new MedicationDateMapDeserializer());
  }
}
