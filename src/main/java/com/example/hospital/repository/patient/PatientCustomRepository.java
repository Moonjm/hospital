package com.example.hospital.repository.patient;

import com.example.hospital.domain.Hospital;
import com.example.hospital.domain.Patient;
import com.example.hospital.domain.PatientVisit;
import com.example.hospital.repository.custom.Querydsl4RepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.example.hospital.domain.QCode.code;
import static com.example.hospital.domain.QHospital.hospital;
import static com.example.hospital.domain.QPatient.patient;
import static com.example.hospital.domain.QPatientVisit.patientVisit;

@Repository
public class PatientCustomRepository extends Querydsl4RepositorySupport {
    public PatientCustomRepository() {
        super(Patient.class);
    }

    public Page<Patient> getPatients(Pageable pageable, Hospital hospital, String type, String value) {
        return applyPagination(pageable, content -> content
                        .selectFrom(patient)
                        .where(
                                patient.hospital.eq(hospital),
                                searchType(type, value)
                        )
                ,
                countQuery -> countQuery
                        .select(patient.id)
                        .from(patient)
                        .where(
                                patient.hospital.eq(hospital),
                                searchType(type, value)
                        )
        );
    }

    public List<PatientVisit> getPatientVisits(Hospital targetHospital, Patient patient) {
        return this.getQueryFactory()
                .selectFrom(patientVisit)
                .join(patientVisit.hospital, hospital).fetchJoin()
                .join(patientVisit.status, code).fetchJoin()
                .where(
                        patientVisit.hospital.eq(targetHospital)
                                .and(patientVisit.patient.eq(patient))
                ).fetch();
    }

    BooleanExpression searchType(String type, String value) {
        if (StringUtils.hasText(type) && StringUtils.hasText(value)) {
            return switch (type) {
                case "name" -> patient.name.contains(value);
                case "registrationNumber" -> patient.registrationNumber.contains(value);
                case "birthDate" -> patient.birthDate.contains(value);
                default -> null;
            };
        }
        return null;
    }
}
