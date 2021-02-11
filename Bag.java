import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

class Bag {
    private Queue<Task> taskBag = new LinkedList<>(){};
    public static List<Future<Integer>> futures = new ArrayList<Future<Integer>>() {};

    public void Bag(){

    }

    public void addTask(Task task) {
        taskBag.add(task);
    }

    public Task getTask() { return taskBag.poll(); }

    public List<Future<Integer>> getFutures() { return futures; }

    public void addFuture(Future f) { futures.add(f); }

}

