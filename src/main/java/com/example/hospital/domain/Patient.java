package com.example.hospital.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;
    @Column(length = 45, nullable = false)
    private String name;
    @Column(length = 13, nullable = false)
    private String registrationNumber;
    @Column(length = 10)
    private String genderCode;
    @Column(length = 10)
    private String birthDate;
    @Column(length = 20)
    private String mobileNumber;
    private LocalDateTime lastVisit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gender_id")
    private Code gender;
    @OneToMany(mappedBy = "patient")
    List<PatientVisit> visits = new ArrayList<>();

    @Builder
    public Patient(Hospital hospital, String name, String genderCode, String birthDate, String mobileNumber,
                   LocalDateTime lastVisit, Code gender) {
        this.hospital = hospital;
        this.name = name;
        this.registrationNumber = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmms")).substring(0, 13);
        this.genderCode = genderCode;
        this.birthDate = birthDate;
        this.mobileNumber = mobileNumber;
        this.lastVisit = lastVisit;
        this.gender = gender;
    }

    public void updatePatient(String name, Code gender, String birthDate, String mobileNumber) {
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.genderCode = gender.getCd();
        this.mobileNumber = mobileNumber;
    }

    public void updateLastVisit(LocalDateTime lastVisit) {
        this.lastVisit = lastVisit;
    }
}
