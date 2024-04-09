import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;



public class Main {

    public static void main(String[] args) {
        int number_threads=3;
        ArrayList<String>Start_urls = new ArrayList<String>();
        SharedMemory memory = new SharedMemory();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("Start_Urls.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Start_urls.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        CrawlerSystem c=new CrawlerSystem(number_threads,memory,Start_urls,"Start_Urls.txt");
        c.Start();

        System.out.println(memory.visited_size());
        System.out.println(c.get_time());

    }
}

//https://www.wikipedia.org/