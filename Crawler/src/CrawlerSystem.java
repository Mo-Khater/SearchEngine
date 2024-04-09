import java.util.ArrayList;




public class CrawlerSystem {
    private final int number_threads;
    private SharedMemory memory;
    ArrayList<String> Start_urls;
    ArrayList<Thread> threads=new ArrayList<>();
    private long time_taken;
    private final String filepath;

    CrawlerSystem(int s, SharedMemory s_m, ArrayList<String> s_u,String fp)
    {
        number_threads = s;
        memory = s_m;
        Start_urls = s_u;
        filepath=fp;
    }

    public void Start()
    {
        for(String s:Start_urls)
        {
            memory.queue_offer(s);
            memory.map_add(s);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            memory.saveState(filepath);
            System.out.println("State Saved Successfully");
            Runtime.getRuntime().halt(0);
        }));

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
