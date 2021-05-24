package com.heathcare.lab.hapistarter.domains.dto;

import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Schedule;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class ScheduleDTO {

    public JSONObject handleFormatSchedule(Schedule theSchedule, String patientID) {

        Map map = new HashMap();

        // NEED TO FIND OUT WHAT EID IS
        map.put("pc_eid", 3);

        for (CodeableConcept innerCategory : theSchedule.getServiceCategory()) {
            String code = innerCategory.getCodingFirstRep().getCode();

            map.put("pc_catid", code);
        }

        Date startDate = theSchedule.getPlanningHorizon().getStart();
        Date endDate = theSchedule.getPlanningHorizon().getEnd();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String eventDate = dateFormat.format(startDate);

        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String startTime = timeFormat.format(startDate);

        map.put("pc_eventDate", eventDate);
        map.put("pc_startTime", startTime);

        long seconds = (endDate.getTime() - startDate.getTime()) / 1000;

        map.put("pc_duration", seconds);
        map.put("pc_title", "Appointment with " + theSchedule.getActorFirstRep().getDisplay());
        map.put("pc_hometext", theSchedule.getComment());
        map.put("pc_apptstatus", "-");
        map.put("pc_facility", 0);
        map.put("pc_billing_location", 0);

        return new JSONObject(map);

    }
}
