import java.util.ArrayList;




public class Crawler_System {
    private int number_threads;
    private shared_memory memory;
    ArrayList<String> Start_urls;
    ArrayList<Thread> threads=new ArrayList<>();
    private long time_taken;

    Crawler_System(int s,shared_memory s_m,ArrayList<String> s_u)
    {
        number_threads = s;
        memory = s_m;
        Start_urls = s_u;
    }

    public void Start()
    {
        for(String s:Start_urls)
        {
            memory.queue_offer(s);
            memory.map_add(s);
        }
        for(int i=0;i<number_threads;i++)
            threads.add(new Thread(new Crawler(i,memory,300)));

        for(Thread t:threads)t.start();

        long start_time = System.currentTimeMillis();

        for(Thread t:threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long end_time = System.currentTimeMillis();

        time_taken = (end_time-start_time)/1000;
    }

    public long get_time()
    {
        return time_taken;
    }


}
