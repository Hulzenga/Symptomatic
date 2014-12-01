package com.hulzenga.symptomatic.common.java.json.deserializer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.hulzenga.symptomatic.common.java.model.medication.Medication;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jouke on 11/28/14.
 */
public class MedicationDateMapDeserializer extends StdDeserializer<Map<Medication, Date>> {

  public MedicationDateMapDeserializer() {
    super(Map.class);
  }

  @Override
  public Map<Medication, Date> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {

    Map<Medication, Date> medicationDateMap = new HashMap<Medication, Date>();

    if (jp.getCurrentToken() != JsonToken.START_ARRAY) {
      throw new JsonParseException("does not start with an array", jp.getCurrentLocation());
    }

    jp.nextToken();

    while (jp.getCurrentToken() != JsonToken.END_ARRAY) {
      Medication m = new Medication();
      m.setId(jp.getLongValue());

      jp.nextToken();
      Date d = new Date(jp.getLongValue());
      jp.nextToken();

      medicationDateMap.put(m, d);
    }
    return medicationDateMap;
  }
}
