package com.hulzenga.symptomatic.client.patient.activity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.hulzenga.symptomatic.client.patient.R;
import com.hulzenga.symptomatic.client.patient.manager.ScheduleManager;
import com.hulzenga.symptomatic.client.patient.manager.SignInManager;
import com.hulzenga.symptomatic.common.android.util.ControlFlow;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ScheduleCheckInsActivity extends Activity {

  @InjectView(R.id.scheduleListView)
  ListView scheduleListView;

  @InjectView(R.id.resetRemindersButton)
  Button resetRemindersButton;

  @OnClick(R.id.setRemindersButton)
  public void saveAndScheduleReminders() {
    ScheduleManager.scheduleReminders(this);
  }

  SimpleDateFormat simpleDateFormat;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //if not signed in return to sign in activity
    if (!SignInManager.hasToken(this)) {
      ControlFlow.redirect(this, PatientSignInActivity.class);
    }
    setContentView(R.layout.activity_schedule_check_ins);
    ButterKnife.inject(this);

    simpleDateFormat = new SimpleDateFormat("H:mm", Locale.getDefault());

    final ScheduleAdapter adapter = new ScheduleAdapter(this, ScheduleManager.getReminders(this));
    scheduleListView.setAdapter(adapter);

    scheduleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        final Calendar c = Calendar.getInstance();
        c.setTime((Date) view.getTag());

        TimePickerDialog dialog = new TimePickerDialog(ScheduleCheckInsActivity.this,
            new TimePickerDialog.OnTimeSetListener() {
              @Override
              public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                c.set(Calendar.MINUTE, minute);

                ScheduleManager.updateReminders(
                    ScheduleCheckInsActivity.this,
                    position,
                    c.getTime());

                adapter.notifyDataSetChanged();
              }
            },
            c.get(Calendar.HOUR_OF_DAY),
            c.get(Calendar.MINUTE),
            DateFormat.is24HourFormat(ScheduleCheckInsActivity.this));

        dialog.show();
      }
    });

    resetRemindersButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ScheduleManager.resetToDefaultReminders(ScheduleCheckInsActivity.this);
        adapter.notifyDataSetChanged();
      }
    });

  }

  private String formatTimeOfDay(Date date) {
    return simpleDateFormat.format(date);
  }

  private class ScheduleAdapter extends ArrayAdapter<Date> {
    private ScheduleAdapter(Context context, List<Date> objects) {
      super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      if (convertView == null) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
      }

      //should be a textview
      ((TextView) convertView).setText("every day at: " + formatTimeOfDay(getItem(position)));

      convertView.setTag(getItem(position));
      return convertView;
    }
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
}
