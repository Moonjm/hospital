package com.example.hospital.repository.patient;

import com.example.hospital.domain.PatientVisit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientVisitRepository extends JpaRepository<PatientVisit, Long> {
}
