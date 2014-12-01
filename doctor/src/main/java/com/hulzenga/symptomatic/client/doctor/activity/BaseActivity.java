package com.hulzenga.symptomatic.client.doctor.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;

import com.hulzenga.symptomatic.client.doctor.R;
import com.hulzenga.symptomatic.client.doctor.service.TokenService;
import com.hulzenga.symptomatic.common.android.util.ControlFlow;

/**
 * Created by jouke on 11/30/14.
 */
public class BaseActivity extends Activity {

  // see BaseDoctorActivity - This violates DRY but cannot (?) be avoided due to
  // lack of multiple inheritance in Java
  protected TokenService mTokenService;
  protected boolean mBound = false;

  private ServiceConnection mConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      mTokenService = ((TokenService.TokenIBinder) service).getService();
      mBound = true;
      //if token service not connected return to sign in
      if (!mTokenService.isSignedIn()) {
        ControlFlow.redirect(BaseActivity.this, DoctorSignInActivity.class);
      }
      onTokenServiceConnected();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      mBound = false;
    }
  };

  protected void onTokenServiceConnected() {

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
      mTokenService.signOut();
      ControlFlow.redirect(this, DoctorSignInActivity.class);
    }

    return super.onOptionsItemSelected(item);
  }

}
