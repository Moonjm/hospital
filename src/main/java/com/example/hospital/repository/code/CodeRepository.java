package com.example.hospital.repository.code;

import com.example.hospital.domain.Code;
import com.example.hospital.domain.CodeGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodeRepository extends JpaRepository<Code, Long> {
    Optional<Code> findByCodeGroupAndCd(CodeGroup codeGroup, String cd);
}
