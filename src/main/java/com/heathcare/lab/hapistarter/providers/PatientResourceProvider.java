package com.heathcare.lab.hapistarter.providers;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import com.heathcare.lab.hapistarter.dao.IPatient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class PatientResourceProvider implements IResourceProvider {

  @Autowired
  private FhirContext ctx;

  @Autowired
  private IPatient patientDao;

  private static final Logger log = LoggerFactory.getLogger(PatientResourceProvider.class);

  @Override
  public Class<? extends IBaseResource> getResourceType() {
    return Patient.class;
  }

  @Create()
  public MethodOutcome createPatient(HttpServletRequest theRequest, @ResourceParam Patient patient) {
    log.debug("Create Patient Provider called");

    MethodOutcome method = new MethodOutcome();
    method.setCreated(true);
    OperationOutcome opOutcome = new OperationOutcome();

    method.setOperationOutcome(opOutcome);

    try {
      Patient myPatient = patientDao.create(ctx, patient);
      log.info(myPatient.getIdElement().toString());
      method.setId(myPatient.getIdElement());
      method.setResource(myPatient);
    } catch (Exception ex) {
      log.error(ex.getMessage());
    }

    log.debug("called create Patient method");

    return method;
  }


  @Read
  public Patient readPatient(HttpServletRequest request, @IdParam IdType internalId) {
    Patient patient = patientDao.read(ctx, internalId);
    return patient;
  }

  @Search
  public List<Resource> searchByDob(HttpServletRequest request, @OptionalParam(name= Patient.SP_BIRTHDATE) DateParam birthDate) {
    List<Resource> results = patientDao.searchByDob(ctx,birthDate);
    return results;
  }

  @Search
  public List<Resource> searchByFamilyName(HttpServletRequest request, @RequiredParam(name = Patient.SP_FAMILY) StringParam familyName) {
    List<Resource> results = patientDao.searchByFamilyName(ctx, familyName.getValue());
    return results;
  }

  @Search
  public List<Resource> searchByGivenName(HttpServletRequest request, @RequiredParam(name = Patient.SP_GIVEN) StringParam givenName) {
    List<Resource> results = patientDao.searchByGivenName(ctx, givenName.getValue());
    return results;
  }
}
