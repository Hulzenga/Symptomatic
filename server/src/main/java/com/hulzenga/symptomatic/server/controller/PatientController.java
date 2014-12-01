package com.hulzenga.symptomatic.server.controller;

import com.hulzenga.symptomatic.common.java.api.PatientApi;
import com.hulzenga.symptomatic.common.java.model.checkin.CheckIn;
import com.hulzenga.symptomatic.common.java.model.checkin.Symptom;
import com.hulzenga.symptomatic.common.java.model.checkin.SymptomState;
import com.hulzenga.symptomatic.common.java.model.convenience.PatientData;
import com.hulzenga.symptomatic.common.java.model.medication.Medication;
import com.hulzenga.symptomatic.common.java.model.user.Doctor;
import com.hulzenga.symptomatic.common.java.model.user.Patient;
import com.hulzenga.symptomatic.common.java.network.ServerSettings;
import com.hulzenga.symptomatic.server.repository.CheckInRepo;
import com.hulzenga.symptomatic.server.repository.DoctorRepo;
import com.hulzenga.symptomatic.server.repository.MedicationRepo;
import com.hulzenga.symptomatic.server.repository.PatientRepo;
import com.hulzenga.symptomatic.server.repository.SymptomRepo;
import com.hulzenga.symptomatic.server.repository.SymptomStateRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by jouke on 11/4/14.
 */
@RestController
public class PatientController implements PatientApi {

  @Autowired
  private SymptomRepo symptomRepo;

  @Autowired
  private SymptomStateRepo symptomStateRepo;

  @Autowired
  private CheckInRepo checkInRepo;

  @Autowired
  private MedicationRepo medicationRepo;

  @Autowired
  private PatientRepo patientRepo;

  /**
   * Assumes username == patients first name and finds the first patient that matches
   *
   * @return the currently authenticated patient
   */
  private Patient getPatient() {
    return patientRepo.findByFirstName(SecurityContextHolder.getContext().getAuthentication().getName());
  }

  @Override
  @RequestMapping(value = PATIENT_CHECK_IN_PATH, method = RequestMethod.POST)
  public CheckIn checkIn(@RequestBody CheckIn checkIn) {
    //save the CheckIn
    CheckIn savedCheckIn = checkInRepo.save(checkIn);

    //update the patients checkIn
    Patient p = getPatient();
    p.getCheckIns().add(savedCheckIn);
    patientRepo.save(p);

    checkPatientForConcerns(p);
    return savedCheckIn;
  }

  @Override
  @RequestMapping(value = PATIENT_SYMPTOMS_PATH, method = RequestMethod.GET)
  public List<Symptom> getSymptoms() {
    return getPatient().getSymptoms();
  }

  @Override
  @RequestMapping(value = PATIENT_MEDICATIONS_PATH, method = RequestMethod.GET)
  public List<Medication> getMedications() {
    return getPatient().getMedications();
  }

  @Override
  @RequestMapping(value = PATIENT_DATA_PATH, method = RequestMethod.GET)
  public PatientData getPatientData() {
    return getPatient().getPatientData();
  }


  public void checkPatientForConcerns(Patient patient) {

    Set<SymptomState> possibleConcerns = new HashSet<SymptomState>();
    List<SymptomState> realConcerns = new ArrayList<SymptomState>();

    List<CheckIn> checkIns = patient.getCheckIns();
    if (checkIns.size() < 2) {
      return; //1 or 0 checkIn no possible concerns
    }

    //sort with latest first
    Collections.sort(checkIns);

    for (SymptomState ss : checkIns.get(0).getCheckedSymptomStates().values()) {
      if (ss.isCauseForConcern()) {
        possibleConcerns.add(ss);
      }
    }

    for (int i = 1; i < checkIns.size(); i++) {
      for (SymptomState ss : possibleConcerns) {
        if (!checkIns.get(i).getCheckedSymptomStates().values().contains(ss) || i == checkIns.size()-1) {
          //symptom started at last checkIn
          possibleConcerns.remove(ss);

          Long alertTime = ss.getAlertAfterXHours()*3600000l;
          Long symptomTime = checkIns.get(0).getCheckInDate().getTime() -
              ((i == checkIns.size() -1) ? checkIns.get(i) : checkIns.get(i-1)).getCheckInDate().getTime();

          if (symptomTime > alertTime) {
            realConcerns.add(ss);
          }
        }
      }

      if (possibleConcerns.size() == 0) {
        //no more concerns end the analysis
        break;
      }
    }

    //Set concern if there are any real concerns
    patient.setCauseForConcern(realConcerns.size() > 0);
    patient.setConcerningSymptoms(realConcerns);

    patientRepo.save(patient);

  }

  @Autowired
  private DoctorRepo doctorRepo;

  /**
   * TEST METHOD USED TO POPULATE THE DATABASE, Could be put in commandlinerunner but this way
   * is easier to code and provides a better testing environment (in combination with the
   * TestController methods
   */
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


    List<Symptom> symptoms = new ArrayList<Symptom>();
    symptoms.add(mouthThroatPain);
    symptoms.add(troubleEating);

    List<Medication> medications = new ArrayList<Medication>();
    medications.add(lortab);
    medications.add(oxyContin);

