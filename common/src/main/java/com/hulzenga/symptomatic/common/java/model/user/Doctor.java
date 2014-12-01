package com.hulzenga.symptomatic.common.java.model.user;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 * Created by jouke on 11/4/14.
 */

@Entity
public class Doctor {

  @Id
  private long id;

  private String firstName;
  private String lastName;

  @ManyToMany
  private List<Patient> patients;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public List<Patient> getPatients() {
    return patients;
  }

  public void setPatients(List<Patient> patients) {
    this.patients = patients;
  }

  public Doctor() {
  }

  public Doctor(String firstName, String lastName, List<Patient> patients) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.patients = patients;
  }
}
