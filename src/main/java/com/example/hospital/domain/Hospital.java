package com.example.hospital.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 45, nullable = false)
    private String name;
    @Column(length = 20, nullable = false)
    private String institutionNumber;
    @Column(length = 10, nullable = false)
    private String directorName;

    @Builder
    public Hospital(String name, String institutionNumber, String directorName) {
        this.name = name;
        this.institutionNumber = institutionNumber;
        this.directorName = directorName;
    }
}