    //THE default test patient
    Patient bob = new Patient(ServerSettings.PATIENT_USERNAME, "Johnson", "04-04-1947", "AAB-47C-7",
        new ArrayList<CheckIn>(), symptoms, medications);
    patientRepo.save(bob);

    /*
     * TEST CHECK-IN ANSWERS
     */

    Map<Symptom, SymptomState> difficultyEatingSymptoms = new HashMap<Symptom, SymptomState>();
    difficultyEatingSymptoms.put(mouthThroatPain, s1s1);
    difficultyEatingSymptoms.put(troubleEating, s2s3);

    Map<Symptom, SymptomState> moderatePainSymptoms = new HashMap<Symptom, SymptomState>();
    moderatePainSymptoms.put(mouthThroatPain, s1s2);
    moderatePainSymptoms.put(troubleEating, s2s1);

    Map<Symptom, SymptomState> severePainSymptoms = new HashMap<Symptom, SymptomState>();
    severePainSymptoms.put(mouthThroatPain, s1s3);
    severePainSymptoms.put(troubleEating, s2s1);

    Map<Medication, Date> noMedicationsTaken = new HashMap<Medication, Date>();
    noMedicationsTaken.put(lortab, Medication.NOT_TAKEN);
    noMedicationsTaken.put(oxyContin, Medication.NOT_TAKEN);

    /*
     * PATIENT CAROL HAS 20 HOURS OF DIFFICULTY EATING -> CONCERN
     */
    List<CheckIn> carolCheckIns = new ArrayList<CheckIn>();

    CheckIn cc1 = new CheckIn(new Date(System.currentTimeMillis() - 20 * 3600000l), difficultyEatingSymptoms, noMedicationsTaken);
    CheckIn cc2 = new CheckIn(new Date(System.currentTimeMillis() - 12 * 3600000l), difficultyEatingSymptoms, noMedicationsTaken);
    CheckIn cc3 = new CheckIn(new Date(System.currentTimeMillis() - 4 * 3600000l), difficultyEatingSymptoms, noMedicationsTaken);

    cc1 = checkInRepo.save(cc1);
    cc2 = checkInRepo.save(cc2);
    cc3 = checkInRepo.save(cc3);

    carolCheckIns.add(cc1);
    carolCheckIns.add(cc2);
    carolCheckIns.add(cc3);

    Patient carol = new Patient("Carol", "Stevenson", "09-03-1952", "CQR-12A-0",
        carolCheckIns, symptoms, medications);
    patientRepo.save(carol);

    /*
     * PATIENT DAVE HAS 14 HOURS OF MODERATE PAIN -> NO CONCERN
     */
    List<CheckIn> daveCheckIns = new ArrayList<CheckIn>();

    CheckIn dc1 = new CheckIn(new Date(System.currentTimeMillis() - 14 * 3600000l), moderatePainSymptoms, noMedicationsTaken);
    CheckIn dc2 = new CheckIn(new Date(System.currentTimeMillis() - 10 * 3600000l), moderatePainSymptoms, noMedicationsTaken);
    CheckIn dc3 = new CheckIn(new Date(System.currentTimeMillis() - 0 * 3600000l), moderatePainSymptoms, noMedicationsTaken);

    dc1 = checkInRepo.save(dc1);
    dc2 = checkInRepo.save(dc2);
    dc3 = checkInRepo.save(dc3);

    daveCheckIns.add(dc1);
    daveCheckIns.add(dc2);
    daveCheckIns.add(dc3);

    Patient dave = new Patient("Dave", "Pinkerton", "03-11-1969", "ZKO-07D-4",
        daveCheckIns, symptoms, medications);
    patientRepo.save(dave);

    /*
     * PATIENT EGBERT HAS 15 HOURS OF SEVER PAIN -> CONCERN
     */

    List<CheckIn> egbertCheckIn = new ArrayList<CheckIn>();

    CheckIn ec1 = new CheckIn(new Date(System.currentTimeMillis() - 16 * 3600000l), severePainSymptoms, noMedicationsTaken);
    CheckIn ec2 = new CheckIn(new Date(System.currentTimeMillis() - 6 * 3600000l), severePainSymptoms, noMedicationsTaken);
    CheckIn ec3 = new CheckIn(new Date(System.currentTimeMillis() - 1 * 3600000l), severePainSymptoms, noMedicationsTaken);

    ec1 = checkInRepo.save(ec1);
    ec2 = checkInRepo.save(ec2);
    ec3 = checkInRepo.save(ec3);

    egbertCheckIn.add(ec1);
    egbertCheckIn.add(ec2);
    egbertCheckIn.add(ec3);

    Patient egbert = new Patient("Egbert", "de Vry", "02-07-1921", "AEP-12E-9",
        egbertCheckIn, symptoms, medications);
    patientRepo.save(egbert);

    /*
     * check all patients for concerns
     */
    checkPatientForConcerns(bob);
    checkPatientForConcerns(carol);
    checkPatientForConcerns(dave);
    checkPatientForConcerns(egbert);

    /*
     * DOCTOR ALICE HAS TO LOOK AFTER ALL OF THEM :( POOR ALICE
     */
    Doctor alice = new Doctor(ServerSettings.DOCTOR_USERNAME, "Cooper",
        Arrays.asList(new Patient[]{bob, carol, dave, egbert}));
    doctorRepo.save(alice);
  }
}
