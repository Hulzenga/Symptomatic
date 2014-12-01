package com.hulzenga.symptomatic.client.doctor.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.hulzenga.symptomatic.common.java.api.DoctorApi;
import com.hulzenga.symptomatic.common.java.network.APIFactory;
import com.hulzenga.symptomatic.common.java.network.ServerSettings;

import java.util.Date;

public class TokenService extends Service {

  private static final String TAG = TokenService.class.getSimpleName();

  private final IBinder mBinder = new TokenIBinder();
  private String token;
  private Date validUntil; //not used but should be

  public class TokenIBinder extends Binder {
    public TokenService getService() {
      return TokenService.this;
    }
  }

  public boolean isSignedIn() {
    return (token != null);
  }

  @Override
  public IBinder onBind(Intent intent) {
    return mBinder;
  }

  @Override
  public boolean onUnbind(Intent intent) {
    signOut();
    return super.onUnbind(intent);
  }

  public void signIn(final String username, final String password,
                     final Runnable failureCallback,
                     final Runnable successCallback) {
    new AsyncTask<Void, Void, Boolean>() {

      @Override
      protected Boolean doInBackground(Void... params) {
        String tokenResponse = APIFactory.signIn(ServerSettings.DOCTOR_CLIENT,
            ServerSettings.DOCTOR_CLIENT_SECRET, username, password);

        if (tokenResponse != null) {
          token = tokenResponse;
          Log.d(TAG, "acquired token");
          return true;
        } else {
          return false;
        }
      }

      @Override
      protected void onPostExecute(Boolean success) {
        if (!success) {
          Log.d(TAG, "Invalid Username and/or Password");
          failureCallback.run();
          return; //done
        } else {
          //success
          successCallback.run();
        }
      }
    }.execute();
  }

  public void signOut() {
    token = null;
    validUntil = null;
  }

  private DoctorApi doctorApi;

  public DoctorApi getDoctorApi() {
    if (!isSignedIn()) {
      Log.e(TAG, "getDoctorApi called without being signed in ");
      return null;
    }

    if (doctorApi == null) {
      doctorApi = APIFactory.makeDoctorApi(token);
    }

    return doctorApi;
  }
}
