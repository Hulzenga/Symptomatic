package com.hulzenga.symptomatic.client.doctor.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.hulzenga.symptomatic.client.doctor.R;
import com.hulzenga.symptomatic.client.doctor.iface.MedicationProvider;
import com.hulzenga.symptomatic.client.doctor.service.TokenService;
import com.hulzenga.symptomatic.common.java.model.checkin.CheckIn;
import com.hulzenga.symptomatic.common.java.model.checkin.Symptom;
import com.hulzenga.symptomatic.common.java.model.checkin.SymptomState;
import com.hulzenga.symptomatic.common.java.model.convenience.CheckInMapper;
import com.hulzenga.symptomatic.common.java.model.medication.Medication;
import com.hulzenga.symptomatic.common.java.model.user.Patient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PatientDetailsActivity extends BaseActivity implements MedicationProvider {

  private static final String TAG = PatientDetailsActivity.class.getSimpleName();

  public static final String PATIENT_ID_KEY = "patient_details";

  private long patientId;
  private Patient patient;

  final private List<Symptom> possibleSymptoms = new ArrayList<Symptom>();
  final private List<Medication> possibleMedications = new ArrayList<Medication>();

  @InjectView(R.id.detailsNameText)
  TextView nameText;

  @InjectView(R.id.detailsDateOfBirthText)
  TextView dateOfBirthText;

  @InjectView(R.id.detailsRecordNumberText)
  TextView recordNumberText;

  @InjectView(R.id.detailsWebView)
  WebView detailsWebView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent creatorIntent = getIntent();
    patientId = creatorIntent.getExtras().getLong(PATIENT_ID_KEY);

    setContentView(R.layout.activity_patient_details);
    ButterKnife.inject(this);


  }

  @OnClick(R.id.adjustMedicationButton)
  public void adjustMedicationDialog() {
    new UpdateMedicationDialog().show(getFragmentManager(), null);
  }

  private void displayPatientDetails() {
    if (patient == null) {
      Log.d(TAG, "displayPatientDetails called before patient was loaded");
    }

    nameText.setText(patient.getLastName() + ", " + patient.getFirstName());
    dateOfBirthText.setText(patient.getDateOfBirth());
    recordNumberText.setText(patient.getMedicalRecordNumber());

    StringBuilder concerns = new StringBuilder("");
    if (patient.isCauseForConcern()) {
      concerns.append(
          "    <h1>Concerning Symptoms</h1>\n" +
          "    <ul>\n");
      for (SymptomState ss : patient.getConcerningSymptoms()) {
        for (Symptom s: possibleSymptoms) {
          if (s.getStates().contains(ss)) {
            concerns.append("<li>" + s.getDescriptor() + ": " + ss.getState() + "</li>\n");
          }
        }


      }
      concerns.append(
          "    </ul>\n");
    }

    StringBuilder medications = new StringBuilder("<h2>Current Medications</h2>\n" + "<ul>\n");
    for (Medication m: patient.getMedications()) {
      medications.append("<li>" + m.getName() + "</li>");
    }
    medications.append("</ul>\n");

    StringBuilder checkIns = new StringBuilder("<h2>Check-Ins</h2>\n" + "<ul>\n");
    CheckInMapper cm = new CheckInMapper(possibleMedications, possibleSymptoms);

    List<CheckIn> checkInList = patient.getCheckIns();
    if (checkInList.size() > 0) {
      Collections.sort(checkInList);
      for (CheckIn c: checkInList) {
        checkIns.append("<h4>Check-In " + c.getCheckInDate().toString() + "</h4>\n");
        checkIns.append("<ul>");

        //due to JSON mapping only ids are stored in the Symptom and the State !!!!
        for(Map.Entry<Symptom, SymptomState> e: c.getCheckedSymptomStates().entrySet()) {
          checkIns.append("<li>" + cm.getSymptomFromId(e.getKey().getId()) + ": " + cm.getSymptomStateFromId(e.getValue().getId()) + "</li>");
        }
        for(Map.Entry<Medication, Date> e: c.getMedicationsTaken().entrySet()) {
          checkIns.append("<li>" + cm.getMedicationNameFromId(e.getKey().getId()) + ": ");
          if (e.getValue().equals(Medication.NOT_TAKEN)) {
            checkIns.append("NOT TAKEN"  + "</li>");
          } else {
            checkIns.append(e.getValue().toString() + "</li>");
          }
        }

        checkIns.append("</ul>");

      }
    } else {
      checkIns.append("<h4>Patient hasn't checked-in in yet</h4>\n");
    }

    String html =
        "<html>  \n" +
            "  <body>\n" +
            concerns.toString() +
            medications.toString() +
            checkIns.toString() +
            "  </body>\n" +
            "</html>\n";

    detailsWebView.loadData(html, "text/html", "utf-8");
  }

  @Override
  protected void onTokenServiceConnected() {
    new AsyncTask<Void, Void, Patient>() {
      @Override
      protected Patient doInBackground(Void... params) {
        try {
          //also download possible symptoms and medications if not already downloaded
          if (possibleSymptoms.size() == 0) {
            possibleSymptoms.addAll(mTokenService.getDoctorApi().getSymptoms());
          }
          if (possibleMedications.size() == 0) {
            possibleMedications.addAll(mTokenService.getDoctorApi().getMedications());
          }

          return mTokenService.getDoctorApi().getPatient(patientId);

        } catch (Exception e) {
          Log.e(TAG, "Failed to acquire simple patient list : " + e.getMessage());
          return null;
        }
      }

      @Override
      protected void onPostExecute(Patient result) {

        if (result != null) {
          patient = result;
          displayPatientDetails();
        } else {
          Toast.makeText(PatientDetailsActivity.this, "Failed to downdload patient information", Toast.LENGTH_SHORT).show();
          finish();
        }

      }
    }.execute();
  }

  @Override
  public List<Medication> getPossibleMedications() {
    return possibleMedications;
  }

  @Override
  public List<Medication> getPrescribedMedications() {
    return patient.getMedications();
  }

  @Override
  public void setNewMedications(final List<Medication> newMedications) {
    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... params) {
        mTokenService.getDoctorApi().setPatientMedication(patient.getId(), newMedications);
        return null;
      }

      @Override
      protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        //restart the patient details activity
        finish();
        startActivity(getIntent());
      }
    }.execute();
  }

  public static class UpdateMedicationDialog extends DialogFragment {

    private MedicationProvider provider;

    @Override
    public void onAttach(Activity activity) {
      super.onAttach(activity);
      provider = (MedicationProvider) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

      final List<Medication> allMedications = provider.getPossibleMedications();
      List<Medication> selectedMedications = provider.getPrescribedMedications();

      final int length = allMedications.size();

      final String[] options = new String[length];
      final boolean[] checked = new boolean[length];
      for (int i = 0; i < length; i++) {
        options[i] = allMedications.get(i).getName();
        checked[i] = selectedMedications.contains(allMedications.get(i));
      }

      builder.setMultiChoiceItems(options, checked, new DialogInterface.OnMultiChoiceClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
          checked[which] = isChecked;
        }
      });

      builder.setPositiveButton("Change Medications", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          List<Medication> newMedications = new ArrayList<Medication>();
          for (int i = 0; i < length; i++) {
            if (checked[i]) {
              newMedications.add(allMedications.get(i));
            }
          }

          provider.setNewMedications(newMedications);
        }
      });

      builder.setNegativeButton("Cancel", null);

      return builder.create();
    }
  }

}
