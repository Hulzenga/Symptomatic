import com.hulzenga.symptomatic.api.PatientApi;
import com.hulzenga.symptomatic.model.checkin.CheckIn;
import com.hulzenga.symptomatic.model.checkin.Symptom;


import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RestAdapter;
import retrofit.converter.JacksonConverter;

/**
 * Created by jouke on 11/8/14.
 */
public class SimpleTest {

  private PatientApi patientApi = new RestAdapter.Builder()
      .setEndpoint("http://localhost:8080/")
      .setConverter(new JacksonConverter())
      .build().create(PatientApi.class);

  @Test
  public void simpleTest() {
    List<Symptom> symptoms = patientApi.getSymptoms();

    Assert.assertNotNull(symptoms);
  }

  @Test
  public void checkInTest() {

    List<Symptom> symptoms = patientApi.getSymptoms();

    Map<Long, Long> answers = new HashMap<Long, Long>();


    for (Symptom s: symptoms) {
      answers.put(s.getId(), s.getStates().get(0).getId());
    }

    CheckIn checkIn = new CheckIn(new Date(System.currentTimeMillis()), answers);

    patientApi.checkIn(checkIn);
  }
}
