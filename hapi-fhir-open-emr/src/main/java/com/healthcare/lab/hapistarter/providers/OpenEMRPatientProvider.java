package com.healthcare.lab.hapistarter.providers;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import com.healthcare.lab.hapistarter.domains.dto.PatientDTO;
import com.healthcare.lab.hapistarter.utils.RequestHelper;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpenEMRPatientProvider implements IResourceProvider {
    @Autowired
    private RequestHelper requestHelper;

    @Autowired
    private PatientDTO patientDTO;

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Patient.class;
    }

    @Read()
    public Patient read(@IdParam IdType theId) {

        if (theId != null) {
            JSONObject json = requestHelper.getResource("patient", theId.getIdPart());

            Patient pat = patientDTO.handleFormatPatient((JSONObject) json.get("data"));

            return pat;

        } else {
            throw new ResourceNotFoundException("Resource not found");
        }

    }

    private String handleCreatePatient(Patient patient) {

        JSONObject json = requestHelper.createResourceWithJSON("patient", patientDTO.formatToEMR(patient));

        JSONObject data = (JSONObject) json.get("data");

        return data.get("uuid").toString();
    }

    @Create()
    public MethodOutcome create(@ResourceParam Patient thePatient) {

        if (thePatient.getIdentifierFirstRep().isEmpty()) {
            throw new UnprocessableEntityException("No identifier supplied");
        }

        String createdUUID = handleCreatePatient(thePatient);

        MethodOutcome methodOutcome = new MethodOutcome();
        methodOutcome.setId(new IdType("Patient", createdUUID));
        methodOutcome.setCreated(true);

        return methodOutcome;

    }

    private JSONObject handleUpdatePatient(Patient thePatient, String resourceId) {
        JSONObject json = requestHelper.updateResourceWithJSON("patient", resourceId, patientDTO.formatToEMR(thePatient));

        JSONObject data = (JSONObject) json.get("data");

        return data;
    }

    @Update()
    public MethodOutcome update(@IdParam IdType theId, @ResourceParam Patient thePatient) {
        String resourceId;

        if (theId == null) {

            MethodOutcome methodOutcome = new MethodOutcome();
            OperationOutcome operationOutcome = new OperationOutcome();
            operationOutcome.addIssue().setDiagnostics("ID is missing or doesn't match!");
            methodOutcome.setOperationOutcome(operationOutcome);

            return methodOutcome;
        }

        resourceId = theId.getIdPart();

        JSONObject updatedPatient = handleUpdatePatient(thePatient, resourceId);

        MethodOutcome methodOutcome = new MethodOutcome();
        methodOutcome.setId(new IdType("patient", updatedPatient.get("uuid").toString()));

        return methodOutcome;

    }

    @Search()
    public List<Patient> searchByFamilyName(@RequiredParam(name=Patient.SP_FAMILY) StringParam theFamily) {
        String valueToMatch = theFamily.getValue();

        List<Patient> patientList = new ArrayList<Patient>();

        JSONObject json = requestHelper.getAll("patient");
        JSONArray data = (JSONArray) json.get("data");

        for ( int i = 0; i < data.size(); i++) {
            JSONObject patientJSON = (JSONObject) data.get(i);
            Patient patient = patientDTO.handleFormatPatient(patientJSON);

            if (patient.hasName()) {
                String patientFamilyName = patient.getName().get(0).getFamily();

                if (patientFamilyName.equals(valueToMatch)) {
                    patientList.add(patient);

                    patient.setId(patientJSON.get("uuid").toString());
                }
            }
        }

        return patientList;
    }
}
