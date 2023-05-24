package com.example.hospital.service;

import com.example.hospital.domain.Code;
import com.example.hospital.domain.CodeGroup;
import com.example.hospital.domain.Hospital;
import com.example.hospital.domain.Patient;
import com.example.hospital.dto.CustomReturnPageDto;
import com.example.hospital.dto.PatientDto;
import com.example.hospital.dto.PatientDto.GetPatientsResponse;
import com.example.hospital.dto.PatientDto.InsertPatientRequest;
import com.example.hospital.dto.PatientDto.ModifyPatientRequest;
import com.example.hospital.exception.OptionalObjectNullException;
import com.example.hospital.model.response.CommonSuccessResponse;
import com.example.hospital.repository.code.CodeGroupRepository;
import com.example.hospital.repository.code.CodeRepository;
import com.example.hospital.repository.hospital.HospitalRepository;
import com.example.hospital.repository.patient.PatientCustomRepository;
import com.example.hospital.repository.patient.PatientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Nested
    @DisplayName("환자 등록")
    class InsertPatientTest {

        @Spy
        @InjectMocks
        PatientService patientService;
        @Mock
        HospitalRepository hospitalRepository;
        @Mock
        CodeGroupRepository codeGroupRepository;
        @Mock
        CodeRepository codeRepository;
        @Mock
        PatientRepository patientRepository;

        @Test
        @DisplayName("성공")
        void success1() {
            // given
            InsertPatientRequest dto = new InsertPatientRequest();
            dto.setName("name");
            dto.setGender("M");
            dto.setBirthDate("2000-01-02");
            dto.setMobileNumber("010-1234-5678");

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
                    .name(dto.getName())
                    .gender(code)
                    .genderCode(dto.getGender())
                    .birthDate(dto.getBirthDate())
                    .mobileNumber(dto.getMobileNumber())
                    .build();

            given(hospitalRepository.findByInstitutionNumber(anyString())).willReturn(Optional.of(hospital));
            given(codeGroupRepository.findByCd(anyString())).willReturn(Optional.of(codeGroup));
            given(codeRepository.findByCodeGroupAndCd(any(), anyString())).willReturn(Optional.of(code));
            given(patientRepository.save(any())).willReturn(patient);


            // when
            CommonSuccessResponse<String> response = (CommonSuccessResponse<String>) patientService.insertPatient(hospital.getInstitutionNumber(), dto);

            // then
            assertThat(response.getResultCode()).isEqualTo(1);
            assertThat(response.getResultData()).isEqualTo(patient.getRegistrationNumber());
            then(patientService).should().insertPatient(hospital.getInstitutionNumber(), dto);
        }

        @Test
        @DisplayName("실패 - 병원 아이디 오류")
        void fail1() {
            // given
            InsertPatientRequest dto = new InsertPatientRequest();

            given(hospitalRepository.findByInstitutionNumber(anyString())).willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> patientService.insertPatient("", dto)).isInstanceOf(OptionalObjectNullException.class);

            // then
            then(patientService).should().insertPatient("", dto);
        }

        @Test
        @DisplayName("실패 - 성별 입력 오류")
        void fail2() {
            // given
            InsertPatientRequest dto = new InsertPatientRequest();
            dto.setName("name");
            dto.setGender("M");
            dto.setBirthDate("2000-01-02");
            dto.setMobileNumber("010-1234-5678");

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

            given(hospitalRepository.findByInstitutionNumber(anyString())).willReturn(Optional.of(hospital));
            given(codeGroupRepository.findByCd(anyString())).willReturn(Optional.of(codeGroup));
            given(codeRepository.findByCodeGroupAndCd(any(), anyString())).willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> patientService.insertPatient("", dto)).isInstanceOf(OptionalObjectNullException.class);

            // then
            then(patientService).should().insertPatient("", dto);
        }
    }

    @Nested
    @DisplayName("환자 목록 조회")
    class GetPatientsTest {

        @Spy
        @InjectMocks
        PatientService patientService;
        @Mock
        HospitalRepository hospitalRepository;
        @Mock
        PatientCustomRepository patientCustomRepository;

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
                    .birthDate("")
                    .mobileNumber("")
                    .build();
            List<Patient> patients = new ArrayList<>();
            patients.add(patient);
            Page<Patient> patientPage = new PageImpl<>(patients);

            Pageable pageable = PageRequest.of(0, 10);

            given(hospitalRepository.findByInstitutionNumber(anyString())).willReturn(Optional.of(hospital));
            given(patientCustomRepository.getPatients(any(), any(), anyString(), anyString())).willReturn(patientPage);

            // when
            CommonSuccessResponse<CustomReturnPageDto> response = (CommonSuccessResponse<CustomReturnPageDto>)
                    patientService.getPatients(hospital.getInstitutionNumber(), pageable, "", "");

            // then
            assertThat(response.getResultCode()).isEqualTo(1);
            List<GetPatientsResponse> content = (List<GetPatientsResponse>) response.getResultData().getContent();
            assertThat(content.get(0).getPatientId()).isEqualTo(patient.getRegistrationNumber());
            then(patientService).should().getPatients(hospital.getInstitutionNumber(), pageable, "", "");
        }

        @Test
        @DisplayName("실패 - 병원 아이디 오류")
        void fail1() {
            // given
            Pageable pageable = PageRequest.of(0, 10);

            given(hospitalRepository.findByInstitutionNumber(anyString())).willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> patientService.getPatients("", pageable, "", "")).isInstanceOf(OptionalObjectNullException.class);

            // then
            then(patientService).should().getPatients("", pageable, "", "");
        }
    }

    @Nested
    @DisplayName("환자 수정")
    class ModifyPatientTest {

        @Spy
        @InjectMocks
        PatientService patientService;
        @Mock
        CodeGroupRepository codeGroupRepository;
        @Mock
        CodeRepository codeRepository;
        @Mock
        PatientRepository patientRepository;

        @Test
        @DisplayName("성공")
        void success1() {
            // given
            ModifyPatientRequest dto = new ModifyPatientRequest();
            dto.setPatientId("patientId");
            dto.setName("name2");
            dto.setGender("M");
            dto.setBirthDate("2000-01-02");
            dto.setMobileNumber("010-1234-5678");

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
                    .genderCode(dto.getGender())
                    .birthDate(dto.getBirthDate())
                    .mobileNumber(dto.getMobileNumber())
                    .build();

            given(codeGroupRepository.findByCd(anyString())).willReturn(Optional.of(codeGroup));
            given(codeRepository.findByCodeGroupAndCd(any(), anyString())).willReturn(Optional.of(code));
            given(patientRepository.findByRegistrationNumber(anyString())).willReturn(Optional.of(patient));


            // when
            CommonSuccessResponse<String> response = (CommonSuccessResponse<String>) patientService.modifyPatient(dto);

            // then
            assertThat(response.getResultCode()).isEqualTo(1);
            assertThat(patient.getName()).isEqualTo(dto.getName());
            then(patientService).should().modifyPatient(dto);
        }

        @Test
        @DisplayName("실패 - 환자 아이디 오류")
        void fail1() {
            // given
            ModifyPatientRequest dto = new ModifyPatientRequest();
            dto.setPatientId("id");

            given(patientRepository.findByRegistrationNumber(anyString())).willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> patientService.modifyPatient(dto)).isInstanceOf(OptionalObjectNullException.class);

            // then
            then(patientService).should().modifyPatient(dto);
        }

        @Test
        @DisplayName("실패 - 성별 입력 오류")
        void fail2() {
            // given
            ModifyPatientRequest dto = new ModifyPatientRequest();
            dto.setPatientId("id");
            dto.setName("name");
            dto.setGender("M");
            dto.setBirthDate("2000-01-02");
            dto.setMobileNumber("010-1234-5678");

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
                    .genderCode(dto.getGender())
                    .birthDate(dto.getBirthDate())
                    .mobileNumber(dto.getMobileNumber())
                    .build();

            given(patientRepository.findByRegistrationNumber(anyString())).willReturn(Optional.of(patient));
            given(codeGroupRepository.findByCd(anyString())).willReturn(Optional.of(codeGroup));
            given(codeRepository.findByCodeGroupAndCd(any(), anyString())).willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> patientService.modifyPatient(dto)).isInstanceOf(OptionalObjectNullException.class);

            // then
            then(patientService).should().modifyPatient(dto);
        }
    }

    @Nested
    @DisplayName("환자 삭제")
    class DeletePatientTest {

        @Spy
        @InjectMocks
        PatientService patientService;
        @Mock
        PatientRepository patientRepository;

        @Test
        @DisplayName("성공")
        void success1() {
            // given
            ModifyPatientRequest dto = new ModifyPatientRequest();
            dto.setPatientId("patientId");
            dto.setName("name2");
            dto.setGender("M");
            dto.setBirthDate("2000-01-02");
            dto.setMobileNumber("010-1234-5678");

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
                    .genderCode(dto.getGender())
                    .birthDate(dto.getBirthDate())
                    .mobileNumber(dto.getMobileNumber())
                    .build();

            given(patientRepository.findByRegistrationNumber(anyString())).willReturn(Optional.of(patient));


            // when
            CommonSuccessResponse<String> response = (CommonSuccessResponse<String>) patientService.deletePatient("id");

            // then
            assertThat(response.getResultCode()).isEqualTo(1);
            then(patientService).should().deletePatient("id");
            then(patientRepository).should().delete(any());
        }

        @Test
        @DisplayName("실패 - 환자 아이디 오류")
        void fail1() {
            // given

            given(patientRepository.findByRegistrationNumber(anyString())).willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> patientService.deletePatient("")).isInstanceOf(OptionalObjectNullException.class);

            // then
            then(patientService).should().deletePatient("");
        }
    }

    @Nested
    @DisplayName("환자 상세 조회")
    class GetPatientDetailTest {

        @Spy
        @InjectMocks
        PatientService patientService;
        @Mock
        HospitalRepository hospitalRepository;
        @Mock
        PatientCustomRepository patientCustomRepository;
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
                    .birthDate("")
                    .mobileNumber("")
                    .build();

            given(hospitalRepository.findByInstitutionNumber(anyString())).willReturn(Optional.of(hospital));
            given(patientRepository.findByRegistrationNumber(anyString())).willReturn(Optional.of(patient));
            given(patientCustomRepository.getPatientVisits(any(), any())).willReturn(new ArrayList<>());

            // when
            CommonSuccessResponse<PatientDto.GetPatientDetailResponse> response = (CommonSuccessResponse<PatientDto.GetPatientDetailResponse>)
                    patientService.getPatientDetail(hospital.getInstitutionNumber(), patient.getRegistrationNumber());

            // then
            assertThat(response.getResultCode()).isEqualTo(1);
            assertThat(response.getResultData().getPatientId()).isEqualTo(patient.getRegistrationNumber());
            then(patientService).should().getPatientDetail(hospital.getInstitutionNumber(), patient.getRegistrationNumber());
        }

        @Test
        @DisplayName("실패 - 병원 아이디 오류")
        void fail1() {
            // given

            given(hospitalRepository.findByInstitutionNumber(anyString())).willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> patientService.getPatientDetail("", "")).isInstanceOf(OptionalObjectNullException.class);

            // then
            then(patientService).should().getPatientDetail("", "");
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
            assertThatThrownBy(() -> patientService.getPatientDetail("", "")).isInstanceOf(OptionalObjectNullException.class);

            // then
            then(patientService).should().getPatientDetail("", "");
        }
    }

}