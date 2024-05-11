package com.searchengine.Search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class searchHistoryService {
    @Autowired
    private  searchHistoryRepository searchhistoryrepository  ;

    public void saveSearch(String txt){
        if(!searchhistoryrepository.existsByTextIgnoreCase(txt)){
            searchHistory searchHis = new searchHistory() ;
            searchHis.setCount(1);
            searchHis.setText(txt);
            searchhistoryrepository.save(searchHis);
        }else{
            searchHistory searchHis =searchhistoryrepository.findFirstByTextIgnoreCase(txt);
            searchHis.setCount(searchHis.getCount()+1);
            searchhistoryrepository.save(searchHis);
        }
    }

    public ArrayList<String> getSuggestions (String txt){
        List <searchHistory> result = searchhistoryrepository.findDistinctByTextStartingWithIgnoreCaseOrderByCountDesc(txt) ;
        ArrayList <String> ret = new ArrayList<>();
        for (searchHistory s : result) {
            ret.add(s.getText());
        }
        return ret;
    }
}
