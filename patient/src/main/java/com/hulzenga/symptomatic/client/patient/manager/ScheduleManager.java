package com.hulzenga.symptomatic.client.patient.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.hulzenga.symptomatic.client.patient.receivers.ReminderReceiver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by jouke on 11/29/14.
 */
public class ScheduleManager {

  private static final String TAG = ScheduleManager.class.getSimpleName();

  private static final String REMINDERS_FILE = "reminders.json";

  private static List<Date> reminders;

  public static List<Date> getDefaultReminders() {
    List<Date> defaults = new ArrayList<Date>();

    Calendar c = Calendar.getInstance();
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.HOUR_OF_DAY, 8);
    defaults.add(c.getTime());
    c.set(Calendar.HOUR_OF_DAY, 12);
    defaults.add(c.getTime());
    c.set(Calendar.HOUR_OF_DAY, 16);
    defaults.add(c.getTime());
    c.set(Calendar.HOUR_OF_DAY, 20);
    defaults.add(c.getTime());

    return defaults;
  }

  public static void resetToDefaultReminders(Context context) {
    List<Date> defaults = getDefaultReminders();
    for(int i = 0; i < defaults.size(); i++) {
      reminders.set(i, defaults.get(i));
    }
    saveReminders(context);
  }

  public static List<Date> getReminders(Context context) {

    if (reminders == null) {
      File reminderFile = new File(context.getFilesDir(), REMINDERS_FILE);
      if (reminderFile.exists()) {
        try {
          ObjectReader reader = new ObjectMapper().reader(new TypeReference<List<Date>>() {
          });
          reminders = reader.readValue(reminderFile);
        } catch (IOException e) {
          Log.d(TAG, e.getMessage());
        }
      } else {
        reminders = getDefaultReminders();
        saveReminders(context);
      }
    }

    return reminders;
  }

  public static void updateReminders(Context context, int reminderNumber, Date reminder) {
    //ensures reminders are loaded into memory
    if (reminders == null) {
      getReminders(context);
    }

    reminders.set(reminderNumber, reminder);
  }

  private static void saveReminders(Context context) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      mapper.writer().writeValue(new File(context.getFilesDir(), REMINDERS_FILE), reminders);
    } catch (IOException e) {
      Log.e(TAG, "Failed to save check in times");
    }
  }

  private static PendingIntent getReminderPendingIntent(Context context, int index) {
    Intent checkInIntent = new Intent(context, ReminderReceiver.class);
    return PendingIntent.getBroadcast(context, index, checkInIntent, PendingIntent.FLAG_CANCEL_CURRENT);
  }

  public static void scheduleReminders(Context context) {

    saveReminders(context);

    //first unschedule existing ones
    unscheduleReminders(context);

    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

    for (int i = 0; i < getReminders(context).size(); i++) {
      alarmManager.setRepeating(
          AlarmManager.RTC_WAKEUP,
          getReminders(context).get(i).getTime(),
          AlarmManager.INTERVAL_DAY,
          getReminderPendingIntent(context, i));
    }

    Log.d(TAG, "Reminders scheduled");
  }

  public static void unscheduleReminders(Context context) {

    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

    for (int i = 0; i < getReminders(context).size(); i++) {
      alarmManager.cancel(getReminderPendingIntent(context, i));
    }
  }

  public static void deleteSchedule(Context context) {
    unscheduleReminders(context);

    reminders = null;
    if (!new File(context.getFilesDir(), REMINDERS_FILE).delete()) {
      Log.d(TAG, "failed to delete reminders");
    }

    Log.d(TAG, "deleted reminders");
  }


}
