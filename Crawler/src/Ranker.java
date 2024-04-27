import java.util.HashMap;
import java.util.HashSet;

public class Ranker {
    private static final double BETA = 0.85;
    private double eps;
    private HashMap<String, HashSet<String>> Graph;
    private HashMap<String, Double> pageRanks;
    private int numIter;
    private boolean isPageRankedDone;

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


}
