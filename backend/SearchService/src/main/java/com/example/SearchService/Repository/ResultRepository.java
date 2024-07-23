package com.example.SearchService.Repository;

import com.example.SearchService.Domain.Result;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends ElasticsearchRepository<Result, String> {
    List<Result> findByKeywordInAndSourceIn(List<String> keyword, List<String> source);
}
