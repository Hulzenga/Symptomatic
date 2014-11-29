package com.hulzenga.symptomatic.client.patient.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hulzenga.symptomatic.client.patient.dialog.TimeDatePickerDialog;
import com.hulzenga.symptomatic.common.java.model.medication.Medication;

import java.util.Date;

/**
 * Created by jouke on 11/26/14.
 */
public class MedicationQuestionFragment extends CheckInQuestionFragment implements TimeDatePickerDialog.TimeDateListener{

  private static final String TAG = "MedicationQuestionFragment";

  public static MedicationQuestionFragment newInstance(Medication medication) {
    MedicationQuestionFragment fragment = new MedicationQuestionFragment();
    Bundle args = new Bundle();
    args.putSerializable(ARG_QUESTION_OBJECT, medication);
    fragment.setArguments(args);
    return fragment;
  }

  //both of these are needed in the
  Button yesButton;
  Medication medication;

  @Override
  public void setMedicationTakenTime(Date date) {
    setAnswerButton(yesButton);
    checkInPresenter.setMedicationQuestionAnswer(medication, date);
    Log.d(TAG, String.valueOf(date) + ", " + date.getTime());
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = super.onCreateView(inflater, container, savedInstanceState);

    medication = (Medication) getArguments().getSerializable(ARG_QUESTION_OBJECT);

    questionTextView.setText("Did you take your " + medication.getName() + "?");
    answerLayout.setOrientation(LinearLayout.HORIZONTAL);

    yesButton = newAnswerButton();
    Button noButton = newAnswerButton();

    yesButton.setText("Yes");
    noButton.setText("No");

    //mark buttons if already answered
    if (checkInPresenter.medicationQuestionAnswered(medication)) {
      Date takenDate = checkInPresenter.getMedicationQuestionAnswer(medication);
      if (!takenDate.equals(Medication.NOT_TAKEN)) {
        setAnswerButton(yesButton);
      } else {
        setAnswerButton(noButton);
      }
    }

    yesButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        TimeDatePickerDialog dialog = TimeDatePickerDialog.newInstance("When did you take the "
            + medication.getName());

        dialog.setTargetFragment(MedicationQuestionFragment.this, 0);
        dialog.show(getFragmentManager(), "td_picker");
      }
    });

    noButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        setAnswerButton((Button) v);
        checkInPresenter.setMedicationQuestionAnswer(medication, Medication.NOT_TAKEN);
      }
    });

    return rootView;
  }
}
