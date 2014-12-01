package com.hulzenga.symptomatic.client.patient.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hulzenga.symptomatic.common.java.model.checkin.Symptom;
import com.hulzenga.symptomatic.common.java.model.checkin.SymptomState;

/**
 * Created by jouke on 11/26/14.
 */
public class SymptomQuestionFragment extends CheckInQuestionFragment {

  private static final String TAG = "SymptomQuestionFragment";

  public static SymptomQuestionFragment newInstance(Symptom symptom) {
    SymptomQuestionFragment fragment = new SymptomQuestionFragment();
    Bundle args = new Bundle();
    args.putSerializable(ARG_QUESTION_OBJECT, symptom);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = super.onCreateView(inflater, container, savedInstanceState);

    final Symptom symptom = (Symptom) getArguments().getSerializable(ARG_QUESTION_OBJECT);

    questionTextView.setText(symptom.getQuestion());

    SymptomState checkedSymptomState = checkInPresenter.getSymptomQuestionAnswer(symptom);

    Button.OnClickListener listener = new Button.OnClickListener() {
      @Override
      public void onClick(View v) {
        setAnswerButton((Button) v);
        checkInPresenter.setSymptomQuestionAnswer(symptom, (SymptomState) v.getTag());
      }
    };

    for (SymptomState symptomState : symptom.getStates()) {

      Button b = newAnswerButton();
      b.setText(symptomState.getState());
      b.setTag(symptomState);
      b.setOnClickListener(listener);

      if (symptomState.equals(checkedSymptomState)) {
        setAnswerButton(b);
      }
    }

    return rootView;
  }
}
