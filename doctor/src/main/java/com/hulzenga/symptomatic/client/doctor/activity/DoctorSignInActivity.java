package com.hulzenga.symptomatic.client.doctor.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.hulzenga.symptomatic.client.doctor.service.TokenService;
import com.hulzenga.symptomatic.common.android.activity.SignInActivity;
import com.hulzenga.symptomatic.common.android.util.ControlFlow;

public class DoctorSignInActivity extends SignInActivity {

  // see BaseDoctorActivity - This violates DRY but cannot (?) be avoided due to
  // lack of multiple inheritance in Java
  private TokenService mTokenService;
  private boolean mBound = false;

  private ServiceConnection mConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      mTokenService = ((TokenService.TokenIBinder) service).getService();
      mBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      mBound = false;
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setDefaultSignIn("Alice", "Alice");
    setSignInInfoText("");
  }


  @Override
  protected void onStart() {
    super.onStart();
    Intent i = new Intent(this, TokenService.class);
    bindService(i, mConnection, Context.BIND_AUTO_CREATE);
  }

  @Override
  protected void onStop() {
    super.onStop();

    if (mBound) {
      unbindService(mConnection);
      mBound = false;
    }
  }

  @Override
  protected void signIn(String username, String password) {
    mTokenService.signIn(username, password, new Runnable() {
      //Failure
      @Override
      public void run() {
        Toast.makeText(DoctorSignInActivity.this, "Failed to Sign in", Toast.LENGTH_SHORT).show();
      }
    }, new Runnable() {
      //Success
      @Override
      public void run() {
        ControlFlow.redirect(DoctorSignInActivity.this, PatientMonitorActivity.class);
      }
    });
  }

}
