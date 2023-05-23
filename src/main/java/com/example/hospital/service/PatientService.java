package com.example.hospital.service;

import com.example.hospital.domain.Code;
import com.example.hospital.domain.CodeGroup;
import com.example.hospital.domain.Hospital;
import com.example.hospital.domain.Patient;
import com.example.hospital.dto.CustomReturnPageDto;
import com.example.hospital.dto.PatientDto.*;
import com.example.hospital.model.response.BasicResponse;
import com.example.hospital.model.response.CommonSuccessResponse;
import com.example.hospital.repository.code.CodeGroupRepository;
import com.example.hospital.repository.code.CodeRepository;
import com.example.hospital.repository.hospital.HospitalRepository;
import com.example.hospital.repository.patient.PatientCustomRepository;
import com.example.hospital.repository.patient.PatientRepository;
import com.sun.jdi.ObjectCollectedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PatientService {

    private final HospitalRepository hospitalRepository;
    private final PatientRepository patientRepository;
    private final CodeRepository codeRepository;
    private final CodeGroupRepository codeGroupRepository;
    private final PatientCustomRepository patientCustomRepository;

    @Transactional
    public BasicResponse insertPatient(String hospitalId, InsertPatientRequest dto) {
        Hospital findHospital = hospitalRepository.findByInstitutionNumber(hospitalId).orElseThrow(ObjectCollectedException::new);
        Code findCode = null;
        if (StringUtils.hasText(dto.getGender())) {
            CodeGroup findCodeGroup = codeGroupRepository.findByCd("성별코드").orElseThrow(ObjectCollectedException::new);
            findCode = codeRepository.findByCodeGroupAndCd(findCodeGroup, dto.getGender()).orElseThrow(ObjectCollectedException::new);
        }
        Patient savePatient = patientRepository.save(
                Patient.builder()
                        .hospital(findHospital)
                        .name(dto.getName())
                        .gender(findCode)
                        .genderCode(dto.getGender())
                        .birthDate(dto.getBirthDate())
                        .mobileNumber(dto.getMobileNumber())
                        .build()
        );
        return new CommonSuccessResponse<>(String.valueOf(savePatient.getRegistrationNumber()));
    }

    public BasicResponse getPatients(String hospitalId, Pageable pageable, String type, String value) {
        Hospital findHospital = hospitalRepository.findByInstitutionNumber(hospitalId).orElseThrow(ObjectCollectedException::new);
        Page<Patient> patients = patientCustomRepository.getPatients(pageable, findHospital, type, value);
        List<GetPatientsResponse> content = patients.getContent().stream().map(GetPatientsResponse::new).collect(Collectors.toList());
        return new CommonSuccessResponse<>(new CustomReturnPageDto(patients.getSize(), patients.getNumber(), patients.isFirst(), patients.isLast(),
                patients.getNumberOfElements(), patients.isEmpty(), content, patients.getTotalElements()));
    }

    @Transactional
    public BasicResponse modifyPatient(ModifyPatientRequest dto) {
        Patient findPatient = patientRepository.findByRegistrationNumber(dto.getPatientId()).orElseThrow(ObjectCollectedException::new);
        CodeGroup findCodeGroup = codeGroupRepository.findByCd("성별코드").orElseThrow(ObjectCollectedException::new);
        Code findCode = codeRepository.findByCodeGroupAndCd(findCodeGroup, dto.getGender()).orElseThrow(ObjectCollectedException::new);
        findPatient.updatePatient(dto.getName(), findCode, dto.getBirthDate(), dto.getMobileNumber());
        return new CommonSuccessResponse<>("SUCCESS");
    }

    @Transactional
    public BasicResponse deletePatient(String id) {
        Patient findPatient = patientRepository.findByRegistrationNumber(id).orElseThrow(ObjectCollectedException::new);
        patientRepository.delete(findPatient);
        return new CommonSuccessResponse<>("SUCCESS");
    }

    public BasicResponse getPatientDetail(String id, String hospitalId) {
        Hospital findHospital = hospitalRepository.findByInstitutionNumber(hospitalId).orElseThrow(ObjectCollectedException::new);
        Patient findPatient = patientRepository.findByRegistrationNumber(id).orElseThrow(ObjectCollectedException::new);
        GetPatientDetailResponse ret = new GetPatientDetailResponse(findPatient);
        ret.setVisits(patientCustomRepository.getPatientVisits(findHospital, findPatient).stream().map(PatientVisitDto::new).collect(Collectors.toList()));
        return new CommonSuccessResponse<>(ret);
    }
}
