package com.sideproject.hororok.keword.repository;

import com.sideproject.hororok.keword.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    Optional<Keyword> findByName(String name);

}
