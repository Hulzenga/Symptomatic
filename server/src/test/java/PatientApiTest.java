import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hulzenga.symptomatic.common.java.api.APIFactory;
import com.hulzenga.symptomatic.common.java.api.PatientApi;
import com.hulzenga.symptomatic.common.java.json.SymptomaticModule;
import com.hulzenga.symptomatic.common.java.model.checkin.CheckIn;
import com.hulzenga.symptomatic.common.java.model.checkin.Symptom;
import com.hulzenga.symptomatic.common.java.model.checkin.SymptomState;
import com.hulzenga.symptomatic.common.java.model.convenience.PatientData;
import com.hulzenga.symptomatic.common.java.model.medication.Medication;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;

/**
 * Created by jouke on 11/8/14.
 */
public class PatientApiTest {

  private PatientApi patientApi = APIFactory.makePatientApi();

  @Test
  public void getSymptomsTest() {
    Assert.assertNotNull(patientApi.getSymptoms());
  }

  @Test
  public void getMedicationsTest() {
    Assert.assertNotNull(patientApi.getMedications());
  }

  @Test
  public void getPatientDataTest() {
    Assert.assertNotNull(patientApi.getPatientData());
  }

  @Test
  public void checkInSubmissionTest() {

    PatientData data = patientApi.getPatientData();

    Map<Symptom, SymptomState> symptomStateMap = new HashMap<Symptom, SymptomState>();
    Map<Medication, Date> medicationDateMap = new HashMap<Medication, Date>();

    for (Symptom s : data.getSymptoms()) {
      symptomStateMap.put(s, s.getStates().get(0));
    }

    for (Medication m : data.getMedications()) {
      medicationDateMap.put(m, new Date(System.currentTimeMillis()));
    }

    CheckIn checkIn = new CheckIn(new Date(System.currentTimeMillis()), symptomStateMap, medicationDateMap);


    try {
      patientApi.checkIn(checkIn);
    } catch (RetrofitError e) {
      Assert.fail(e.getMessage());
    }
  }


}
