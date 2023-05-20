package com.example.hospital.controller;

import com.example.hospital.dto.VisitDto.ModifyVisitRequest;
import com.example.hospital.dto.VisitDto.VisitRequest;
import com.example.hospital.model.response.BasicResponse;
import com.example.hospital.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequiredArgsConstructor
@Validated
public class VisitController {

    private final VisitService visitService;

    @PostMapping("/v1/visits")
    public ResponseEntity<? extends BasicResponse> insertVisit(
            @RequestBody @Validated VisitRequest dto,
            WebRequest webRequest
    ) {
        webRequest.setAttribute("body", dto, RequestAttributes.SCOPE_REQUEST);
        return ResponseEntity.status(HttpStatus.OK).body(visitService.insertPatient(dto));
    }

    @PutMapping("/v1/visits")
    public ResponseEntity<? extends BasicResponse> modifyVisit(
            @RequestBody @Validated ModifyVisitRequest dto,
            WebRequest webRequest
    ) {
        webRequest.setAttribute("body", dto, RequestAttributes.SCOPE_REQUEST);
        return ResponseEntity.status(HttpStatus.OK).body(visitService.modifyPatient(dto));
    }
}
