package com.example.hospital.service;

import com.example.hospital.domain.*;
import com.example.hospital.dto.VisitDto.ModifyVisitRequest;
import com.example.hospital.dto.VisitDto.VisitRequest;
import com.example.hospital.model.response.BasicResponse;
import com.example.hospital.model.response.CommonSuccessResponse;
import com.example.hospital.repository.code.CodeGroupRepository;
import com.example.hospital.repository.code.CodeRepository;
import com.example.hospital.repository.hospital.HospitalRepository;
import com.example.hospital.repository.patient.PatientRepository;
import com.example.hospital.repository.patient.PatientVisitRepository;
import com.sun.jdi.ObjectCollectedException;
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
    public BasicResponse insertPatient(VisitRequest dto) {
        Hospital findHospital = hospitalRepository.findById(dto.getHospitalId()).orElseThrow(ObjectCollectedException::new);
        Patient findPatient = patientRepository.findById(dto.getPatientId()).orElseThrow(ObjectCollectedException::new);
        CodeGroup findCodeGroup = codeGroupRepository.findByCd("방문상태코드").orElseThrow(ObjectCollectedException::new);
        Code findCode = codeRepository.findByCodeGroupAndCd(findCodeGroup, "1").orElseThrow(ObjectCollectedException::new);
        PatientVisit savePatientVisit = patientVisitRepository.save(
                PatientVisit.builder()
                        .hospital(findHospital)
                        .patient(findPatient)
                        .status(findCode)
                        .statusCode("1")
                        .build()
        );
        findPatient.updateLastVisit(savePatientVisit.getRegDate());
        return new CommonSuccessResponse<>("SUCCESS");
    }

    @Transactional
    public BasicResponse modifyPatient(ModifyVisitRequest dto) {
        PatientVisit findPatientVisit = patientVisitRepository.findById(dto.getVisitId()).orElseThrow(ObjectCollectedException::new);
        CodeGroup findCodeGroup = codeGroupRepository.findByCd("방문상태코드").orElseThrow(ObjectCollectedException::new);
        Code findCode = codeRepository.findByCodeGroupAndCd(findCodeGroup, dto.getStatus()).orElseThrow(ObjectCollectedException::new);
        findPatientVisit.updateStatus(findCode);
        return new CommonSuccessResponse<>("SUCCESS");
    }
}
