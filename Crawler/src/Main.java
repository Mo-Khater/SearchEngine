import java.net.URI;
import java.net.URL;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;



public class Main {

    public static void main(String[] args) {
        int number_threads=3;
        ArrayList<String>Start_urls = new ArrayList<String>();
        shared_memory memory = new shared_memory();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("Start_Urls.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Normalize URLs before adding them to Start_urls
                URI orgURI = new URI(line);
                URL check = orgURI.toURL(); // THIS LINE WILL THROW EXCEPTION IF URL IS INVALID

                URI normUrl = new URI(orgURI.getScheme(), orgURI.getAuthority(), orgURI.getPath(), null, null);
                if (!normUrl.toString().isEmpty() && !Start_urls.contains(normUrl.toString())) {
                    Start_urls.add(normUrl.toString());
                }
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }


        Crawler_System c=new Crawler_System(number_threads,memory,Start_urls);
        c.Start();

        System.out.println("# Visited URLs = "+ memory.visited_size());
        System.out.println("Time taken = " + c.get_time());

    }
}