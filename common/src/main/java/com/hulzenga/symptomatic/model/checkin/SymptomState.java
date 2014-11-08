package com.hulzenga.symptomatic.model.checkin;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by jouke on 11/7/14.
 */
@Entity
public class SymptomState {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private String state;

  //state enumerates this symptom
  @JsonIgnore
  @ManyToOne
  private Symptom symptom;

  private float severity;

  private boolean causeForConcern;

  private int alertAfterXHours;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public Symptom getSymptom() {
    return symptom;
  }

  public void setSymptom(Symptom symptom) {
    this.symptom = symptom;
  }

  public float getSeverity() {
    return severity;
  }

  public void setSeverity(float severity) {
    this.severity = severity;
  }

  public boolean isCauseForConcern() {
    return causeForConcern;
  }

  public void setCauseForConcern(boolean causeForConcern) {
    this.causeForConcern = causeForConcern;
  }

  public int getAlertAfterXHours() {
    return alertAfterXHours;
  }

  public void setAlertAfterXHours(int alertAfterXHours) {
    this.alertAfterXHours = alertAfterXHours;
  }

  public SymptomState() {
  }

  public SymptomState(String state, float severity) {
    this(state, severity, false, 0);
  }

  public SymptomState(String state, float severity, boolean causeForConcern, int alertAfterXHours) {
    this.state = state;
    this.severity = severity;
    this.causeForConcern = causeForConcern;
    this.alertAfterXHours = alertAfterXHours;
  }

}
