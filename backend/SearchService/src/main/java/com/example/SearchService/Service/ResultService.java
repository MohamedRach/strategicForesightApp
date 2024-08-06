package com.example.SearchService.Service;

import com.example.SearchService.Domain.Result;
import com.example.SearchService.Repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResultService {

    private final ResultRepository resultRepository;

    @Autowired
    public ResultService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    public void CreateResult(ArrayList<Result> results){
        resultRepository.saveAll(results);
    }

    public List<Result> getResultsByKeywordsAndSources(List<String> keywords, List<String> sources) {
        return resultRepository.findByKeywordInAndSourceIn(keywords, sources);
    }

    public Result getResultByCaption(String caption) {
        return resultRepository.findResultByCaption(caption);
    }

    public void DeleteResult(String id){
        resultRepository.deleteById(id);
    }
}
