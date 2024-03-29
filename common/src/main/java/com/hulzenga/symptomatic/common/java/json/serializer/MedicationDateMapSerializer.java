package com.hulzenga.symptomatic.common.java.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.hulzenga.symptomatic.common.java.model.medication.Medication;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Created by jouke on 11/28/14.
 */
public class MedicationDateMapSerializer extends StdSerializer<Map> {

  public MedicationDateMapSerializer() {
    super(Map.class);
  }

  @Override
  public void serialize(Map map, JsonGenerator jgen, SerializerProvider provider)
      throws IOException, JsonProcessingException {

    jgen.writeStartArray();
    for (Map.Entry<Medication, Date> e : ((Map<Medication, Date>)map).entrySet()) {
      jgen.writeNumber(e.getKey().getId());
      jgen.writeNumber(e.getValue().getTime());
    }
    jgen.writeEndArray();
  }
}
