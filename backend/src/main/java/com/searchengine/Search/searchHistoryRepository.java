package com.searchengine.Search;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface searchHistoryRepository extends MongoRepository<searchHistory, String> {

    boolean existsByTextIgnoreCase(String txt) ;

    List<searchHistory> findDistinctByTextStartingWithIgnoreCaseOrderByCountDesc(String txt);
    searchHistory findFirstByTextIgnoreCase (String txt) ;
}
