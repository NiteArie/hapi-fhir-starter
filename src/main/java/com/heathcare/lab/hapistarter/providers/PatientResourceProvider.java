package com.heathcare.lab.hapistarter.providers;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import com.heathcare.lab.hapistarter.domain.entities.PatientEntity;
import com.heathcare.lab.hapistarter.domain.transform.FHIRPatientToPatientEntity;
import com.heathcare.lab.hapistarter.domain.transform.PatientEntityToFHIRPatient;
import com.heathcare.lab.hapistarter.repositories.PatientRepository;
import org.hl7.fhir.r4.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PatientResourceProvider implements IResourceProvider {

  @Autowired
  private PatientRepository patientRepository;

  @Autowired
  private PatientEntityToFHIRPatient patientEntityToFHIRPatient;

  @Autowired
  private FHIRPatientToPatientEntity fhirPatientToPatientEntity;

  private static final Logger log = LoggerFactory.getLogger(PatientResourceProvider.class);

  @Override
  public Class<Patient> getResourceType() {
      return Patient.class;
  }

  @Create()
  public MethodOutcome createPatient(@ResourceParam Patient patient) {
      PatientEntity patientEntity = fhirPatientToPatientEntity.transform(patient);
      var result = patientRepository.save(patientEntity);
      return new MethodOutcome(new IdType(patientEntity.getId()), true);
  }


  @Read
  public Patient getPatientById(@IdParam IdType internalId) {
      Long id = Long.valueOf(internalId.getIdPart());
      PatientEntity patientEntity = new PatientEntity();
      Optional<PatientEntity> optional = patientRepository.findById(id);
        if (optional.isPresent()) {
            patientEntity = optional.get();
        }
      return patientEntityToFHIRPatient.transform(patientEntity);
  }

  @Search
  public List<Patient> searchByDob(@RequiredParam(name= Patient.SP_BIRTHDATE) DateParam birthDate) {
      return patientRepository.findByDateOfBirth(birthDate.getValue())
                              .stream()
                              .map(item -> patientEntityToFHIRPatient.transform(item))
                              .collect(Collectors.toList());
  }


  @Search
  public List<Resource> searchByFamilyName(@RequiredParam(name = Patient.SP_FAMILY) StringParam familyName) {
      return patientRepository.findByFamilyName(familyName.getValue())
                              .stream()
                              .map(item -> patientEntityToFHIRPatient.transform(item))
                              .collect(Collectors.toList());
  }

  @Search
  public List<Resource> searchByGivenName(@RequiredParam(name = Patient.SP_GIVEN) StringParam givenName) {
    return patientRepository.findByGivenName(givenName.getValue())
                            .stream()
                            .map(item -> patientEntityToFHIRPatient.transform(item))
                            .collect(Collectors.toList());
  }
}
