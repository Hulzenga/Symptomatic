package com.hulzenga.symptomatic.common.java.model.user;

import com.hulzenga.symptomatic.common.java.model.checkin.CheckIn;
import com.hulzenga.symptomatic.common.java.model.checkin.Symptom;
import com.hulzenga.symptomatic.common.java.model.checkin.SymptomState;
import com.hulzenga.symptomatic.common.java.model.medication.Medication;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by jouke on 11/4/14.
 */
@Entity
public class Patient {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private String firstName;
  private String lastName;

  @ElementCollection
  private List<CheckIn> checkIns;

  @ElementCollection
  private List<Symptom> symptoms;

  @ElementCollection
  private List<Medication> medications;

}
