import java.io.FileReader;
import java.net.URI;
import java.net.URL;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;


public class Main {

    public static void main(String[] args) {
        int number_threads = 5;
        int maxsize = 3;
        ArrayList<Pair<String,String>>Start_urls = new ArrayList<Pair<String,String>>();
        ArrayList<String>visited_urls = new ArrayList<String>();
        SharedMemory memory = new SharedMemory();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("Start_Urls.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Normalize URLs before adding them to Start_urls
                String[] parts = line.split(" ");
                String first = parts[0];
                String second = null;
                if(parts.length > 1) {
                    second = parts[1];
                }
                URI orgURI = new URI(first);
                URL check = orgURI.toURL(); // THIS LINE WILL THROW EXCEPTION IF URL IS INVALID

                URI normUrl = new URI(orgURI.getScheme(), orgURI.getAuthority(), orgURI.getPath(), null, null);
                if (!normUrl.toString().isEmpty() && !Start_urls.contains(normUrl.toString())) {
                    Start_urls.add(new Pair<>(normUrl.toString(),second));
                }
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader("visited.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                visited_urls.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        CrawlerSystem c=new CrawlerSystem(number_threads,memory,Start_urls,visited_urls,"Start_Urls.txt",maxsize);
        c.Start();


//        String s = memory.queue_poll();
//        while (s != null) {
//            System.out.println(s);
//            s = memory.queue_poll();
//        }

        System.out.println("queue size : " + memory.queue_size());
        System.out.println("Graph size : " + memory.Graph_size());
        System.out.println("# Visited URLs = "+ memory.visited_size());
        System.out.println("Time taken = " + c.get_time());
//        System.out.println(memory.get_Graph());
//        for (HashMap.Entry<String, HashSet<String>> entry : memory.get_Graph().entrySet()) {
//            String key = entry.getKey();
//            int setSize = entry.getValue().size();
//            System.out.println(key + " " + setSize);
//        }



        //test rankpage algo
//        System.out.println("pagerank algorithm here");
//        Ranker rank = new Ranker(0,memory.get_Graph());
//        rank.calcPageRank();
//        while(!rank.get_isPageRankDone()){}
//        System.out.println("time taken to calculate pagerank : " + rank.time_taken);
    }
}

//https://www.wikipedia.org/