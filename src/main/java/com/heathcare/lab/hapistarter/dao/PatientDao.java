package com.heathcare.lab.hapistarter.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.param.DateParam;

import com.heathcare.lab.hapistarter.dao.transform.PatientEntityToFHIRPatient;
import com.heathcare.lab.hapistarter.entity.Name;
import com.heathcare.lab.hapistarter.entity.PatientEntity;
import com.heathcare.lab.hapistarter.entity.Telecom;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class PatientDao implements IPatient {

    @Autowired
    private PatientEntityToFHIRPatient patientEntityToFHIRPatient;

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Patient create(FhirContext ctx, Patient patient) {
        PatientEntity patientEntity = new PatientEntity();

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

        sessionFactory.getCurrentSession().save(patientEntity);

        return patientEntityToFHIRPatient.transform(patientEntity);
    }

    @Override
    public Patient read(FhirContext ctx, IdType theId) {
        Long id = Long.valueOf(theId.getIdPart());
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<PatientEntity> criteriaQuery = cb.createQuery(PatientEntity.class);
        Root<PatientEntity> model = criteriaQuery.from(PatientEntity.class);
        criteriaQuery.where(cb.equal(model.get("id"), id));
        TypedQuery<PatientEntity> q = sessionFactory.getCurrentSession().createQuery(criteriaQuery);
        return patientEntityToFHIRPatient.transform(q.getSingleResult());
    }

    @Override
    public List<Resource> searchByDob(FhirContext ctx, DateParam birthDate) {
        List<Resource> resources = new ArrayList<>();
        List<PatientEntity> patientResults = new ArrayList<>();

        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<PatientEntity> criteriaQuery = cb.createQuery(PatientEntity.class);
        Root<PatientEntity> model = criteriaQuery.from(PatientEntity.class);


        if (birthDate != null) {
            criteriaQuery.where(cb.equal(model.get("dateOfBirth"), birthDate.getValue()));
            TypedQuery<PatientEntity> q = sessionFactory.getCurrentSession().createQuery(criteriaQuery);
            patientResults.add(q.getSingleResult());
        }

        for (PatientEntity patientEntity : patientResults) {
            resources.add(patientEntityToFHIRPatient.transform(patientEntity));
        }

        return resources;
    }

    @Override
    public List<Resource> searchByFamilyName(FhirContext ctx, @Param("name") String familyName) {
        List<Resource> res = new ArrayList<>();
        String hql = "from PatientEntity as p JOIN p.names as n WHERE n.familyName like :name";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("name", familyName);
        List<Object[]> list = query.getResultList();
        for (int i = 0; i < list.size(); i++) {
            Object[] test = list.get(i);
            PatientEntity patientEntity = (PatientEntity) test[0];
            res.add(patientEntityToFHIRPatient.transform(patientEntity));
        }
        return res;
    }

    @Override
    public List<Resource> searchByGivenName(FhirContext ctx, String givenName) {
        List<Resource> res = new ArrayList<>();
        String hql = "from PatientEntity as p JOIN p.names as n WHERE n.givenName like :name";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("name", givenName);
        List<Object[]> list = query.getResultList();
        for (int i = 0; i < list.size(); i++) {
            Object[] test = list.get(i);
            PatientEntity patientEntity = (PatientEntity) test[0];
            res.add(patientEntityToFHIRPatient.transform(patientEntity));
        }
        return res;
    }
}
