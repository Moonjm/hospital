package com.example.hospital.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CodeGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 10, nullable = false)
    private String cd;
    @Column(length = 10, nullable = false)
    private String name;
    @Column(length = 100, nullable = false)
    private String description;

    @OneToMany(mappedBy = "codeGroup")
    private List<Code> codes = new ArrayList<>();

    @Builder
    public CodeGroup(String cd, String name, String description) {
        this.cd = cd;
        this.name = name;
        this.description = description;
    }
}
