package com.hulzenga.symptomatic.client.patient.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hulzenga.symptomatic.client.patient.activity.CheckInActivity;

public class ReminderReceiver extends BroadcastReceiver {

  private static final String TAG = ReminderReceiver.class.getSimpleName();

  public ReminderReceiver() {
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    Log.d(TAG, "ReminderReceiver called");
    Intent i = new Intent(context, CheckInActivity.class);

    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(i);
  }
}
