package com.healthcare.lab.hapistarter.providers;

import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import com.healthcare.lab.hapistarter.domains.dto.ScheduleDTO;
import com.healthcare.lab.hapistarter.utils.RequestHelper;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Schedule;
import org.json.simple.JSONObject;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class OpenEMRScheduleProvider implements IResourceProvider {
    @Autowired
    private RequestHelper requestHelper;

    @Autowired
    private ScheduleDTO scheduleDTO;

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Schedule.class;
    }

    @Create
    public MethodOutcome create(@ResourceParam Schedule theSchedule) {

        if (theSchedule.getIdentifierFirstRep().isEmpty()) {
            throw new UnprocessableEntityException("No identifier supplied");
        }

        String patientReference = theSchedule.getActorFirstRep().getReference();
        String[] temp = patientReference.split("/");

        String patientID = temp[1];

        JSONObject json = requestHelper.createResourceOnAResourceWithJSON(
                "patient",
                patientID,
                "appointment",
                scheduleDTO.handleFormatSchedule(theSchedule, patientID)
        );

        MethodOutcome methodOutcome = new MethodOutcome();

        return methodOutcome;

    }
}
