package com.hulzenga.symptomatic.client.patient.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.hulzenga.symptomatic.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by jouke on 11/27/14.
 */
public class TimeDatePickerDialog extends DialogFragment {

  public interface TimeDateListener {
    public void setMedicationTakenTime(Date timeDate);
  }

  @InjectView(R.id.dayPicker)
  NumberPicker mDayPicker;

  @InjectView(R.id.timePicker)
  TimePicker mTimePicker;

  private static final String TITLE_KEY = "title";

  public static TimeDatePickerDialog newInstance(String title) {
    TimeDatePickerDialog dialog = new TimeDatePickerDialog();
    Bundle args = new Bundle();
    args.putString(TITLE_KEY, title);
    dialog.setArguments(args);
    return dialog;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();

    View dialogView = inflater.inflate(R.layout.dialog_time_date_picker, null);
    ButterKnife.inject(this, dialogView);

    mDayPicker.setMinValue(0);
    mDayPicker.setMaxValue(1);

    mDayPicker.setDisplayedValues(new String[]{"yesterday", "today"});
    mDayPicker.setValue(1);
    mDayPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

    mTimePicker.setIs24HourView(DateFormat.is24HourFormat(getActivity()));
    mTimePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);


    builder.setView(dialogView)
        .setTitle(getArguments().getString(TITLE_KEY))
        .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {

            GregorianCalendar c = new GregorianCalendar();
            c.set(Calendar.HOUR_OF_DAY, mTimePicker.getCurrentHour());
            c.set(Calendar.MINUTE, mTimePicker.getCurrentMinute());
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);

            if (mDayPicker.getValue() == 0) {
              c.add(Calendar.DATE, -1); //yesterday
            }

            Fragment parent = getTargetFragment();
            TimeDateListener tdl = (TimeDateListener) parent;

            tdl.setMedicationTakenTime(c.getTime());
          }
        })
        .setNegativeButton("cancel", null);

    return builder.create();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.reset(this);
  }
}
