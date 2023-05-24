package com.example.hospital.controller;

import com.example.hospital.dto.VisitDto.ModifyVisitRequest;
import com.example.hospital.model.response.BasicResponse;
import com.example.hospital.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequiredArgsConstructor
@Validated
public class VisitController {

    private final VisitService visitService;

    @PostMapping("/v1/hospitals/{hospitalId}/patients/{patientId}/visits")
    public ResponseEntity<? extends BasicResponse> insertVisit(
            @PathVariable("hospitalId") String hospitalId,
            @PathVariable("patientId") String patientId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(visitService.insertVisit(hospitalId, patientId));
    }

    @PutMapping("/v1/hospitals/{hospitalId}/patients/{patientId}/visits/{visitId}")
    public ResponseEntity<? extends BasicResponse> modifyVisit(
            @PathVariable("hospitalId") String hospitalId,
            @PathVariable("patientId") String patientId,
            @PathVariable("visitId") Long visitId,
            @RequestBody @Validated ModifyVisitRequest dto,
            WebRequest webRequest
    ) {
        webRequest.setAttribute("body", dto, RequestAttributes.SCOPE_REQUEST);
        return ResponseEntity.status(HttpStatus.OK).body(visitService.modifyVisit(hospitalId, patientId, visitId, dto));
    }
}
