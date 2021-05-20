package com.heathcare.lab.hapistarter.providers;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.heathcare.lab.hapistarter.entity.PatientEntity;
import com.heathcare.lab.hapistarter.entity.PersonNameEntity;
import com.heathcare.lab.hapistarter.repositories.PatientRepo;
import lombok.AllArgsConstructor;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.Identifier.IdentifierUse;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class PatientResourceProvider implements IResourceProvider {

  @Autowired
  private PatientRepo patientRepo;

  /**
   * The getResourceType method comes from IResourceProvider, and must
   * be overridden to indicate what type of resource this provider
   * supplies.
   */
  @Override
  public Class<Patient> getResourceType() {
    return Patient.class;
  }

  /**
   * The "@Read" annotation indicates that this method supports the
   * read operation. Read operations should return a single resource
   * instance.
   *
   * @param theId
   *    The read operation takes one parameter, which must be of type
   *    IdType and must be annotated with the "@Read.IdParam" annotation.
   * @return
   *    Returns a resource matching this identifier, or null if none exists.
   */
  @Read
  public Patient getResourceById(@IdParam IdType theId) {

    if (patientRepo.findByPatientId(theId.getId()).isPresent()) {
      return null;
    }

    var pa = patientRepo.findByPatientId(theId.getId()).get();

    var patient = new Patient();
    patient.addIdentifier().setId(theId.getId());
    patient.getIdentifier().get(0).setSystem("urn:hapitest:mrns");
    patient.getIdentifier().get(0).setValue("00002");
    patient.addName().setFamily(pa.getPatientName().getLastName());
    patient.getName().get(0).setUse(HumanName.NameUse.OFFICIAL);
    patient.getName().get(0).setText(pa.getPatientName().getFullName());
    patient.getName().get(0).addGiven(pa.getPatientName().getFirstName());
    if (pa.getPatientSex().equals("MALE")) {
      patient.setGender(AdministrativeGender.MALE);
    } else {
      patient.setGender(AdministrativeGender.FEMALE);
    }
    patient.setBirthDate(pa.getPatientBirthDate());
    return patient;
  }

  /**
   * The "@Create" annotation indicates that this method implements "create=type", which adds a
   * new instance of a resource to the server.
   */
  @Create
  public MethodOutcome createPatient(@ResourceParam Patient thePatient) {
    var pat = PatientEntity.builder()
            .patientSex(thePatient.getGender().toCode())
            .patientName(PersonNameEntity.builder()
                    .firstName(thePatient.getName().get(0).getGivenAsSingleString())
                    .lastName(thePatient.getName().get(0).getFamily())
                    .build())
            .patientBirthDate(thePatient.getBirthDate())
            .build();
    var result = patientRepo.save(pat);
    return new MethodOutcome(new IdType(pat.getPatientId()), true);
  }


  /**
   * The "@Search" annotation indicates that this method supports the
   * search operation. You may have many different methods annotated with
   * this annotation, to support many different search criteria. This
   * example searches by family name.
   *
   * @param theFamilyName
   *    This operation takes one parameter which is the search criteria. It is
   *    annotated with the "@Required" annotation. This annotation takes one argument,
   *    a string containing the name of the search criteria. The datatype here
   *    is StringParam, but there are other possible parameter types depending on the
   *    specific search criteria.
   * @return
   *    This method returns a list of Patients. This list may contain multiple
   *    matching resources, or it may also be empty.
   */
  @Search
  public List<Patient> getPatient(
    @RequiredParam(name = Patient.SP_FAMILY) StringParam theFamilyName
  ) {
    var patient = new Patient();
    patient.addIdentifier();
    patient.getIdentifier().get(0).setUse(IdentifierUse.OFFICIAL);
    patient.getIdentifier().get(0).setSystem("urn:hapitest:mrns");
    patient.getIdentifier().get(0).setValue("00001");
    patient.addName();
    patient.getName().get(0).setFamily(theFamilyName.getValue());
    patient.getName().get(0).addGiven("PatientOne");
    patient.setGender(AdministrativeGender.MALE);
    return Collections.singletonList(patient);
  }
}
