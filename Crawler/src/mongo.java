import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;

public class mongo {
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> collection;
    private static MongoCollection<Document> collection_Pagerank;

    static {
        mongoClient = MongoClients.create("mongodb+srv://admin:68071299@cluster0.vvgixko.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0");
        database = mongoClient.getDatabase("SearchEngine");
        collection = database.getCollection("CrawlerPages");
        collection_Pagerank = database.getCollection("Pagerank");
    }

    public static void insert_pagerank(HashMap<String,Double> pageRanks){
        for (HashMap.Entry<String, Double> e : pageRanks.entrySet()) {
            collection_Pagerank.insertOne(new Document("url",e.getKey()).append("pagerank",e.getValue()));
        }
    }

    public static void remove_collection_Pagerank(){
        collection_Pagerank.drop();
    }

    public static void remove_collection_Crawler(){
        collection.drop();
    }

    public static void insert_crawler(String url, String doc) {
        collection.insertOne(new Document("url",url).append("doc",doc).append("dummy",1));
    }

}
