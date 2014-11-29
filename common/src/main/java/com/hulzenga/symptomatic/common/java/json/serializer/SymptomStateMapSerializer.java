package com.hulzenga.symptomatic.common.java.json.serializer;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.hulzenga.symptomatic.common.java.model.checkin.Symptom;
import com.hulzenga.symptomatic.common.java.model.checkin.SymptomState;

import java.io.IOException;
import java.util.Map;

/**
 * Created by jouke on 11/28/14.
 */
public class SymptomStateMapSerializer extends StdSerializer<Map> {

  public SymptomStateMapSerializer() {
    super(Map.class);
  }

  @Override
  public void serialize(Map map, JsonGenerator jgen, SerializerProvider provider)
      throws IOException, JsonProcessingException {

    jgen.writeStartArray();
    for (Map.Entry<Symptom, SymptomState> e : ((Map<Symptom, SymptomState>)map).entrySet()) {
      jgen.writeNumber(e.getKey().getId());
      jgen.writeNumber(e.getValue().getId());
    }
    jgen.writeEndArray();
  }

}

