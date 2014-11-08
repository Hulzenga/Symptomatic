package com.hulzenga.symptomatic.server.controller;

import com.google.common.collect.Lists;
import com.hulzenga.symptomatic.api.PatientApi;
import com.hulzenga.symptomatic.model.checkin.CheckIn;
import com.hulzenga.symptomatic.model.checkin.Symptom;
import com.hulzenga.symptomatic.model.checkin.SymptomState;
import com.hulzenga.symptomatic.server.repository.CheckInRepo;
import com.hulzenga.symptomatic.server.repository.SymptomRepo;
import com.hulzenga.symptomatic.server.repository.SymptomStateRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jouke on 11/4/14.
 */
@RestController
public class PatientController implements PatientApi{

  @Autowired
  private SymptomRepo symptomRepo;

  @Autowired
  private SymptomStateRepo symptomStateRepo;

  @Autowired
  private CheckInRepo checkInRepo;

  @RequestMapping(value = "/clear", method = RequestMethod.GET)
  public String clear() {
    symptomRepo.deleteAll();
    return "clear";
  }

  @RequestMapping(value = "/pop", method = RequestMethod.GET)
  public void populate() {

    SymptomState s1s1 = new SymptomState("well-controlled", 0.0f);
    SymptomState s1s2 = new SymptomState("moderate", 0.5f, true, 16);
    SymptomState s1s3 = new SymptomState("severe", 1.0f, true, 12);

    symptomStateRepo.save(s1s1);
    symptomStateRepo.save(s1s2);
    symptomStateRepo.save(s1s3);

    Symptom mouthThroatPain = new Symptom(
        "Mouth/Throat Pain", "How bad is your mouth pain/sore throat?",
        new ArrayList<SymptomState>(Arrays.asList(
            s1s1, s1s2, s1s3
        )));

    symptomRepo.save(mouthThroatPain);

    SymptomState s2s1 = new SymptomState("no", 0.0f);
    SymptomState s2s2 = new SymptomState("some", 0.5f);
    SymptomState s2s3 = new SymptomState("I can't eat", 1.0f, true, 12);

    symptomStateRepo.save(s2s1);
    symptomStateRepo.save(s2s2);
    symptomStateRepo.save(s2s3);

    Symptom troubleEating = new Symptom(
        "Trouble Eating", "Does your pain stop you from eating/drinking?",
        new ArrayList<SymptomState>(Arrays.asList(
            s2s1, s2s2, s2s3
        )));

    symptomRepo.save(troubleEating);

  }

  @Override
  @RequestMapping(value = "/checkin", method = RequestMethod.POST)
  public CheckIn checkIn(@RequestBody CheckIn checkIn) {
    return checkInRepo.save(checkIn);
  }

  @RequestMapping(value = "/checkins", method = RequestMethod.GET)
  public List<CheckIn> getCheckins() {
    return Lists.newArrayList(checkInRepo.findAll());
  }

  @Override
  @RequestMapping(value = "/symptoms", method = RequestMethod.GET)
  public List<Symptom> getSymptoms() {
    return Lists.newArrayList(symptomRepo.findAll());
  }

  @RequestMapping(value = "/symptomstates", method = RequestMethod.GET)
  public List<SymptomState> getSymptomStates() {
    return Lists.newArrayList(symptomStateRepo.findAll());
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String helloWorld() {
    return "Hello World !";
  }
}
