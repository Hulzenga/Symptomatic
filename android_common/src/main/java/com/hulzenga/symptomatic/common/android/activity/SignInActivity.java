package com.hulzenga.symptomatic.common.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hulzenga.symptomatic.common.android.R;

public abstract class SignInActivity extends Activity {

  //TODO figure out why butterknife can't InjectView here !?!?!

  TextView mSignInInfoText;
  EditText mUserNameEditText;
  EditText mPasswordEditText;
  Button mSignInButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_in);

    mSignInInfoText = (TextView) findViewById(R.id.signInInfoText);
    mUserNameEditText = (EditText) findViewById(R.id.userNameEditText);
    mPasswordEditText = (EditText) findViewById(R.id.passwordEditText);
    mSignInButton = (Button) findViewById(R.id.signInButton);

    mSignInButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        signIn(mUserNameEditText.getText().toString(), mPasswordEditText.getText().toString());
      }
    });
  }

  //Doctor needs to set different info text
  protected void setSignInInfoText(String text) {
    mSignInInfoText.setText(text);
  }

  abstract protected void signIn(String username, String password);

  //for testing purposes
  protected void setDefaultSignIn(String username, String password) {
    mUserNameEditText.setText(username);
    mPasswordEditText.setText(password);
  }
}
