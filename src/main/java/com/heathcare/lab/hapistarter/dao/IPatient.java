package com.heathcare.lab.hapistarter.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.param.DateParam;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;

import java.util.List;

public interface IPatient {

    Patient create(FhirContext ctx, Patient patient);

    Patient read(FhirContext ctx, IdType theId);

    List<Resource> searchByDob (FhirContext ctx, @OptionalParam(name= Patient.SP_BIRTHDATE) DateParam birthDate);

    List<Resource> searchByFamilyName(FhirContext ctx, @RequiredParam(name = Patient.SP_FAMILY) String familyName);

    List<Resource> searchByGivenName(FhirContext ctx, @RequiredParam(name = Patient.SP_GIVEN) String givenName);


}
