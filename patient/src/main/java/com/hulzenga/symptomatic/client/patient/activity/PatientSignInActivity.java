package com.hulzenga.symptomatic.client.patient.activity;

import android.os.Bundle;
import android.util.Log;

import com.hulzenga.symptomatic.client.patient.manager.DataManager;
import com.hulzenga.symptomatic.client.patient.manager.SignInManager;
import com.hulzenga.symptomatic.common.android.util.ControlFlow;

public class PatientSignInActivity extends com.hulzenga.symptomatic.common.android.activity.SignInActivity {


  private static final String TAG = PatientSignInActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    //bypass the signin if already signed in
    if (SignInManager.hasToken(this)) {
      ControlFlow.redirect(this, ScheduleCheckInsActivity.class);
    }


    super.onCreate(savedInstanceState);

    //TODO: remove if not testing
    setDefaultSignIn("Bob", "Bob");
  }

  @Override
  public void signIn(String username, String password) {
    Log.d(PatientSignInActivity.class.getSimpleName(), "Patient Sign in called " + username + ", " + password);
    SignInManager.signIn(this, username, password, new Runnable() {
      @Override
      public void run() {

        DataManager.downloadPatientData(PatientSignInActivity.this, new Runnable() {
          @Override
          public void run() {
            Log.d(TAG, "downloaded patient data");
            ControlFlow.redirect(PatientSignInActivity.this, ScheduleCheckInsActivity.class);
          }
        });
        Log.d(TAG, "signed in");
      }
    });
  }
}
