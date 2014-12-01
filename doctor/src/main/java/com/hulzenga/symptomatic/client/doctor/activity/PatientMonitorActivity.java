package com.hulzenga.symptomatic.client.doctor.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hulzenga.symptomatic.client.doctor.R;
import com.hulzenga.symptomatic.common.java.model.convenience.SimplePatient;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PatientMonitorActivity extends BaseActivity {

  private static final String TAG = PatientMonitorActivity.class.getSimpleName();

  @OnClick(R.id.findPatientButton)
  public void findPatient() {
    if (mBound) {
      if (findPatientEditText.getText().toString().equals("")) {
        listPatients(null);
      } else {
        listPatients(findPatientEditText.getText().toString());
      }
    } else {
      Log.d(TAG, "findpatient called before service was bound");
    }
  }

  @InjectView(R.id.findPatientEditText)
  TextView findPatientEditText;

  @InjectView(R.id.patientsListView)
  ListView patientListView;

  private SimplePatientsAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_patient_monitor);

    ButterKnife.inject(this);

    adapter = new SimplePatientsAdapter(this, R.layout.item_patient_list, new ArrayList<SimplePatient>());
    patientListView.setAdapter(adapter);

    patientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long patientId = adapter.getItem(position).getId();
        Intent i = new Intent(PatientMonitorActivity.this, PatientDetailsActivity.class);
        i.putExtra(PatientDetailsActivity.PATIENT_ID_KEY, patientId);
        startActivity(i);
      }
    });
  }

  @Override
  protected void onTokenServiceConnected() {
    listPatients(null);
  }

  private class SimplePatientsAdapter extends ArrayAdapter<SimplePatient>{

    private SimplePatientsAdapter(Context context, int resource, List<SimplePatient> objects) {
      super(context, resource, objects);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
      ViewHolder holder;
      if (view != null) {
        holder = (ViewHolder) view.getTag();
      } else {
        view = getLayoutInflater().inflate(R.layout.item_patient_list, parent, false);
        holder = new ViewHolder(view);
        view.setTag(holder);
      }

      SimplePatient p = getItem(position);
      holder.name.setText(p.getLastName() + ", " + p.getFirstName());
      holder.dateOfBirth.setText(p.getDateOfBirth());
      holder.recordNumber.setText(p.getMedicalRecordNumber());

      if (p.isCauseForConcern()) {
        view.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
      } else {
        view.setBackgroundColor(getResources().getColor(android.R.color.background_light));
      }
      return view;
    }
  }

  static class ViewHolder {
    @InjectView(R.id.nameTextView) TextView name;
    @InjectView(R.id.dateOfBirthTextView) TextView dateOfBirth;
    @InjectView(R.id.recordNumberTextView) TextView recordNumber;

    public ViewHolder(View v) {
      ButterKnife.inject(this, v);
    }
  }


  private void listPatients(final String lastName) {
    new AsyncTask<Void, Void, List<SimplePatient>>(){
      @Override
      protected List<SimplePatient> doInBackground(Void... params) {
        try {
          if (lastName == null) {
            return mTokenService.getDoctorApi().getPatients();
          } else {
            return mTokenService.getDoctorApi().getNamedPatients(lastName);
          }

        } catch(Exception e) {
          Log.e(TAG, "Failed to acquire simple patient list : " + e.getMessage());
          return null;
        }
      }

      @Override
      protected void onPostExecute(List<SimplePatient> simplePatients) {

        if (simplePatients != null) {
          adapter.clear();
          adapter.addAll(simplePatients);
          if (simplePatients.size() == 0) {
            Toast.makeText(PatientMonitorActivity.this, "No patients found", Toast.LENGTH_SHORT).show();
          }
        } else {
          Toast.makeText(PatientMonitorActivity.this, "Failed to downdload patient list", Toast.LENGTH_SHORT).show();
        }

      }
    }.execute();
  }
}
