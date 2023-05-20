package com.example.hospital.dto;

import com.example.hospital.domain.Patient;
import com.example.hospital.domain.PatientVisit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class PatientDto {

    @Data
    public static class InsertPatientRequest {
        @NotNull
        private Long hospitalId;
        @Size(max = 45, message = "name은 45글자 이하로 입력해 주세요.")
        @NotBlank(message = "name은 공백일 수 없습니다.")
        private String name;
        private String gender;
        private String birthDate;
        private String mobileNumber;
    }

    @Data
    public static class GetPatientsResponse {
        private Long patientId;
        private String name;
        private String gender = "모름";
        private String birthDate = "-";
        private String mobileNumber = "-";
        private String lastVisit = "-";

        public GetPatientsResponse(Patient patient) {
            this.patientId = patient.getId();
            this.name = patient.getName();
            if (patient.getGender() != null) {
                this.gender = patient.getGender().getName();
            }
            if (patient.getBirthDate() != null) {
                this.birthDate = patient.getBirthDate();
            }
            if (patient.getMobileNumber() != null) {
                this.mobileNumber = patient.getMobileNumber();
            }
            if (patient.getLastVisit() != null) {
                this.lastVisit = patient.getLastVisit().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        }
    }

    @Data
    public static class ModifyPatientRequest {
        @NotNull
        private Long patientId;
        @Size(max = 45, message = "name은 45글자 이하로 입력해 주세요.")
        @NotBlank(message = "name은 공백일 수 없습니다.")
        private String name;
        private String gender;
        private String birthDate;
        private String mobileNumber;
    }

    @Data
    public static class GetPatientDetailResponse {
        private Long patientId;
        private String name;
        private String registrationNumber;
        private String gender = "모름";
        private String birthDate = "-";
        private String mobileNumber = "-";
        private List<PatientVisitDto> visits;

        public GetPatientDetailResponse(Patient patient) {
            this.patientId = patient.getId();
            this.name = patient.getName();
            this.registrationNumber = patient.getRegistrationNumber();
            if (patient.getGender() != null) {
                this.gender = patient.getGender().getName();
            }
            if (patient.getBirthDate() != null) {
                this.birthDate = patient.getBirthDate();
            }
            if (patient.getMobileNumber() != null) {
                this.mobileNumber = patient.getMobileNumber();
            }
        }
    }

    @Data
    public static class PatientVisitDto {
        private Long visitId;
        private String regDate;
        private String status;

        public PatientVisitDto(PatientVisit patientVisit) {
            this.visitId = patientVisit.getId();
            this.regDate = patientVisit.getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            this.status = patientVisit.getStatus().getName();
        }
    }
}
