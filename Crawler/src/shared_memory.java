import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;






public class shared_memory {
    private ConcurrentLinkedQueue<String> queue;
    private ConcurrentHashMap<String, Boolean> map;
    private ConcurrentHashMap<String, Boolean> visited;


    shared_memory()
    {
        queue=new ConcurrentLinkedQueue<>();
        map = new ConcurrentHashMap<>();
        visited = new ConcurrentHashMap<>();
    }


    public void map_add(String element) {
        map.put(element,true) ;
    }

    public boolean map_contains(String element) {
        return map.containsKey(element);
    }

    public boolean map_remove(String element) {
        return map.remove(element) != null;
    }

    public void queue_offer(String element) {
        queue.offer(element);
    }

    public boolean queue_contains(String element) {
        return queue.contains(element);
    }

    public String queue_poll() {
        if(queue.isEmpty())return null;
        return queue.poll();
    }

    public void visited_add(String e)
    {
        visited.put(e,true);
    }

    public long visited_size()
    {
        return visited.size();
    }

}
