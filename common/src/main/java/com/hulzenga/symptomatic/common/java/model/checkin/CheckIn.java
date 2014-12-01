package com.hulzenga.symptomatic.common.java.model.checkin;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hulzenga.symptomatic.common.java.json.deserializer.MedicationDateMapDeserializer;
import com.hulzenga.symptomatic.common.java.json.deserializer.SymptomStateMapDeserializer;
import com.hulzenga.symptomatic.common.java.json.serializer.MedicationDateMapSerializer;
import com.hulzenga.symptomatic.common.java.json.serializer.SymptomStateMapSerializer;
import com.hulzenga.symptomatic.common.java.model.medication.Medication;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 * Created by jouke on 11/8/14.
 * (implements Serializable for Android save instance state, no other use intended)
 */
@Entity
public class CheckIn implements Serializable, Comparable<CheckIn>{

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private Date checkInDate;

  /**
   * Map of symptom ids to the observed symptom state ids
   */

  @JsonSerialize(using = SymptomStateMapSerializer.class)
  @JsonDeserialize(using = SymptomStateMapDeserializer.class)
  @ManyToMany
  private Map<Symptom, SymptomState> checkedSymptomStates;

  /**
   * Map of medication ids to the time it was taken (or null if it wasn't)
   */
  @JsonSerialize(using = MedicationDateMapSerializer.class)
  @JsonDeserialize(using = MedicationDateMapDeserializer.class)
  @ElementCollection
  private Map<Medication, Date> medicationsTaken;

  /** ---------------------------------------*
   *        JPA constructor methods          *
   *-----------------------------------------*/

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

  public Map<Symptom, SymptomState> getCheckedSymptomStates() {
    return checkedSymptomStates;
  }

  public void setCheckedSymptomStates(Map<Symptom, SymptomState> checkedSymptomStates) {
    this.checkedSymptomStates = checkedSymptomStates;
  }

  public Map<Medication, Date> getMedicationsTaken() {
    return medicationsTaken;
  }

  public void setMedicationsTaken(Map<Medication, Date> medicationsTaken) {
    this.medicationsTaken = medicationsTaken;
  }

  public CheckIn() {
  }

  public CheckIn(Date checkInDate, Map<Symptom, SymptomState> checkedSymptomStates, Map<Medication, Date> medicationsTaken) {
    this.checkInDate = checkInDate;
    this.checkedSymptomStates = checkedSymptomStates;
    this.medicationsTaken = medicationsTaken;
  }

  /**
   * sorts by date, with more recent dates having a "lower" value
   * @param another
   * @return
   */
  @Override
  public int compareTo(CheckIn another) {
    return Long.signum(another.getCheckInDate().getTime() - checkInDate.getTime());
  }
}
