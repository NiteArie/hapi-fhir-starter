package com.heathcare.lab.hapistarter.repositories.transform;

import com.heathcare.lab.hapistarter.entity.Name;
import com.heathcare.lab.hapistarter.entity.PatientEntity;
import com.heathcare.lab.hapistarter.entity.Telecom;
import org.apache.commons.collections4.Transformer;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Component;

@Component
public class FHIRPatientToPatientEntity implements Transformer<Patient, PatientEntity> {

    @Override
    public PatientEntity transform(Patient patient) {
        final PatientEntity patientEntity = new PatientEntity();

        for (Identifier identifier : patient.getIdentifier()) {
            com.heathcare.lab.hapistarter.entity.Identifier identifierE = new com.heathcare.lab.hapistarter.entity.Identifier();
            identifierE.setSystem(identifier.getSystem());
            identifierE.setValue(identifier.getValue().replaceAll(" ",""));

            patientEntity.getIdentifiers().add(identifierE);
            patientEntity.addToIdentifer(identifierE);
        }
        for (HumanName name : patient.getName()) {
            Name nameE = new Name();
            nameE.setFamilyName(name.getFamily());
            nameE.setGivenName(name.getGivenAsSingleString());
            if (name.hasPrefix()) {
                nameE.setPrefix(name.getPrefix().get(0).getValue());
            }
            nameE.setUse(name.getUse().toString());

            patientEntity.getNames().add(nameE);
            patientEntity.addToName(nameE);
        }
        if (patient.hasBirthDate()) {
            patientEntity.setDateOfBirth(patient.getBirthDate());
        }
        if (patient.hasGender()) {
            patientEntity.setGender(patient.getGender());
        }
        for (ContactPoint contactPoint : patient.getTelecom()) {
            Telecom telecom = new Telecom();
            telecom.setValue(contactPoint.getValue());
            if (contactPoint.hasSystem()) {
                telecom.setSystem(contactPoint.getSystem());
            }
            if (contactPoint.hasUse()) {
                telecom.setUse(contactPoint.getUse());
            }

            patientEntity.getTelecoms().add(telecom);
            patientEntity.addToTelecom(telecom);
        }
        for (Address address : patient.getAddress()) {
            com.heathcare.lab.hapistarter.entity.Address addressEntity = new com.heathcare.lab.hapistarter.entity.Address();

            addressEntity.setLine(address.getLine().toString());

            if (address.hasCity()) {
                addressEntity.setCity(address.getCity());
            }
            if (address.hasPostalCode()) {
                addressEntity.setPostalCode(address.getPostalCode());
            }
            if (address.hasCountry()) {
                addressEntity.setCountry(address.getCountry());
            }
            if (address.hasState()) {
                addressEntity.setState(address.getState());
            }

            patientEntity.getAddresses().add(addressEntity);
            patientEntity.addToAddress(addressEntity);
        }
        return patientEntity;
    }
}
