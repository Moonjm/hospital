package com.example.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

public class VisitDto {

    @Data
    public static class VisitRequest {
        @NotNull(message = "hospitalId는 공백일 수 없습니다.")
        private Long hospitalId;
        @NotNull(message = "patientId는 공백일 수 없습니다.")
        private Long patientId;
    }

    @Data
    public static class ModifyVisitRequest {
        @NotNull(message = "visitId는 공백일 수 없습니다.")
        private Long visitId;
        @NotBlank(message = "visitId는 공백일 수 없습니다.")
        private String status;
    }
}
