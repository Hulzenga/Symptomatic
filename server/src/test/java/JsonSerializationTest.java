import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hulzenga.symptomatic.common.java.json.SymptomaticModule;
import com.hulzenga.symptomatic.common.java.model.checkin.CheckIn;
import com.hulzenga.symptomatic.common.java.model.checkin.Symptom;
import com.hulzenga.symptomatic.common.java.model.checkin.SymptomState;
import com.hulzenga.symptomatic.common.java.model.medication.Medication;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jouke on 11/28/14.
 */
public class JsonSerializationTest {

  @Test
  public void checkInSerializationTest() {

    SymptomState ss1 = new SymptomState("c", 1.0f);
    ss1.setId(1);
    Symptom s1 = new Symptom("a", "b", Arrays.asList(new SymptomState[]{ss1}));
    s1.setId(2);
    Map<Symptom, SymptomState> symptomStateMap = new HashMap<Symptom, SymptomState>();
    symptomStateMap.put(s1, ss1);

    Medication m1 = new Medication("med 1");
    m1.setId(3);
    Map<Medication, Date> medicationDateMap = new HashMap<Medication, Date>();
    medicationDateMap.put(m1, new Date(System.currentTimeMillis()));

    CheckIn checkIn = new CheckIn(new Date(System.currentTimeMillis()), symptomStateMap, medicationDateMap);

    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new SymptomaticModule());

      String json = mapper.writeValueAsString(checkIn);
      CheckIn jsonCheckIn = mapper.readValue(json, CheckIn.class);

      Map<Symptom, SymptomState> jsonSymptomStateMap = jsonCheckIn.getCheckedSymptomStates();
      Map<Medication, Date> jsonMedicationDateMap = jsonCheckIn.getMedicationsTaken();

      Assert.assertEquals(symptomStateMap.keySet(), jsonSymptomStateMap.keySet());
      //Assert.assertEquals(symptomStateMap.values(), jsonSymptomStateMap.values());

      Assert.assertEquals(medicationDateMap.keySet(), jsonMedicationDateMap.keySet());
      //Assert.assertEquals(medicationDateMap.values(), jsonMedicationDateMap.values());

    } catch (JsonProcessingException e) {
      Assert.fail(e.getMessage());
    } catch (IOException e) {
      Assert.fail(e.getMessage());
    }

  }

}
