package com.hulzenga.symptomatic.common.java.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hulzenga.symptomatic.common.java.api.DoctorApi;
import com.hulzenga.symptomatic.common.java.api.PatientApi;
import com.hulzenga.symptomatic.common.java.json.SymptomaticModule;
import com.hulzenga.symptomatic.common.java.network.client.UnsafeHttpsClient;
import com.hulzenga.symptomatic.common.java.network.interceptor.OAuthInterceptor;

import org.apache.commons.net.util.Base64;
import org.apache.oltu.oauth2.client.HttpClient;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthClientResponse;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import java.util.Map;

import retrofit.RestAdapter;
import retrofit.client.ApacheClient;
import retrofit.converter.JacksonConverter;

/**
 * Created by jouke on 11/28/14.
 */
public class APIFactory {

  private static final String TAG = APIFactory.class.getSimpleName();

  /**
   * Signs into the server and retrieves the appropriate bearer token
   *
   * @param clientId
   * @param clientSecret
   * @param username
   * @param password
   * @return OAuth 2.0 Bearer token
   */
  public static String signIn(String clientId, String clientSecret, String username, String password) {
    try {
      OAuthClientRequest request = OAuthClientRequest
          .tokenLocation(ServerSettings.SERVER_TOKEN_END_POINT)
          .setGrantType(GrantType.PASSWORD)
          //.setClientId(clientId)
          //.setClientSecret(clientSecret)
          .setUsername(username)
          .setPassword(password)
          .buildQueryMessage();

      //Do Basic Authentication
      Base64 base64 = new Base64();
      String base64Auth = base64.encodeToString(
          new String(clientId + ":" + clientSecret).getBytes())
          .replaceAll("[\n\r]", ""); //strip off carriage return and newline character
      request.setHeader("Authorization", "Basic " + base64Auth);

      OAuthClient client = new OAuthClient(new URLConnectionClient());
      OAuthJSONAccessTokenResponse tokenResponse = client.accessToken(request);

      return tokenResponse.getAccessToken();

    } catch (OAuthSystemException e) {
      return null;
    } catch (Exception e) {
      return null;
    }
  }

  public static class SignInException extends RuntimeException {

    public SignInException(String message) {
      super(message);
    }

  }

  public static PatientApi makePatientAPI(String token) {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new SymptomaticModule());

    OAuthInterceptor interceptor = new OAuthInterceptor(token);

    return new RestAdapter.Builder()
        //.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
        .setEndpoint(ServerSettings.SERVER_ADDRESS)
        .setRequestInterceptor(interceptor)
        .setConverter(new JacksonConverter(mapper))
        .build().create(PatientApi.class);
  }

  public static DoctorApi makeDoctorApi(String token) {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new SymptomaticModule());

    OAuthInterceptor interceptor = new OAuthInterceptor(token);

    return new RestAdapter.Builder()
        //.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
        .setEndpoint(ServerSettings.SERVER_ADDRESS)
        .setRequestInterceptor(interceptor)
        .setConverter(new JacksonConverter(mapper))
        .build().create(DoctorApi.class);
  }
}
