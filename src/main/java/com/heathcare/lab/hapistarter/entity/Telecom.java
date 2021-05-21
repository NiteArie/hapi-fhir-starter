package com.heathcare.lab.hapistarter.entity;

import org.hl7.fhir.r4.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointUse;

import javax.persistence.*;

@Entity
@Table(name = "telecom")
public class Telecom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String value;
    private ContactPointSystem system;
    private ContactPointUse use;

    @ManyToOne(fetch = FetchType.LAZY)
    private PatientEntity patientEntity;

    public Telecom() {
    }

    public Telecom(String value, ContactPointSystem system, ContactPointUse use) {
        this.value = value;
        this.system = system;
        this.use = use;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PatientEntity getPatientEntity() {
        return patientEntity;
    }

    public void setPatientEntity(PatientEntity patientEntity) {
        this.patientEntity = patientEntity;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ContactPointSystem getSystem() {
        return system;
    }

    public void setSystem(ContactPointSystem system) {
        this.system = system;
    }

    public ContactPointUse getUse() {
        return use;
    }

    public void setUse(ContactPointUse use) {
        this.use = use;
    }
}
