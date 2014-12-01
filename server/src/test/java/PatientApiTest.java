import com.hulzenga.symptomatic.common.java.api.PatientApi;
import com.hulzenga.symptomatic.common.java.model.checkin.CheckIn;
import com.hulzenga.symptomatic.common.java.model.checkin.Symptom;
import com.hulzenga.symptomatic.common.java.model.checkin.SymptomState;
import com.hulzenga.symptomatic.common.java.model.convenience.PatientData;
import com.hulzenga.symptomatic.common.java.model.medication.Medication;
import com.hulzenga.symptomatic.common.java.network.APIFactory;
import com.hulzenga.symptomatic.common.java.network.ServerSettings;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit.RetrofitError;

/**
 * Created by jouke on 11/8/14.
 */
public class PatientApiTest {

  private String token;

  private PatientApi patientApi;

  @Before
  public void init() {
    token = APIFactory.signIn(ServerSettings.PATIENT_CLIENT,
        ServerSettings.PATIENT_CLIENT_SECRET, "Bob", "Bob");
    patientApi = APIFactory.makePatientAPI(token);
  }
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
