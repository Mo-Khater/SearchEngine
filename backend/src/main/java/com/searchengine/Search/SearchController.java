package com.searchengine.Search;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController

@RequestMapping("/api")
public class SearchController {


    @Autowired
    private QueryService queryService;

    @Autowired
    private IndexerRepository indexerRepository;



    @GetMapping("/search")
    public ResponseEntity<List<ArrayList<String>>> search(@RequestParam String keyword) {

        List<ArrayList<String>> result = queryService.searchly(keyword);


        return new ResponseEntity<List<ArrayList<String>>>(result,HttpStatus.OK);
    }
}
