package com.example.hospital.repository.patient;

import com.example.hospital.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByRegistrationNumber(String registrationNumber);
}
