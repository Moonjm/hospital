package com.example.hospital.repository.patient;

import com.example.hospital.domain.Hospital;
import com.example.hospital.domain.Patient;
import com.example.hospital.domain.PatientVisit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientVisitRepository extends JpaRepository<PatientVisit, Long> {
    Optional<PatientVisit> findByIdAndHospitalAndPatient(Long id, Hospital hospital, Patient patient);
}
