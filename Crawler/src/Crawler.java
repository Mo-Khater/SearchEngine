import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.github.itechbear.robotstxt.RobotsMatcher;

import javax.swing.*;

public class Crawler implements Runnable{

    private int maxsize;
    private final int ID;
    private SharedMemory memory;


    public Crawler(int id, SharedMemory s_m, int max_s)
    {
        ID=id;
        maxsize=max_s;
        memory=s_m;
    }


    @Override
    public void run() {
        start_crawl();
    }

    private void start_crawl()
    {
        while (true) {
            String url = memory.queue_poll();
            if (url == null) {
                if (memory.visited_size() >= maxsize) {
                    return;
                }
            } else {
                int status=allowedRobot(url);
                if (status==1)
                {
                    crawl(url);
                }
                else if(status==0)
                {
                    memory.queue_offer(url);
                }
                if (memory.visited_size() >= maxsize) {
                    return;
                }
            }
        }
    }

    private void crawl(String url)
    {
        Document doc=request(url);
        if(doc != null) {
            for (Element link : doc.select("a[href]")) {
                String nextlink = link.absUrl("href");

                URI originalURI = null;
                URI normalizedURI = null;

                // now check if there exists a malformed URL
                try {;
                    originalURI = new URI(nextlink);
                    URL  check = originalURI.toURL(); // THIS LINE WILL THROW EXCEPTION IF URL IS INVALID
//                    System.out.println("original URI (next link): " + originalURI);

                    normalizedURI = new URI(originalURI.getScheme(), originalURI.getAuthority(), originalURI.getPath(), null, null);
//                    System.out.println("Normalized URI :" + normalizedURI);
                } catch (URISyntaxException | MalformedURLException e) {
                    // uncomment these lines to see the exception happen
//                    System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
//                    throw new RuntimeException(e);
                }


                if(normalizedURI != null && !memory.map_contains(normalizedURI.toString()))
                {
                    memory.map_add(normalizedURI.toString());
                    memory.queue_offer(normalizedURI.toString());
//                    System.out.println("ADDED TO QUEUE: "+ normalizedURI);
                }
            }
        }
    }

    private Document request(String url)
    {
        try{
            Connection con = Jsoup.connect(url);
            Document doc = con.get();
            if(con.response().statusCode() == 200)
            {
                memory.visited_add(url);
                memory.map_add(url);
                System.out.println("thread "+ ID + ": added Link: " + url);
                System.out.println(doc.title());
                System.out.println(memory.visited_size());
                return doc;
            }
            throw new IOException();
        }
        catch (IOException e)
        {
            memory.queue_offer(url);
            return null;
        }
    }

    public static String getRobotUrl(String site)
    {
        URL a;
        try {
            // get the address of robots.txt
            a = new URI(site).toURL();
            String s = a.getProtocol() + "://" + a.getHost().toString() + "/robots.txt";
            return s;
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String fetchRobotsTxtOfSite(String str)
    {
        String roboturl = getRobotUrl(str);
        String data = "";
        try {
            Connection con = Jsoup.connect(roboturl);
            Document doc = con.get();
            if (con.response().statusCode() == 200) {
                data = doc.wholeText();
            }
        } catch (IOException e) {
            System.out.println("cant connect: " + roboturl);
            data="cant connect";
        }
        return data;
    }

    public static int allowedRobot(String website) {
        RobotsMatcher matcher = new RobotsMatcher();
        String robotstxt = fetchRobotsTxtOfSite(website);
        if(robotstxt.equals("cant connect")) return 0;
        boolean a = matcher.OneAgentAllowedByRobots(robotstxt, "*", website);
//        System.out.println(a);
//        System.out.println(robotstxt);
        return (a)? 1:-1;
    }
}