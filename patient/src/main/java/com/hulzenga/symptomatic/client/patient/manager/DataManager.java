package com.hulzenga.symptomatic.client.patient.manager;

import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.hulzenga.symptomatic.common.java.api.PatientApi;
import com.hulzenga.symptomatic.common.java.model.checkin.CheckIn;
import com.hulzenga.symptomatic.common.java.model.checkin.Symptom;
import com.hulzenga.symptomatic.common.java.model.convenience.PatientData;
import com.hulzenga.symptomatic.common.java.model.medication.Medication;
import com.hulzenga.symptomatic.common.java.network.APIFactory;

import java.io.File;
import java.io.IOException;

import retrofit.RetrofitError;

/**
 * Created by jouke on 11/11/14.
 */
public class DataManager {

  private static final String TAG = "DataManager";
  private static final String PATIENT_DATA_FILE = "patient_data.json";

  private static PatientData patientData;

  public static void downloadPatientData(final Context context, final Runnable callback) throws RetrofitError {

    final PatientApi patientApi = APIFactory.makePatientAPI(SignInManager.getToken(context));

    new AsyncTask<Void, Void, Boolean>() {
      @Override
      protected Boolean doInBackground(Void... voids) {
        try {
          patientData = patientApi.getPatientData();

          ObjectMapper mapper = new ObjectMapper();
          ObjectWriter writer = mapper.writer();

          writer.writeValue(new File(context.getFilesDir(), PATIENT_DATA_FILE), patientData);

          return true;
        } catch (JsonProcessingException e) {
          Log.e(TAG, "failed to process JSON while saving patient data to file");
        } catch (IOException e) {
          Log.e(TAG, "IOException while writing patient data to file");
        }

        return false;
      }

      @Override
      protected void onPostExecute(Boolean success) {

        if (success) {
          callback.run();
        } else {
          Toast.makeText(context, "Failed to download patient data", Toast.LENGTH_SHORT).show();
        }
      }
    }.execute();
  }

  public static PatientData getPatientData(Context context) {

    if (patientData == null ) {
      File patientDataFile = new File(context.getFilesDir(), PATIENT_DATA_FILE);
      if (patientDataFile.exists()) {
        try {
          ObjectReader reader = new ObjectMapper().reader(PatientData.class);
          patientData = (PatientData) reader.readValue(patientDataFile);
          Log.d(TAG, "successfully loaded patientData from disk");
        } catch (JsonProcessingException e) {
          Log.e(TAG, "failed to process JSON while loading patient data from file");
          return null;
        } catch (IOException e) {
          Log.e(TAG, "IOException while loading patient data from file");
          return null;
        }
      } else {
        return null;
      }
    }
    return patientData;
  }

  /**
   * checks if a check-in is valid
   *
   * @param checkIn
   * @return
   */
  public static boolean isCheckInValid(CheckIn checkIn) {
    for (Symptom s : patientData.getSymptoms()) {
      if (!checkIn.getCheckedSymptomStates().containsKey(s)) {
        return false;
      }
    }
    for (Medication m : patientData.getMedications()) {
      if (!checkIn.getMedicationsTaken().containsKey(m)) {
        return false;
      }
    }
    return true;
  }

  public static void checkIn(final Context context, final CheckIn checkIn) {
    final PatientApi patientApi = APIFactory.makePatientAPI(SignInManager.getToken(context));

    new AsyncTask<Void, Void, Boolean>() {
      @Override
      protected Boolean doInBackground(Void... params) {
        try {
          patientApi.checkIn(checkIn);
          return true;
        } catch (Exception e) {
          Log.e(TAG, "Could not check-in with server: " + e.getMessage());
          return false;
        }
      }

      @Override
      protected void onPostExecute(Boolean success) {
        if (success) {
          Log.d(TAG, "SUCCESSFUL Check-In !");
        }
      }
    }.execute();
  }

  public static void deletePatientData(Context context) {
    //remove from memory
    patientData = null;

    //remove from disk
    if (!context.deleteFile(PATIENT_DATA_FILE)) {
      Log.d(TAG, "failed to delete patientData");
    }
  }
}
