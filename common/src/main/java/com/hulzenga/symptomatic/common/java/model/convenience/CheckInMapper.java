package com.hulzenga.symptomatic.common.java.model.convenience;

import com.hulzenga.symptomatic.common.java.model.checkin.Symptom;
import com.hulzenga.symptomatic.common.java.model.checkin.SymptomState;
import com.hulzenga.symptomatic.common.java.model.medication.Medication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jouke on 12/1/14.
 */
public class CheckInMapper {

  private Map<Long, String> medicationMap = new HashMap<Long, String>();
  private Map<Long, String> symptomMap = new HashMap<Long, String>();
  private Map<Long, String> symptomStateMap = new HashMap<Long, String>();
  private List<Medication> possibleMedications;
  private List<Symptom> possibleSymptoms;

  public CheckInMapper(List<Medication> possibleMedications, List<Symptom> possibleSymptoms) {
    this.possibleMedications = possibleMedications;
    this.possibleSymptoms = possibleSymptoms;
  }

  public String getMedicationNameFromId(long id) {
    if (medicationMap.containsKey(id)) {
      return medicationMap.get(id);
    } else {
      for (Medication m: possibleMedications)  {
        if (m.getId() == id) {
          medicationMap.put(id, m.getName());
          return medicationMap.get(id);
        }
      }
    }

    return "NOT FOUND";
  }
  public String getSymptomFromId(long id) {
    if (symptomMap.containsKey(id)) {
      return symptomMap.get(id);
    } else {
      for (Symptom s: possibleSymptoms)  {
        if (s.getId() == id) {
          symptomMap.put(id, s.getDescriptor());
          return symptomMap.get(id);
        }
      }
    }

    return "NOT FOUND";
  }
  public String getSymptomStateFromId(long id) {
    if (symptomStateMap.containsKey(id)) {
      return symptomStateMap.get(id);
    } else {
      for (Symptom s: possibleSymptoms)  {
        for (SymptomState ss: s.getStates()) {
          if (ss.getId() == id) {
            symptomStateMap.put(id, ss.getState());
            return symptomStateMap.get(id);
          }
        }
      }
    }

    return "NOT FOUND";
  }
}
