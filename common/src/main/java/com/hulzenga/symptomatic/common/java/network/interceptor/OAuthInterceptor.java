package com.hulzenga.symptomatic.common.java.network.interceptor;

import retrofit.RequestInterceptor;

/**
 * Created by jouke on 11/15/14.
 */
public class OAuthInterceptor implements RequestInterceptor {

  private String accessToken;

  public OAuthInterceptor(String accessToken) {
    super();
    this.accessToken = accessToken;
  }

  @Override
  public void intercept(RequestFacade request) {
    request.addHeader("Authorization", "Bearer " + accessToken);
  }
}

