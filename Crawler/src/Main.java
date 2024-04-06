import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;



public class Main {
    public static void main(String[] args) {
        int number_threads=3;
        ArrayList<String>Start_urls = new ArrayList<String>();
        ArrayList<Crawler>Robots=new ArrayList<>();
        Object to_sync=new Object();
        ArrayList<String>Seeds = new ArrayList<String>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("Start_Urls.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Start_urls.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        int n_url = Start_urls.size()/number_threads;
        for(int i=0;i<number_threads;i++)
        {
            int start=i*n_url,end=(i*n_url + n_url == 0) ? 1:i*n_url + n_url;
            Robots.add(new Crawler(i,new ArrayList<>(Start_urls.subList(start,end)),Seeds,100,to_sync));
        }
        for(Crawler w : Robots) w.start_thread();
        long Start_time = System.currentTimeMillis();
        for(Crawler w : Robots) {
            try {
                w.getThread().join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        long End_time = System.currentTimeMillis();
        long Total_time = (End_time-Start_time)/1000 ;
        System.out.println("it takes:" + Total_time +" seconds to finish");
        System.out.println(Seeds.size());
    }
}
