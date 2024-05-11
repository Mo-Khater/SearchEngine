package com.searchengine.Search;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "SearchHistory")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class searchHistory {
    @Id
    private String ID ;
    private String text ;
    private int count ;
}
