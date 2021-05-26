package com.healthcare.lab.hapistarter.domains.dto;

import org.hl7.fhir.r4.model.*;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Component
public class PatientDTO {

    public Patient handleFormatPatient(JSONObject patientData) {

        Patient pat = new Patient();

        pat.addName();
        pat.getName().get(0).setUse(HumanName.NameUse.OFFICIAL);
        pat.getName().get(0).setFamily(patientData.get("lname").toString());
        pat.getName().get(0).addGiven(patientData.get("fname").toString());

        pat.addTelecom();
        pat.getTelecom().get(0).setSystem(ContactPoint.ContactPointSystem.EMAIL);
        pat.getTelecom().get(0).setValue(patientData.get("email").toString());

        if (patientData.get("sex").toString().equals("Male")) {
            pat.setGender(Enumerations.AdministrativeGender.MALE);
        } else {
            pat.setGender(Enumerations.AdministrativeGender.FEMALE);
        }

        pat.addAddress();
        pat.getAddress().get(0).addLine(patientData.get("street").toString());
        pat.getAddress().get(0).setCity(patientData.get("city").toString());
        pat.getAddress().get(0).setState(patientData.get("state").toString());
        pat.getAddress().get(0).setPostalCode(patientData.get("postal_code").toString());
        pat.getAddress().get(0).setCountry(patientData.get("country_code").toString());

        return pat;
    }

    public JSONObject formatToEMR(Patient patient) {
        Map map = new HashMap();

        for (HumanName humanName : patient.getName()) {
            map.put("lname", humanName.getFamily());
            map.put("fname", humanName.getGivenAsSingleString());
        }

        if (patient.hasGender()) {
            map.put("sex", patient.getGender().toString());
        }

        if (patient.hasBirthDate()) {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(patient.getBirthDate());

            map.put("DOB", date);
        }

        for (ContactPoint contactPoint : patient.getTelecom()) {
            if (contactPoint.hasSystem() && contactPoint.getSystem().equals(ContactPoint.ContactPointSystem.EMAIL)) {
                map.put("email", contactPoint.getValue());
            }

            // TO DO: MORE SYSTEM
        }

        for (Address address : patient.getAddress()) {
            String street = "";
            for (StringType line: address.getLine()) {
                street += line.toString();
            }

            map.put("street", street);
            if (address.hasCity()) {
                map.put("city", address.getCity());
            }
            if (address.hasState()) {
                map.put("state", address.getState());
            }
            if (address.hasPostalCode()) {
                map.put("postal_code", address.getPostalCode());
            }
            if (address.hasCountry()) {
                map.put("country", address.getCountry());
            }
        }

        return new JSONObject(map);
    }

}
