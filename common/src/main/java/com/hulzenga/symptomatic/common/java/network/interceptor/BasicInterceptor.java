package com.hulzenga.symptomatic.common.java.network.interceptor;

import org.apache.commons.net.util.Base64;

import retrofit.RequestInterceptor;

/**
 * Created by jouke on 11/17/14.
 */
public class BasicInterceptor implements RequestInterceptor {

  private String user;
  private String password;
  private Base64 base64 = new Base64();

  public BasicInterceptor(String user, String password) {
    this.user = user;
    this.password = password;


  }

  @Override
  public void intercept(RequestFacade request) {

    request.addHeader("Authorization", String.valueOf(base64.encode("steve:pass".getBytes())));
  }
}
