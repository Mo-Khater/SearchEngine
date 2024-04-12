import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.FileWriter;
import java.io.IOException;





public class SharedMemory {
    private ConcurrentLinkedQueue<String> queue;
    private ConcurrentHashMap<String, Boolean> map;
    private ConcurrentHashMap<String, Boolean> visited;


    SharedMemory()
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
                String element = queue.poll();
                fileWriter.write(element + "\n");
            }
            fileWriter.close();
            System.out.println("Queue contents saved to file successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
