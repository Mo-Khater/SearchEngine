import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import javax.print.Doc;


public class Crawler implements Runnable{

    private final int maxsize;
    private ArrayList<String> mainvisited;
    private ArrayList<String> suppvisited=new ArrayList<String>();
    private Thread thread;
    private Queue<String> queue;
    private final int ID;
    private final Object o;


    public Crawler(int id,ArrayList<String>s,ArrayList<String>v,int max_s,Object to_sync)
    {
        ID=id;
        o=to_sync;
        mainvisited=v;
        maxsize=max_s;
        queue=new LinkedList<>();
        for(int e=0;e<s.size();e++)
            queue.offer(s.get(e));
        thread=new Thread(this);
    }

    public void start_thread()
    {
     thread.start();
    }

    @Override
    public void run() {
        while(!queue.isEmpty() && suppvisited.size() != maxsize)
        {
            String url = queue.poll();
            crawl(url);
        }
    }

    private void crawl(String url)
    {
        Document doc=request(url);
        if(doc != null) {
            for (Element link : doc.select("a[href]")) {
                String nextlink = link.absUrl("href");
                if (!queue.contains(nextlink) && !suppvisited.contains(nextlink))
                    queue.offer(nextlink);
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
                if(add_url(url))
                {
//                System.out.println("thread "+ ID + ": added Link: " + url);
//                System.out.println(doc.title());
                return doc;
                }
            }
            return null;
        }
        catch (IOException e)
        {
            return null;
        }
    }

    private boolean add_url(String url)
    {
        synchronized (o)
        {
            if(!mainvisited.contains(url)) {
                mainvisited.add(url);
                suppvisited.add(url);
                return true;
            }
            return false;
        }
    }

    public Thread getThread()
    {
        return thread;
    }

}