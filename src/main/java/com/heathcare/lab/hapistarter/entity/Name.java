package com.heathcare.lab.hapistarter.entity;

import javax.persistence.*;

@Entity
@Table(name = "name")
public class Name {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String familyName;
    private String givenName;
    private String prefix;

    @ManyToOne(fetch = FetchType.LAZY)
    private PatientEntity patientEntity;

    public Name() {

    }

    public Name(String familyName, String givenName, String prefix) {
        this.familyName = familyName;
        this.givenName = givenName;
        this.prefix = prefix;
    }

    public PatientEntity getPatientEntity() {
        return patientEntity;
    }

    public void setPatientEntity(PatientEntity patientEntity) {
        this.patientEntity = patientEntity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}