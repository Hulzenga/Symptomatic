package com.hulzenga.symptomatic.client.patient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.hulzenga.symptomatic.R;
import com.hulzenga.symptomatic.common.java.api.PatientApi;
import com.hulzenga.symptomatic.client.patient.data.DataManager;
import com.hulzenga.symptomatic.common.java.model.checkin.Symptom;

import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class TestActivity extends Activity {

  private List<Symptom> symptoms;
  private Map<Long, Long> answers;

  private PatientApi patientApi;
  private Random random = new Random();

  @OnClick(R.id.testDownloadPatientDataButton)
  public void downloadPatientData() {
    DataManager.downloadPatientData(this, new Runnable() {
      @Override
      public void run() {
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            Intent intent = new Intent(TestActivity.this, CheckInActivity.class);
            startActivity(intent);
          }
        });
      }
    });
  }

  @OnClick(R.id.testLoginButton)
  public void login() {
    Intent intent = new Intent(TestActivity.this, LoginActivity.class);
    startActivity(intent);
  }

  @OnClick(R.id.testCheckInButton)
  public void goCheckIn() {
    Intent intent = new Intent(TestActivity.this, CheckInActivity.class);
    startActivity(intent);
  }

  @OnClick(R.id.testSchedulerButton)
  public void schedule() {
    Intent intent = new Intent(TestActivity.this, ScheduleCheckInsActivity.class);
    startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test);
    ButterKnife.inject(this);

  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_test, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
