package com.hulzenga.symptomatic.common.java.model.convenience;

import com.hulzenga.symptomatic.common.java.model.checkin.CheckIn;
import com.hulzenga.symptomatic.common.java.model.checkin.Symptom;
import com.hulzenga.symptomatic.common.java.model.checkin.SymptomState;
import com.hulzenga.symptomatic.common.java.model.medication.Medication;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jouke on 11/28/14.
 */
public class CheckInTransfer {

  public Date checkInDate;
  public Map<Long, Long> checkedSymptomStates  = new HashMap<Long, Long>();
  public Map<Long, Date> medicationsTaken  = new HashMap<Long, Date>();

  public CheckInTransfer(CheckIn checkIn) {

    checkInDate = checkIn.getCheckInDate();

    for (Map.Entry<Symptom, SymptomState> e: checkIn.getCheckedSymptomStates().entrySet()) {
      checkedSymptomStates.put(e.getKey().getId(), e.getValue().getId());
    }

    for (Map.Entry<Medication, Date> e: checkIn.getMedicationsTaken().entrySet()) {
      medicationsTaken.put(e.getKey().getId(), e.getValue());
    }
  }
}
