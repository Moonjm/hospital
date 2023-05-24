package com.example.hospital.service;

import com.example.hospital.domain.*;
import com.example.hospital.dto.VisitDto.ModifyVisitRequest;
import com.example.hospital.exception.OptionalObjectNullException;
import com.example.hospital.model.response.CommonSuccessResponse;
import com.example.hospital.repository.code.CodeGroupRepository;
import com.example.hospital.repository.code.CodeRepository;
import com.example.hospital.repository.hospital.HospitalRepository;
import com.example.hospital.repository.patient.PatientRepository;
import com.example.hospital.repository.patient.PatientVisitRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class VisitServiceTest {

    @Nested
    @DisplayName("환자 방문 등록")
    class InsertVisitTest {

        @Spy
        @InjectMocks
        VisitService visitService;
        @Mock
        HospitalRepository hospitalRepository;
        @Mock
        CodeGroupRepository codeGroupRepository;
        @Mock
        CodeRepository codeRepository;
        @Mock
        PatientVisitRepository patientVisitRepository;
        @Mock
        PatientRepository patientRepository;

        @Test
        @DisplayName("성공")
        void success1() {
            // given
            Hospital hospital = Hospital.builder()
                    .institutionNumber("hos-a")
                    .directorName("원장")
                    .name("A병원")
                    .build();

            CodeGroup codeGroup = CodeGroup.builder()
                    .cd("성별코드")
                    .name("성별코드")
                    .description("성별을 표시")
                    .build();

            Code code = Code.builder()
                    .cd("M")
                    .codeGroup(codeGroup)
                    .name("남성")
                    .build();

            Patient patient = Patient.builder()
                    .hospital(hospital)
                    .name("name")
                    .gender(code)
                    .genderCode("M")
                    .birthDate("1999-12-01")
                    .mobileNumber("010-1234-5678")
                    .build();

            CodeGroup visitCodeGroup = CodeGroup.builder()
                    .cd("방문상태코드")
                    .name("방문상태코드")
                    .description("환자방문의 상태(방문중, 종료, 취소)")
                    .build();

            Code visitCode = Code.builder()
                    .cd("1")
                    .codeGroup(visitCodeGroup)
                    .name("방문중")
                    .build();

            PatientVisit patientVisit = PatientVisit.builder()
                    .patient(patient)
                    .hospital(hospital)
                    .statusCode("1")
                    .status(visitCode)
                    .build();

            given(hospitalRepository.findByInstitutionNumber(anyString())).willReturn(Optional.of(hospital));
            given(patientRepository.findByRegistrationNumber(anyString())).willReturn(Optional.of(patient));
            given(codeGroupRepository.findByCd(anyString())).willReturn(Optional.of(visitCodeGroup));
            given(codeRepository.findByCodeGroupAndCd(any(), anyString())).willReturn(Optional.of(visitCode));
            given(patientVisitRepository.save(any())).willReturn(patientVisit);


            // when
            CommonSuccessResponse<String> response = (CommonSuccessResponse<String>) visitService.insertVisit("", "");

            // then
            assertThat(response.getResultCode()).isEqualTo(1);
            assertThat(response.getResultData()).isEqualTo(String.valueOf(patientVisit.getId()));
            then(visitService).should().insertVisit("", "");
        }

        @Test
        @DisplayName("실패 - 병원 아이디 오류")
        void fail1() {
            // given

            given(hospitalRepository.findByInstitutionNumber(anyString())).willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> visitService.insertVisit("", "")).isInstanceOf(OptionalObjectNullException.class);

            // then
            then(visitService).should().insertVisit("", "");
        }

        @Test
        @DisplayName("실패 - 환자 아이디 오류")
        void fail2() {
            // given

            Hospital hospital = Hospital.builder()
                    .institutionNumber("hos-a")
                    .directorName("원장")
                    .name("A병원")
                    .build();

            given(hospitalRepository.findByInstitutionNumber(anyString())).willReturn(Optional.of(hospital));
            given(patientRepository.findByRegistrationNumber(anyString())).willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> visitService.insertVisit("", "")).isInstanceOf(OptionalObjectNullException.class);

            // then
            then(visitService).should().insertVisit("", "");
        }

        @Test
        @DisplayName("실패 - 방문 상태 코드 오류")
        void fail3() {
            // given

            Hospital hospital = Hospital.builder()
                    .institutionNumber("hos-a")
                    .directorName("원장")
                    .name("A병원")
                    .build();

            CodeGroup codeGroup = CodeGroup.builder()
                    .cd("성별코드")
                    .name("성별코드")
                    .description("성별을 표시")
                    .build();

            Code code = Code.builder()
                    .cd("M")
                    .codeGroup(codeGroup)
                    .name("남성")
                    .build();

            Patient patient = Patient.builder()
                    .hospital(hospital)
                    .name("name")
                    .gender(code)
                    .genderCode("M")
                    .birthDate("1999-12-01")
                    .mobileNumber("010-1234-5678")
                    .build();

            CodeGroup visitCodeGroup = CodeGroup.builder()
                    .cd("방문상태코드")
                    .name("방문상태코드")
                    .description("환자방문의 상태(방문중, 종료, 취소)")
                    .build();

            given(hospitalRepository.findByInstitutionNumber(anyString())).willReturn(Optional.of(hospital));
            given(patientRepository.findByRegistrationNumber(anyString())).willReturn(Optional.of(patient));
            given(codeGroupRepository.findByCd(anyString())).willReturn(Optional.of(visitCodeGroup));
            given(codeRepository.findByCodeGroupAndCd(any(), anyString())).willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> visitService.insertVisit("", "")).isInstanceOf(OptionalObjectNullException.class);

            // then
            then(visitService).should().insertVisit("", "");
        }
    }

    @Nested
    @DisplayName("환자 방문 상태 수정")
    class ModifyVisitTest {

        @Spy
        @InjectMocks
        VisitService visitService;
        @Mock
        HospitalRepository hospitalRepository;
        @Mock
        CodeGroupRepository codeGroupRepository;
        @Mock
        CodeRepository codeRepository;
        @Mock
        PatientVisitRepository patientVisitRepository;
        @Mock
        PatientRepository patientRepository;

        @Test
        @DisplayName("성공")
        void success1() {
            // given
            Hospital hospital = Hospital.builder()
                    .institutionNumber("hos-a")
                    .directorName("원장")
                    .name("A병원")
                    .build();

            CodeGroup codeGroup = CodeGroup.builder()
                    .cd("성별코드")
                    .name("성별코드")
                    .description("성별을 표시")
                    .build();

            Code code = Code.builder()
                    .cd("M")
                    .codeGroup(codeGroup)
                    .name("남성")
                    .build();

            Patient patient = Patient.builder()
                    .hospital(hospital)
                    .name("name")
                    .gender(code)
                    .genderCode("M")
                    .birthDate("1999-12-01")
                    .mobileNumber("010-1234-5678")
                    .build();

            CodeGroup visitCodeGroup = CodeGroup.builder()
                    .cd("방문상태코드")
                    .name("방문상태코드")
                    .description("환자방문의 상태(방문중, 종료, 취소)")
                    .build();

            Code visitCode = Code.builder()
                    .cd("1")
                    .codeGroup(visitCodeGroup)
                    .name("방문중")
                    .build();

            PatientVisit patientVisit = PatientVisit.builder()
                    .patient(patient)
                    .hospital(hospital)
                    .statusCode("1")
                    .status(visitCode)
                    .build();
            ModifyVisitRequest dto = new ModifyVisitRequest();
            dto.setStatus("2");
            given(hospitalRepository.findByInstitutionNumber(anyString())).willReturn(Optional.of(hospital));
            given(patientRepository.findByRegistrationNumber(anyString())).willReturn(Optional.of(patient));
            given(codeGroupRepository.findByCd(anyString())).willReturn(Optional.of(visitCodeGroup));
            given(codeRepository.findByCodeGroupAndCd(any(), anyString())).willReturn(Optional.of(visitCode));
            given(patientVisitRepository.findByIdAndHospitalAndPatient(anyLong(), any(), any())).willReturn(Optional.of(patientVisit));


            // when
            CommonSuccessResponse<String> response = (CommonSuccessResponse<String>) visitService.modifyVisit("", "", 1L, dto);

            // then
            assertThat(response.getResultCode()).isEqualTo(1);
            then(visitService).should().modifyVisit("", "", 1L, dto);
        }

        @Test
        @DisplayName("실패 - 병원 아이디 오류")
        void fail1() {
            // given
            ModifyVisitRequest dto = new ModifyVisitRequest();
            dto.setStatus("2");

            given(hospitalRepository.findByInstitutionNumber(anyString())).willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> visitService.modifyVisit("", "", 1L, dto)).isInstanceOf(OptionalObjectNullException.class);

            // then
            then(visitService).should().modifyVisit("", "", 1L, dto);
        }

        @Test
        @DisplayName("실패 - 환자 아이디 오류")
        void fail2() {
            // given
            ModifyVisitRequest dto = new ModifyVisitRequest();
            dto.setStatus("2");

            Hospital hospital = Hospital.builder()
                    .institutionNumber("hos-a")
                    .directorName("원장")
                    .name("A병원")
                    .build();

            given(hospitalRepository.findByInstitutionNumber(anyString())).willReturn(Optional.of(hospital));
            given(patientRepository.findByRegistrationNumber(anyString())).willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> visitService.modifyVisit("", "", 1L, dto)).isInstanceOf(OptionalObjectNullException.class);

            // then
            then(visitService).should().modifyVisit("", "", 1L, dto);
        }

        @Test
        @DisplayName("실패 - 방문 아이디 오류")
        void fail3() {
            // given

            Hospital hospital = Hospital.builder()
                    .institutionNumber("hos-a")
                    .directorName("원장")
                    .name("A병원")
                    .build();

            CodeGroup codeGroup = CodeGroup.builder()
                    .cd("성별코드")
                    .name("성별코드")
                    .description("성별을 표시")
                    .build();

            Code code = Code.builder()
                    .cd("M")
                    .codeGroup(codeGroup)
                    .name("남성")
                    .build();

            Patient patient = Patient.builder()
                    .hospital(hospital)
                    .name("name")
                    .gender(code)
                    .genderCode("M")
                    .birthDate("1999-12-01")
                    .mobileNumber("010-1234-5678")
                    .build();
            ModifyVisitRequest dto = new ModifyVisitRequest();
            dto.setStatus("2");

            given(hospitalRepository.findByInstitutionNumber(anyString())).willReturn(Optional.of(hospital));
            given(patientRepository.findByRegistrationNumber(anyString())).willReturn(Optional.of(patient));
            given(patientVisitRepository.findByIdAndHospitalAndPatient(anyLong(), any(), any())).willReturn(Optional.empty());
            ;
            // when
            assertThatThrownBy(() -> visitService.modifyVisit("", "", 1L, dto)).isInstanceOf(OptionalObjectNullException.class);

            // then
            then(visitService).should().modifyVisit("", "", 1L, dto);
        }

        @Test
        @DisplayName("실패 - 방문 상태 코드 오류")
        void fail4() {
            // given

            Hospital hospital = Hospital.builder()
                    .institutionNumber("hos-a")
                    .directorName("원장")
                    .name("A병원")
                    .build();

            CodeGroup codeGroup = CodeGroup.builder()
                    .cd("성별코드")
                    .name("성별코드")
                    .description("성별을 표시")
                    .build();

            Code code = Code.builder()
                    .cd("M")
                    .codeGroup(codeGroup)
                    .name("남성")
                    .build();

            Patient patient = Patient.builder()
                    .hospital(hospital)
                    .name("name")
                    .gender(code)
                    .genderCode("M")
                    .birthDate("1999-12-01")
                    .mobileNumber("010-1234-5678")
                    .build();

            CodeGroup visitCodeGroup = CodeGroup.builder()
                    .cd("방문상태코드")
                    .name("방문상태코드")
                    .description("환자방문의 상태(방문중, 종료, 취소)")
                    .build();

            Code visitCode = Code.builder()
                    .cd("1")
                    .codeGroup(visitCodeGroup)
                    .name("방문중")
                    .build();

            PatientVisit patientVisit = PatientVisit.builder()
                    .patient(patient)
                    .hospital(hospital)
                    .statusCode("1")
                    .status(visitCode)
                    .build();
            ModifyVisitRequest dto = new ModifyVisitRequest();
            dto.setStatus("2");

            given(hospitalRepository.findByInstitutionNumber(anyString())).willReturn(Optional.of(hospital));
            given(patientRepository.findByRegistrationNumber(anyString())).willReturn(Optional.of(patient));
            given(codeGroupRepository.findByCd(anyString())).willReturn(Optional.of(visitCodeGroup));
            given(patientVisitRepository.findByIdAndHospitalAndPatient(anyLong(), any(), any())).willReturn(Optional.of(patientVisit));
            given(codeRepository.findByCodeGroupAndCd(any(), anyString())).willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> visitService.modifyVisit("", "", 1L, dto)).isInstanceOf(OptionalObjectNullException.class);

            // then
            then(visitService).should().modifyVisit("", "", 1L, dto);
        }
    }
}