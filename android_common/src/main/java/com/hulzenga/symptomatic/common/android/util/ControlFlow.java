package com.hulzenga.symptomatic.common.android.util;

import android.content.Context;
import android.content.Intent;

/**
 * Created by jouke on 11/30/14.
 */
public class ControlFlow {

  public static void redirect(Context context, Class targetActivity) {
    Intent intent = new Intent(context, targetActivity);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }
}
