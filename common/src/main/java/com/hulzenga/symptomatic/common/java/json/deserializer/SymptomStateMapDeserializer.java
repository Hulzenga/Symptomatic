package com.hulzenga.symptomatic.common.java.json.deserializer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.hulzenga.symptomatic.common.java.model.checkin.Symptom;
import com.hulzenga.symptomatic.common.java.model.checkin.SymptomState;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jouke on 11/28/14.
 */
public class SymptomStateMapDeserializer extends StdDeserializer<Map<Symptom, SymptomState>> {

  public SymptomStateMapDeserializer() {
    super(Map.class);
  }

  @Override
  public Map<Symptom, SymptomState> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {

    Map<Symptom, SymptomState> symptomStateMap = new HashMap<Symptom, SymptomState>();

    if (jp.getCurrentToken() != JsonToken.START_ARRAY) {
      throw new JsonParseException("does not start with an array", jp.getCurrentLocation());
    }

    jp.nextToken();

    while (jp.getCurrentToken() != JsonToken.END_ARRAY) {
      Symptom s = new Symptom();
      s.setId(jp.getLongValue());

      SymptomState ss = new SymptomState();
      jp.nextToken();
      ss.setId(jp.getLongValue());
      jp.nextToken();

      symptomStateMap.put(s, ss);
    }

    return symptomStateMap;
  }
}
