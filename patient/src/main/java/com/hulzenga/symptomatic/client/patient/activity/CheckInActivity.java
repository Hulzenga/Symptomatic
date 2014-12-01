package com.hulzenga.symptomatic.client.patient.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hulzenga.symptomatic.client.patient.R;
import com.hulzenga.symptomatic.client.patient.fragment.CheckInQuestionFragment;
import com.hulzenga.symptomatic.client.patient.fragment.MedicationQuestionFragment;
import com.hulzenga.symptomatic.client.patient.fragment.SymptomQuestionFragment;
import com.hulzenga.symptomatic.client.patient.manager.DataManager;
import com.hulzenga.symptomatic.client.patient.manager.SignInManager;
import com.hulzenga.symptomatic.common.android.util.ControlFlow;
import com.hulzenga.symptomatic.common.android.view.SlidingTabLayout;
import com.hulzenga.symptomatic.common.java.model.checkin.CheckIn;
import com.hulzenga.symptomatic.common.java.model.checkin.Symptom;
import com.hulzenga.symptomatic.common.java.model.checkin.SymptomState;
import com.hulzenga.symptomatic.common.java.model.medication.Medication;

import java.sql.Date;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class CheckInActivity extends Activity implements CheckInQuestionFragment.CheckInPresenter {

  final static String TAG = CheckInActivity.class.getSimpleName();
  final static String CHECK_IN_KEY = "check_in_key";

  private CheckIn mCheckIn;
  private CheckInPagerAdapter mCheckInPagerAdapter;

  private int nSymptoms;
  private int nMedications;

  @InjectView(R.id.submitCheckInButton)
  Button checkInButton;

  @OnClick(R.id.submitCheckInButton)
  public void checkIn() {
    DataManager.checkIn(this, mCheckIn);
    Toast.makeText(this, "Thank you for Checking-In", Toast.LENGTH_SHORT).show();
    finish();
  }

  @InjectView(R.id.checkInPager)
  ViewPager mViewPager;

  @InjectView(R.id.sliding_tabs)
  SlidingTabLayout mSlidingTabLayout;

  /*-------------------*
   * LIFECYCLE METHODS *
   *-------------------*/
  @Override
  protected void onSaveInstanceState(Bundle outState) {
    outState.putSerializable(CHECK_IN_KEY, mCheckIn);
    super.onSaveInstanceState(outState);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //first check if patient data is available, if not quit immediately
    if (DataManager.getPatientData(this) != null) {
      nSymptoms = DataManager.getPatientData(this).getSymptoms().size();
      nMedications = DataManager.getPatientData(this).getMedications().size();
    } else {
      //this should never happen
      Toast.makeText(this, "Patient data is not available", Toast.LENGTH_LONG).show();
      Log.e(TAG, "Patient data is not available");
      finish();
      return;
    }

    //setup UI
    setContentView(R.layout.activity_check_in);
    ButterKnife.inject(this);

    //setup tabs
    final ActionBar actionBar = getActionBar();

    if (savedInstanceState == null) {
      mCheckIn = new CheckIn(
          new Date(System.currentTimeMillis()),
          new HashMap<Symptom, SymptomState>(),
          new HashMap<Medication, java.util.Date>());
    } else {
      mCheckIn = (CheckIn) savedInstanceState.get(CHECK_IN_KEY);
    }
    updateSubmissionValidity(); //syncs mCheckIn state with the UI

    // Create the adapter that will return a fragment for each of the three
    // primary sections of the activity.
    mCheckInPagerAdapter = new CheckInPagerAdapter(getFragmentManager());
    mViewPager.setAdapter(mCheckInPagerAdapter);
    mSlidingTabLayout.setViewPager(mViewPager);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_sign_out) {
      SignInManager.signOut(this);
      ControlFlow.redirect(this, PatientSignInActivity.class);
    }

    return super.onOptionsItemSelected(item);
  }

  /*------------*
   * UI METHODS *
   *------------*/
  public void updateSubmissionValidity() {
    if (DataManager.isCheckInValid(mCheckIn)) {
      checkInButton.setVisibility(View.VISIBLE);
    } else {
      checkInButton.setVisibility(View.INVISIBLE);
    }
  }

  /*---------------------------*
   * CHECK-IN PRESENTER METHODS *
   *---------------------------*/
  @Override
  public boolean symptomQuestionAnswered(Symptom symptom) {
    return mCheckIn.getCheckedSymptomStates().containsKey(symptom.getId());
  }

  @Override
  public SymptomState getSymptomQuestionAnswer(Symptom symptom) {
    return mCheckIn.getCheckedSymptomStates().get(symptom);
  }

  @Override
  public void setSymptomQuestionAnswer(Symptom symptom, SymptomState symptomState) {
    mCheckIn.getCheckedSymptomStates().put(symptom, symptomState);
    updateSubmissionValidity();
  }

  @Override
  public boolean medicationQuestionAnswered(Medication medication) {
    return mCheckIn.getMedicationsTaken().containsKey(medication);
  }

  @Override
  public java.util.Date getMedicationQuestionAnswer(Medication medication) {
    return mCheckIn.getMedicationsTaken().get(medication);
  }

  @Override
  public void setMedicationQuestionAnswer(Medication medication, java.util.Date date) {
    mCheckIn.getMedicationsTaken().put(medication, date);
    updateSubmissionValidity();
  }

  /*---------------*
   * INNER CLASSES *
   *---------------*/

  /**
   * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
   * one of the sections/tabs/pages.
   */
  public class CheckInPagerAdapter extends FragmentStatePagerAdapter {

    public CheckInPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      if (position < nSymptoms) {
        return SymptomQuestionFragment.newInstance(DataManager
            .getPatientData(getApplicationContext()).getSymptoms().get(position));
      } else {
        return MedicationQuestionFragment.newInstance(DataManager
            .getPatientData(getApplicationContext()).getMedications().get(position - nSymptoms));
      }
    }

    @Override
    public int getCount() {
      return DataManager.getPatientData(getApplicationContext()).getSymptoms().size() +
          DataManager.getPatientData(getApplicationContext()).getMedications().size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
      if (position < nSymptoms) {
        return DataManager.getPatientData(getApplicationContext())
            .getSymptoms().get(position).getDescriptor();
      } else {
        return DataManager.getPatientData(getApplicationContext())
            .getMedications().get(position - nSymptoms).getName();
      }
    }
  }

}
