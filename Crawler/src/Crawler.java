import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;



public class Crawler implements Runnable{

    private int maxsize;
    private final int ID;
    private shared_memory memory;


    public Crawler(int id,shared_memory s_m,int max_s)
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
                crawl(url);
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

                URI originalUrl = null;
                URI normalizedUrl = null;

                // now check if there exists a malformed URL
                try {;
                    originalUrl = new URI(nextlink);
                    System.out.println("original URL (next link): " + originalUrl);

                    normalizedUrl = new URI(originalUrl.getScheme(), originalUrl.getAuthority(), originalUrl.getPath(), null, null);
                    System.out.println("Normalized Url :" + normalizedUrl);
                } catch (URISyntaxException e) {
                    // uncomment these lines to see the exception happen
//                    System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
//                    throw new RuntimeException(e);
                }


                if(normalizedUrl != null && !memory.map_contains(normalizedUrl.toString()))
                {
                    memory.map_add(normalizedUrl.toString());
                    memory.queue_offer(normalizedUrl.toString());
                    System.out.println("ADDED TO QUEUE: "+ normalizedUrl);
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
                return doc;
            }
            return null;
        }
        catch (IOException e)
        {
            return null;
        }
    }



}