package com.heathcare.lab.hapistarter.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "healthcare_patients")
@Data @Builder
public class PatientEntity {
    @Id
    @GeneratedValue
    long id;

    @Column(name = "fhir_id")
    String patientId;

    @Version
    @Column(name = "version")
    long version;

    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time", updatable = false)
    Date createdTime;

    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_time")
    Date updatedTime;

    @Basic(optional = false)
    @Column(name = "failed_verifications")
    int failedVerifications;

    @Basic(optional = false)
    @Temporal(TemporalType.DATE)
    @Column(name = "pat_birthdate")
    Date patientBirthDate;

    @Basic(optional = false)
    @Column(name = "pat_sex")
    String patientSex;

    @Embedded
    PersonNameEntity patientName;

    @PrePersist
    public void onPrePersist() {
        Date now = new Date();
        createdTime = now;
        updatedTime = now;
        patientId = String.join("/", "Patient", Long.toString(id));
    }

    @PreUpdate
    public void onPreUpdate() {
        updatedTime = new Date();
    }
}
