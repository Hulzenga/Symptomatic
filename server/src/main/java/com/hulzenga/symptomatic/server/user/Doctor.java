package com.hulzenga.symptomatic.server.user;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by jouke on 11/4/14.
 */

@Entity
public class Doctor {

  @Id
  private long id;
  private String firstName;
  private String lastName;

}
