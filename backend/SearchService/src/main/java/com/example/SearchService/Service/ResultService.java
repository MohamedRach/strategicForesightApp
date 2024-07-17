package com.example.SearchService.Service;

import com.example.SearchService.Domain.Result;
import com.example.SearchService.Repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Service
public class ResultService {

    private final ResultRepository resultRepository;

    @Autowired
    public ResultService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    public void CreateResult(ArrayList<Result> results){
        for(Result result:results){
            resultRepository.save(result);
        }
    }
}
