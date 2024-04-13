import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;

public class mongo {
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> collection;

    static {
        mongoClient = MongoClients.create("mongodb+srv://admin:68071299@cluster0.vvgixko.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0");
        database = mongoClient.getDatabase("SearchEngine");
        collection = database.getCollection("SearchEngine");
    }

    public static int df_count(String word) {
        Document document = collection.find(new Document("_id", word)).first();
        if (document != null) return document.getInteger("count");
        else return 0;
    }

    public static int tf_count(String word, String doc) {
        Document document = collection.find(new Document("_id", word)).first();
        if (document != null) {
            @SuppressWarnings("unchecked")
            ArrayList<Document> documents = (ArrayList<Document>) document.get("documents");
            for (Document document1 : documents) {
                if (document1.get("doc_id").equals(doc)) {
                    return document1.getInteger("tf");
                }
            }
        }
        return 0;
    }

    public static ArrayList<String> metadata(String word, String doc) {
        Document document = collection.find(new Document("_id", word)).first();
        if (document != null) {
            @SuppressWarnings("unchecked")
            ArrayList<Document> documents = (ArrayList<Document>) document.get("documents");
            for (Document document1 : documents) {
                if (document1.get("doc_id").equals(doc)) {
                    return (ArrayList<String>) document1.get("metadata");
                }
            }
        }
        return null;
    }

    public static void connectmongo(String word, String docId, String metadata) {
        try {
            Document result = collection.find(new Document("_id", word)).first();
            if (result != null) {
                @SuppressWarnings("unchecked")
                ArrayList<Document> documents = (ArrayList<Document>) result.get("documents");
                int resultindex = -1;
                for (int i = 0; i < documents.size(); i++) {
                    if (documents.get(i).get("doc_id").equals(docId)) {
                        resultindex = i;
                        break;
                    }
                }
                if (resultindex != -1) {
                    Document document = documents.get(resultindex);
                    int tf = document.getInteger("tf") + 1;
                    document.put("tf", tf);
                    @SuppressWarnings("unchecked")
                    ArrayList<String> metadatas = (ArrayList<String>) document.get("metadata");
                    metadatas.add(metadata);
                    document.put("metadata", metadatas);
                    collection.updateOne(
                            new Document("_id", word),
                            new Document("$set", new Document("documents." + resultindex, document))
                    );
                } else {
                    ArrayList<String> metadatas = new ArrayList<>();
                    metadatas.add(metadata);
                    collection.updateOne(
                            new Document("_id", word),
                            new Document("$push", new Document("documents",
                                    new Document("doc_id", docId)
                                            .append("metadata", metadatas)
                                            .append("tf", 1)
                            ))
                    );
                    collection.updateOne(
                            new Document("_id", word),
                            new Document("$inc", new Document("count", 1))
                    );
                }
            } else {
                ArrayList<Document> documents = new ArrayList<>();
                ArrayList<String> metadatas = new ArrayList<>();
                metadatas.add(metadata);
                documents.add(new Document("doc_id", docId)
                        .append("metadata", metadatas)
                        .append("tf", 1));
                collection.insertOne(new Document("_id", word)
                        .append("documents", documents)
                        .append("count", 1)
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
