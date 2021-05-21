package com.heathcare.lab.hapistarter.entity;

import javax.persistence.*;

@Entity
@Table(name = "identifier")
public class Identifier {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String system;
    private String value;
    private org.hl7.fhir.r4.model.Identifier.IdentifierUse identifierUse;

    @ManyToOne(fetch = FetchType.LAZY)
    private PatientEntity patientEntity;

    public Identifier() {
    }

    public Identifier(String system, String value, org.hl7.fhir.r4.model.Identifier.IdentifierUse identifierUse) {
        this.system = system;
        this.value = value;
        this.identifierUse = identifierUse;
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

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public org.hl7.fhir.r4.model.Identifier.IdentifierUse getIdentifierUse() {
        return identifierUse;
    }

    public void setIdentifierUse(org.hl7.fhir.r4.model.Identifier.IdentifierUse identifierUse) {
        this.identifierUse = identifierUse;
    }
}
