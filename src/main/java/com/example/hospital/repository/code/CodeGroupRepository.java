package com.example.hospital.repository.code;

import com.example.hospital.domain.CodeGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodeGroupRepository extends JpaRepository<CodeGroup, Long> {
    Optional<CodeGroup> findByCd(String cd);
}
