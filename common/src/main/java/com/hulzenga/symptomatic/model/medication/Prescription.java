package com.hulzenga.symptomatic.model.medication;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by jouke on 11/4/14.
 */

@Entity
public class Prescription {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

//  private Medicine medicine;
//
//  private Doctor prescriber;
//
//  private Patient patient;
//
//  private Time prescriptionTime;
//  private Time stopTime;
}
