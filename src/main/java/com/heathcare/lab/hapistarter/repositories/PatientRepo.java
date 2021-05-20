package com.heathcare.lab.hapistarter.repositories;

import com.heathcare.lab.hapistarter.entity.PatientEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface PatientRepo extends CrudRepository<PatientEntity, UUID> {
    Optional<PatientEntity> findByPatientId(String patientId);
}
