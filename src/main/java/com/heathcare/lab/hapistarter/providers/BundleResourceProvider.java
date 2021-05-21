package com.heathcare.lab.hapistarter.providers;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import com.heathcare.lab.hapistarter.dao.PatientDao;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class BundleResourceProvider implements IResourceProvider {

    @Autowired
    private PatientDao patientDao;

    @Autowired
    private FhirContext ctx;

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Bundle.class;
    }

    @Create()
    public MethodOutcome create(HttpServletRequest theRequest, @ResourceParam Bundle bundle) {

        MethodOutcome method = new MethodOutcome();
        method.setCreated(true);
        OperationOutcome opOutcome = new OperationOutcome();

        method.setOperationOutcome(opOutcome);

        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
            if (entry.getResource() instanceof Patient) {
                try {
                    Patient myPatient = patientDao.create(ctx,(Patient) entry.getResource());
                    method.setId(myPatient.getIdElement());
                    method.setResource(myPatient);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        return method;
    }
}
