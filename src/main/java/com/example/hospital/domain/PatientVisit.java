package com.example.hospital.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PatientVisit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;
    @Column(length = 10, nullable = false)
    private LocalDateTime regDate;
    @Column(length = 10, nullable = false)
    private String statusCode;
    @ManyToOne(fetch = FetchType.LAZY)
    private Code status;

    @Builder
    public PatientVisit(Hospital hospital, Patient patient, String statusCode, Code status) {
        this.hospital = hospital;
        this.patient = patient;
        this.regDate = LocalDateTime.now();
        this.statusCode = statusCode;
        this.status = status;
    }

    public void updateStatus(Code status) {
        this.status = status;
        this.statusCode = status.getCd();
    }
}
