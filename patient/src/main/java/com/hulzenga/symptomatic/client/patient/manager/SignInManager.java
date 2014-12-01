package com.hulzenga.symptomatic.client.patient.manager;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.hulzenga.symptomatic.common.java.network.APIFactory;
import com.hulzenga.symptomatic.common.java.network.ServerSettings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by jouke on 11/29/14.
 */
public class SignInManager {

  private static final String TAG = SignInManager.class.getSimpleName();

  public static String token;
  private static final String TOKEN_FILE = "patient.token";

  public static void signIn(final Context context, final String username, final String password, final Runnable callback) {
    new AsyncTask<Void, Void, Boolean>() {

      @Override
      protected Boolean doInBackground(Void... params) {
        try {
          token = APIFactory.signIn(ServerSettings.PATIENT_CLIENT,
              ServerSettings.PATIENT_CLIENT_SECRET, username, password);
          if (token != null) {
            Log.d(TAG, "acquired token");
            return true;
          } else {
            return false;
          }
        } catch (Exception e) {
          Log.e(TAG, "Something went wrong, trying to Sign in: " + e.getMessage());
          return false;
        }
      }

      @Override
      protected void onPostExecute(Boolean success) {
        if (!success) {
          Toast.makeText(context, "Invalid Username and/or Password", Toast.LENGTH_SHORT).show();
          return; //done
        }

        //Save Token
        try {
          FileWriter writer = new FileWriter(new File(context.getFilesDir(), TOKEN_FILE));
          writer.write(token);
          writer.close();
        } catch (IOException e) {
          Log.e(TAG, "failed to save token: " + e.getMessage());
          return; //done
        }
        Log.d(TAG, "saved token");

        //success, so run the callback
        callback.run();
      }
    }.execute();
  }

  public static boolean hasToken(Context context) {
    return (token != null) || new File(context.getFilesDir(), TOKEN_FILE).exists();
  }

  public static String getToken(Context context) {
    //Load Token
    if (token == null) {
      try {

        BufferedReader reader = new BufferedReader(
            new FileReader(new File(context.getFilesDir(), TOKEN_FILE)));

        String token = reader.readLine();
        reader.close();
      } catch (IOException e) {
        Log.e(TAG, "Failed to load token: " + e.getMessage());
      }
    }

    return token;
  }

  public static void signOut(Context context) {
    token = null;
    //remove from disk
    if (!context.deleteFile(TOKEN_FILE)) {
      Log.d(TAG, "failed to delete patientData");
    }

    DataManager.deletePatientData(context);
    ScheduleManager.deleteSchedule(context);
  }
}
