package com.example.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

public class VisitDto {

    @Data
    public static class ModifyVisitRequest {
        @NotBlank(message = "status 공백일 수 없습니다.")
        private String status;
    }
}
