package com.hulzenga.symptomatic.common.java.model.checkin;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Created by jouke on 11/4/14.
 */
@Entity
public class Symptom implements Serializable{

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private String descriptor;
  private String question;

  @OneToMany
  private List<SymptomState> states;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getDescriptor() {
    return descriptor;
  }

  public void setDescriptor(String descriptor) {
    this.descriptor = descriptor;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public List<SymptomState> getStates() {
    return states;
  }

  public void setStates(List<SymptomState> states) {
    this.states = states;
  }

  public Symptom(){};

  public Symptom(String descriptor, String question, List<SymptomState> states) {
    this.descriptor = descriptor;
    this.question = question;
    this.states = states;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Symptom)) return false;

    Symptom symptom = (Symptom) o;

    if (id != symptom.id) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return (int) (id ^ (id >>> 32));
  }
}
