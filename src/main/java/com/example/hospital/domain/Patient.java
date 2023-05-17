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
    @Column(length = 10, nullable = false)
    private String genderCode;
    @Column(length = 10)
    private String birth;
    @Column(length = 10)
    private String birthDate;
    @Column(length = 20)
    private String mobileNumber;
    private LocalDateTime lastVisit;
    @ManyToOne(fetch = FetchType.LAZY)
    private Code gender;

    @Builder
    public Patient(Hospital hospital, String name, String registrationNumber, String genderCode, String birth, String birthDate, String mobileNumber,
                   LocalDateTime lastVisit, Code gender) {
        this.hospital = hospital;
        this.name = name;
        this.registrationNumber = registrationNumber;
        this.genderCode = genderCode;
        this.birth = birth;
        this.birthDate = birthDate;
        this.mobileNumber = mobileNumber;
        this.lastVisit = lastVisit;
        this.gender = gender;
    }
}
