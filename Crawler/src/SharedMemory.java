import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.FileWriter;
import java.io.IOException;




public class SharedMemory {
    private ConcurrentLinkedQueue<Pair<String,String>> queue;
    private ConcurrentHashMap<String, Boolean> map;
    private HashMap<String, HashSet<String>> Graph;
//    private ConcurrentHashMap<String, String> visited;
    private ConcurrentHashMap<String, Boolean> visited;
    private boolean queueReachMaxSize;


    SharedMemory() {
        queue=new ConcurrentLinkedQueue<>();
        map = new ConcurrentHashMap<>();
        Graph = new HashMap<>();
        visited = new ConcurrentHashMap<>();
        queueReachMaxSize = false;
    }

    public boolean isqueueReachMaxSize() {
        return queueReachMaxSize;
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

    public void queue_offer(String child, String parent) {
        queue.offer(new Pair<>(child,parent));
//        if(queue.size() == maxsize)
//            queueReachMaxSize=true;
    }

    public boolean queue_contains(String element) {
        return queue.contains(element);
    }

    public Pair<String, String> queue_poll() {
        if(queue.isEmpty())return null;
        return queue.poll();
    }

    public long queue_size() {return queue.size();}


    public boolean visited_contains(String element) {
        return visited.containsKey(element);
    }

//    public String visited_get(String e){return visited.get(e);}

//    public void visited_add(String e,String hash){visited.put(e,hash);}

    public void visited_add(String e) {
        visited.put(e,true);
    }

    public long visited_size() {
        return visited.size();
    }

    public synchronized void Graph_add(String child , String parent) {
        if(Graph.containsKey(child)) {
            Graph.get(child).add(parent);
        }
        else{
            HashSet<String> temp = new HashSet<>();
            temp.add(parent);
            Graph.put(child,temp);
        }
    }

    public int Graph_size(){
        return Graph.size();
    }

    public HashMap<String, HashSet<String>> get_Graph() {return Graph;}

//    public boolean updated_urls_contains(String element) {
//        return updated_urls.containsKey(element);
//    }
//
//    public boolean updated_urls_remove(String element) {
//        return updated_urls.remove(element) != null;
//    }

    public void saveState(String filePath){
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.close();
            System.out.println("File cleared successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            while (!queue.isEmpty()) {
                Pair<String,String> element = queue.poll();
                fileWriter.write( element.getfirst()+ " "+ element.getsecond() + "\n");
            }
            fileWriter.close();
            System.out.println("Queue contents saved to file successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
