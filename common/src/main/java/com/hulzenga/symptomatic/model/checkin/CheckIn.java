package com.hulzenga.symptomatic.model.checkin;

import java.util.Date;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by jouke on 11/8/14.
 */
@Entity
public class CheckIn {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private Date checkInDate;

  //TODO: not kosher, needs to be something more type safe ?
  /**
   * Map of symptom ids to the observed symptom state ids
   */
  @ElementCollection
  private Map<Long, Long> checkedSymptomStates;

  //private Map<Long, Date> medicationsAtDate;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Date getCheckInDate() {
    return checkInDate;
  }

  public void setCheckInDate(Date checkInDate) {
    this.checkInDate = checkInDate;
  }

  public Map<Long, Long> getCheckedSymptomStates() {
    return checkedSymptomStates;
  }

  public void setCheckedSymptomStates(Map<Long, Long> checkedSymptomStates) {
    this.checkedSymptomStates = checkedSymptomStates;
  }

  public CheckIn() {
  }

  public CheckIn(Date checkInDate, Map<Long, Long> checkedSymptomStates) {
    this.checkInDate = checkInDate;
    this.checkedSymptomStates = checkedSymptomStates;
  }
}
