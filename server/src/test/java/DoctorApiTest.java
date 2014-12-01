import com.hulzenga.symptomatic.common.java.api.DoctorApi;
import com.hulzenga.symptomatic.common.java.model.convenience.SimplePatient;
import com.hulzenga.symptomatic.common.java.model.medication.Medication;
import com.hulzenga.symptomatic.common.java.network.APIFactory;
import com.hulzenga.symptomatic.common.java.network.ServerSettings;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jouke on 11/30/14. Run test/pop before the tests !!!
 */
public class DoctorApiTest {


  private String token;

  private DoctorApi doctorApi;

  @Before
  public void init() {
    token = APIFactory.signIn(ServerSettings.DOCTOR_CLIENT,
        ServerSettings.DOCTOR_CLIENT_SECRET, "Alice", "Alice");
    doctorApi = APIFactory.makeDoctorApi(token);
  }

  @Test
  public void getSimplePatientsTest() {
    Assert.assertNotNull(doctorApi.getPatients());
  }

  @Test
  public void getPatientTest() {
    List<SimplePatient> simplePatients = doctorApi.getPatients();

    for (SimplePatient p: simplePatients) {
      Assert.assertNotNull(doctorApi.getPatient(p.getId()));
    }
  }

  @Test
  public void getMedicationsTest() {
    Assert.assertNotNull(doctorApi.getMedications());
  }


  @Test
  public void getNamedPatientTest() {
    Assert.assertEquals(1, doctorApi.getNamedPatients("Johnson").size());
  }
  @Test
  public void changeMedicationTest() {
    List<SimplePatient> simplePatients = doctorApi.getPatients();
    List<Medication> medications = doctorApi.getMedications();

    long id = simplePatients.get(0).getId();

    List<Medication> newMedication = new ArrayList<Medication>();
    newMedication.add(medications.get(0));

    doctorApi.setPatientMedication(id, newMedication );

    Assert.assertEquals(1, doctorApi.getPatient(id).getMedications().size());

  }

}
