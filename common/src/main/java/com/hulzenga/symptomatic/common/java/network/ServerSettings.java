package com.hulzenga.symptomatic.common.java.network;

/**
 * Created by jouke on 11/11/14.
 */
public class ServerSettings {

  public static String SERVER_ADDRESS = "http://192.168.6.26:8080/";
  public static String SERVER_TOKEN_END_POINT = SERVER_ADDRESS + "oauth/token";

  //hard coded usernames
  public static String PATIENT_USERNAME = "Bob";
  public static String DOCTOR_USERNAME = "Alice";


  public static String PATIENT_CLIENT = "patient";
  public static String PATIENT_CLIENT_SECRET = "1234";

  public static String DOCTOR_CLIENT = "doctor";
  public static String DOCTOR_CLIENT_SECRET = "4321";

  public static int DOCTOR_TOKEN_TIME = 3600; //an hour
}
