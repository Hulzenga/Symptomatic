package com.hulzenga.symptomatic.server.controller;

import com.google.common.collect.Lists;
import com.hulzenga.symptomatic.common.java.model.checkin.CheckIn;
import com.hulzenga.symptomatic.common.java.model.checkin.Symptom;
import com.hulzenga.symptomatic.common.java.model.checkin.SymptomState;
import com.hulzenga.symptomatic.common.java.model.medication.Medication;
import com.hulzenga.symptomatic.server.repository.CheckInRepo;
import com.hulzenga.symptomatic.server.repository.MedicationRepo;
import com.hulzenga.symptomatic.server.repository.SymptomRepo;
import com.hulzenga.symptomatic.server.repository.SymptomStateRepo;
import com.hulzenga.symptomatic.server.security.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jouke on 11/28/14.
 */
@RestController
public class TestController {


  @Autowired
  private SymptomRepo symptomRepo;

  @Autowired
  private SymptomStateRepo symptomStateRepo;

  @Autowired
  private CheckInRepo checkInRepo;

  @Autowired
  private MedicationRepo medicationRepo;


  @RequestMapping(value = "/test/clear", method = RequestMethod.GET)
  public String clear() {
    checkInRepo.deleteAll();
    symptomRepo.deleteAll();
    symptomStateRepo.deleteAll();
    medicationRepo.deleteAll();
    return "clear";
  }

  @RequestMapping(value = "/test/pop", method = RequestMethod.GET)
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

    Medication lortab = new Medication("Lortab");
    Medication oxyContin = new Medication("OxyContin");

    medicationRepo.save(lortab);
    medicationRepo.save(oxyContin);

    Map<Symptom, SymptomState> smap = new HashMap<Symptom, SymptomState>();
    smap.put(mouthThroatPain, s1s2);
    smap.put(troubleEating, s2s3);

    Map<Medication, Date> mmap =  new HashMap<Medication, Date>();
    mmap.put(lortab, new Date(System.currentTimeMillis()));
    mmap.put(oxyContin, Medication.NOT_TAKEN);
    CheckIn test = new CheckIn(new Date(), smap, mmap);

    checkInRepo.save(test);
  }

  @RequestMapping(value = "/test/checkins", method = RequestMethod.GET)
  public List<CheckIn> getCheckins() {
    return checkInRepo.findAll();
  }

  @RequestMapping(value = "/test/medication", method = RequestMethod.GET)
  public List<Medication> getMedications() {
    return medicationRepo.findAll();
  }

  @RequestMapping(value = "/test/symptomstates", method = RequestMethod.GET)
  public List<SymptomState> getSymptomStates() {
    return symptomStateRepo.findAll();
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String helloWorld() {

    User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return "Hello Symptomatic World !";
  }
}
