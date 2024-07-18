package com.example.SearchService.Repository;

import com.example.SearchService.Domain.Result;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends ElasticsearchRepository<Result, String> {

}
