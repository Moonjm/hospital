package com.example.hospital.service;

import com.example.hospital.domain.*;
import com.example.hospital.dto.VisitDto.ModifyVisitRequest;
import com.example.hospital.exception.OptionalObjectNullException;
import com.example.hospital.model.response.BasicResponse;
import com.example.hospital.model.response.CommonSuccessResponse;
import com.example.hospital.repository.code.CodeGroupRepository;
import com.example.hospital.repository.code.CodeRepository;
import com.example.hospital.repository.hospital.HospitalRepository;
import com.example.hospital.repository.patient.PatientRepository;
import com.example.hospital.repository.patient.PatientVisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VisitService {

    private final HospitalRepository hospitalRepository;
    private final PatientRepository patientRepository;
    private final PatientVisitRepository patientVisitRepository;
    private final CodeGroupRepository codeGroupRepository;
    private final CodeRepository codeRepository;

    @Transactional
    public BasicResponse insertVisit(String hospitalId, String patientId) {
        Hospital findHospital = hospitalRepository.findByInstitutionNumber(hospitalId).orElseThrow(OptionalObjectNullException::new);
        Patient findPatient = patientRepository.findByRegistrationNumber(patientId).orElseThrow(OptionalObjectNullException::new);
        CodeGroup findCodeGroup = codeGroupRepository.findByCd("방문상태코드").orElseThrow(OptionalObjectNullException::new);
        Code findCode = codeRepository.findByCodeGroupAndCd(findCodeGroup, "1").orElseThrow(OptionalObjectNullException::new);
        PatientVisit savePatientVisit = patientVisitRepository.save(
                PatientVisit.builder()
                        .hospital(findHospital)
                        .patient(findPatient)
                        .status(findCode)
                        .statusCode("1")
                        .build()
        );
        findPatient.updateLastVisit(savePatientVisit.getRegDate());
        return new CommonSuccessResponse<>(String.valueOf(savePatientVisit.getId()));
    }

    @Transactional
    public BasicResponse modifyVisit(String hospitalId, String patientId, Long visitId, ModifyVisitRequest dto) {
        Hospital findHospital = hospitalRepository.findByInstitutionNumber(hospitalId).orElseThrow(OptionalObjectNullException::new);
        Patient findPatient = patientRepository.findByRegistrationNumber(patientId).orElseThrow(OptionalObjectNullException::new);
        PatientVisit findPatientVisit = patientVisitRepository.findByIdAndHospitalAndPatient(visitId, findHospital, findPatient).orElseThrow(OptionalObjectNullException::new);
        CodeGroup findCodeGroup = codeGroupRepository.findByCd("방문상태코드").orElseThrow(OptionalObjectNullException::new);
        Code findCode = codeRepository.findByCodeGroupAndCd(findCodeGroup, dto.getStatus()).orElseThrow(OptionalObjectNullException::new);
        findPatientVisit.updateStatus(findCode);
        return new CommonSuccessResponse<>("SUCCESS");
    }
}
