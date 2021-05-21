package com.heathcare.lab.hapistarter.entity;

import org.hl7.fhir.r4.model.Enumerations;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;

@Entity
@Table(name = "patient")
public class PatientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date dateOfBirth;
    private Enumerations.AdministrativeGender gender;

    @OneToMany(mappedBy = "patientEntity", cascade = CascadeType.ALL)
    private Collection<Name> names = new LinkedHashSet<>();

    @OneToMany(mappedBy = "patientEntity", cascade = CascadeType.ALL)
    private Collection<Identifier> identifiers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "patientEntity", cascade = CascadeType.ALL)
    private Collection<Address> addresses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "patientEntity", cascade = CascadeType.ALL)
    private Collection<Telecom> telecoms = new LinkedHashSet<>();

    public PatientEntity() {
    }

    public PatientEntity(Date dateOfBirth, Enumerations.AdministrativeGender gender, Collection<Name> names, Collection<Identifier> identifiers, Collection<Address> addresses, Collection<Telecom> telecoms) {
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.names = names;
        this.identifiers = identifiers;
        this.addresses = addresses;
        this.telecoms = telecoms;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Enumerations.AdministrativeGender getGender() {
        return gender;
    }

    public void setGender(Enumerations.AdministrativeGender gender) {
        this.gender = gender;
    }

    public Collection<Name> getNames() {
        return names;
    }

    public void setNames(Collection<Name> names) {
        this.names = names;
    }

    public Collection<Identifier> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(Collection<Identifier> identifiers) {
        this.identifiers = identifiers;
    }

    public Collection<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Collection<Address> addresses) {
        this.addresses = addresses;
    }

    public Collection<Telecom> getTelecoms() {
        return telecoms;
    }

    public void setTelecoms(Collection<Telecom> telecoms) {
        this.telecoms = telecoms;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addToName(Name name) {
        name.setPatientEntity(this);
        this.names.add(name);
    }

    public void addToAddress(Address address) {
        address.setPatientEntity(this);
        this.addresses.add(address);
    }

    public void addToTelecom(Telecom telecom) {
        telecom.setPatientEntity(this);
        this.telecoms.add(telecom);
    }

    public void addToIdentifer(Identifier identifier) {
        identifier.setPatientEntity(this);
        this.identifiers.add(identifier);
    }

}
