package com.example.hospital.controller;

import com.example.hospital.dto.PatientDto.InsertPatientRequest;
import com.example.hospital.dto.PatientDto.ModifyPatientRequest;
import com.example.hospital.model.response.BasicResponse;
import com.example.hospital.service.PatientService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequiredArgsConstructor
@Validated
public class PatientController {

    private final PatientService patientService;

    @PostMapping("/v1/patients")
    public ResponseEntity<? extends BasicResponse> insertPatient(
            @RequestBody @Validated InsertPatientRequest dto,
            WebRequest webRequest
    ) {
        webRequest.setAttribute("body", dto, RequestAttributes.SCOPE_REQUEST);
        return ResponseEntity.status(HttpStatus.OK).body(patientService.insertPatient(dto));
    }

    @GetMapping("/v1/patients")
    public ResponseEntity<? extends BasicResponse> getPatients(
            @RequestParam(value = "hospitalId", required = false, defaultValue = "1") Long hospitalId,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") @Min(value = 0) int pageSize,
            @RequestParam(value = "pageNo", required = false, defaultValue = "1") @Min(1) int pageNo,
            @RequestParam(value = "type", required = false, defaultValue = "") String type,
            @RequestParam(value = "value", required = false, defaultValue = "") String value
    ) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.Direction.DESC, "id");
        return ResponseEntity.status(HttpStatus.OK).body(patientService.getPatients(hospitalId, pageable, type, value));
    }

    @PutMapping("/v1/patients")
    public ResponseEntity<? extends BasicResponse> modifyPatient(
            @RequestBody @Validated ModifyPatientRequest dto,
            WebRequest webRequest
    ) {
        webRequest.setAttribute("body", dto, RequestAttributes.SCOPE_REQUEST);
        return ResponseEntity.status(HttpStatus.OK).body(patientService.modifyPatient(dto));
    }

    @DeleteMapping("/v1/patients/{id}")
    public ResponseEntity<? extends BasicResponse> deletePatient(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(patientService.deletePatient(id));
    }

    @GetMapping("/v1/patients/{id}")
    public ResponseEntity<? extends BasicResponse> getPatientDetail(
            @PathVariable("id") Long id,
            @RequestParam(value = "hospitalId", required = false, defaultValue = "1") Long hospitalId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(patientService.getPatientDetail(id, hospitalId));
    }

}
