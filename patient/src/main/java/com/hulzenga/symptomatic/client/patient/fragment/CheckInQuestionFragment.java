package com.hulzenga.symptomatic.client.patient.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hulzenga.symptomatic.client.patient.R;
import com.hulzenga.symptomatic.common.java.model.checkin.Symptom;
import com.hulzenga.symptomatic.common.java.model.checkin.SymptomState;
import com.hulzenga.symptomatic.common.java.model.medication.Medication;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by jouke on 11/26/14.
 */
public abstract class CheckInQuestionFragment extends Fragment {

  private static final String TAG = "CheckInQuestionFragment";

  public interface CheckInPresenter {
    public boolean symptomQuestionAnswered(Symptom symptom);
    public SymptomState getSymptomQuestionAnswer(Symptom symptom);
    public void setSymptomQuestionAnswer(Symptom symptom, SymptomState symptomState);

    public boolean medicationQuestionAnswered(Medication medication);
    public Date getMedicationQuestionAnswer(Medication medication);
    public void setMedicationQuestionAnswer(Medication medication, Date date);
  }

  @InjectView(R.id.questionTextView)
  TextView questionTextView;

  @InjectView(R.id.answerLayout)
  LinearLayout answerLayout;

  protected static final String ARG_QUESTION_OBJECT = "question_object";
  protected CheckInPresenter checkInPresenter;
  protected List<Button> answerButtons = new ArrayList<Button>();

  protected Button newAnswerButton() {
    Button b = new Button(getActivity());
    applyButtonStyle(b);
    answerButtons.add(b);
    answerLayout.addView(b);
    return b;
  }

  protected void setAnswerButton(Button b) {
    clearAnswerButtons();
    applyAnswerButtonStyle(b);
  }

  private void clearAnswerButtons() {
    for (Button b: answerButtons) {
      applyButtonStyle(b);
    }
  }

  private void applyButtonStyle(Button b) {
    b.setBackgroundColor(Color.WHITE);
  }

  private void applyAnswerButtonStyle(Button b) {
    b.setBackgroundColor(Color.GREEN);
  }

  @Override
  public void onAttach(Activity activity) {
    try {
      checkInPresenter = (CheckInPresenter) activity;
    } catch (Exception e) {
      Log.e(TAG, "parent " + activity.getClass().getName() +
          " did not implement " + CheckInPresenter.class.getSimpleName());
    }

    super.onAttach(activity);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_question_answer, container, false);
    ButterKnife.inject(this, rootView);

    return rootView;
  }
}
