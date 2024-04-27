import java.util.HashMap;
import java.util.HashSet;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.ArrayList;
import java.lang.Math;


public class Ranker {
    private static final double BETA = 0.85;
    private double eps;
    private HashMap<String, HashSet<String>> Graph;
    private HashMap<String, Double> pageRanks;
    private int numIter;
    private boolean isPageRankedDone;

    // To connect to Mongo and update db
    static MongoClient mongoClient= MongoClients.create("mongodb+srv://admin:68071299@cluster0.vvgixko.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0");
    static MongoDatabase database = mongoClient.getDatabase("SearchEngine");
    static MongoCollection<Document> collection = database.getCollection("TF_IDF_Test");

    // test TF-IDF only
    public static void main(String[] args) {
        Ranker.appendTF_IDF("love");
    }

    public Ranker(double e,HashMap<String, HashSet<String>> g){
        eps = e;
        Graph = g;
        pageRanks = new HashMap<>();
        isPageRankedDone =false;
    }

    public void calcPageRank() {
        numIter = 0;
        HashMap<String, Double> pr = new HashMap<String, Double>();
        for(String s : Graph.keySet()) {
            pr.put(s, 1.0 /(double) Graph.size());
        }

        double diff = Double.MAX_VALUE;
        HashMap<String, Double> nextIter;
        while(diff > eps) {
            nextIter = singleIterationCalcPageRank(pr);
            diff = diff(nextIter, pr);
            pr = nextIter;
            numIter++;
        }
        pageRanks = pr;
        isPageRankedDone = true;
        System.out.println(pageRanks);
    }

    private HashMap<String, Double> singleIterationCalcPageRank(HashMap<String, Double> pr) {
        HashMap<String, Double> newPageRanks = new HashMap<>();
        double sumOfRanks = 0.0;

        // Contribution from incoming edges
        for (String incomingNode : Graph.keySet()) {
            double newPageRank = (1 - BETA); // Initial value (damping factor)
            if (Graph.get(incomingNode) != null && !Graph.get(incomingNode).isEmpty()) {
                for (String outgoingNode : Graph.get(incomingNode)) {
                    if (Graph.containsKey((outgoingNode)))
                        newPageRank += BETA * (pr.get(outgoingNode) / (double) Graph.get(outgoingNode).size());
                }
            }
            newPageRanks.put(incomingNode, newPageRank);
            sumOfRanks += newPageRank;

        }
        // Normalize PageRank values
        for (String node : newPageRanks.keySet()) {
            newPageRanks.put(node, newPageRanks.get(node) / sumOfRanks);
        }

        return newPageRanks;
    }

    private double diff(HashMap<String, Double> nextIter, HashMap<String, Double> prevIter) {
        double sum = 0;
        for(String s : Graph.keySet()) {
            sum += Math.abs(nextIter.get(s) - prevIter.get(s));
        }
        return(Math.sqrt(sum));
    }

    public HashMap<String, Double> getPageRanks() {return pageRanks;}

    public int numberOfIteration() {return numIter;}

    public boolean get_isPageRankDone() { return isPageRankedDone;}


    /**
     * This function takes a token and calculates the TF-IDF for all the documents
     * in which this word appears.
     * */
    public static void appendTF_IDF(String token) {
        System.out.println("Appending TF-IDF for the token '" + token+"'");
        long countAll = collection.countDocuments();
        // Normalized TF = term count / number of words in doc
        // IDF = log (#docs in db / DF)
        // TF_IDF = nTF * IDF
        Document filter = new Document("_id", token);
        Document doc = collection.find(filter).first();
        assert doc != null;
        ArrayList<Document> documents = (ArrayList<Document>) doc.get("documents");
        long DF = documents.size();
        double IDF = Math.log((double) countAll / DF);
        int i = 0;
        for (Document d : documents) {
            int tf = d.getInteger("tf");
            int numWords = d.getInteger("numWords");
            double normTF = (double) tf / numWords;
            double TFIDF = IDF * normTF;
            d.append("TF_IDF", TFIDF);
            Document update = new Document();
            update.append("$set", new Document("documents." + i, d));
            collection.updateOne(filter, update);
            System.out.println(d.get("doc_id") +"\t" + TFIDF);
            i++;
        }

    }

}
