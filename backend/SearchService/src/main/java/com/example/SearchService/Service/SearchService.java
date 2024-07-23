package com.example.SearchService.Service;

import com.example.SearchService.Domain.SearchEntity;
import com.example.SearchService.Repository.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {
    private final SearchRepository searchRepository;

    @Autowired
    public SearchService(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    public List<SearchEntity> findAll() {
        return searchRepository.findAll();
    }

    public SearchEntity findById(Long id) {
        return searchRepository.findById(id).orElse(null);
    }

    public SearchEntity save(SearchEntity search) {
        return searchRepository.save(search);
    }

    public void deleteById(Long id) {
        searchRepository.deleteById(id);
    }
}

