package com.taufer.tales.repo;

import com.taufer.tales.domain.Tale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaleRepository extends JpaRepository<Tale, Long> {
    Page<Tale> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String t, String a, Pageable pg);
}
