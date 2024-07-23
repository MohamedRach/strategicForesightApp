package com.example.SearchService.Repository;

import com.example.SearchService.Domain.SearchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository extends JpaRepository<SearchEntity, Long> {
}

