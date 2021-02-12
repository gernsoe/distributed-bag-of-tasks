import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

class Bag {
    public Queue<Task> taskBag;
    public List<Future<Integer>> futures;

    public Bag(){
        this.taskBag = new LinkedList<Task>(){};
        this.futures = new ArrayList<Future<Integer>>(){};
    }

    public void addTask(Task task) {
        taskBag.add(task);
    }

    public Task getTask() { return taskBag.poll(); }

    public List<Future<Integer>> getFutures() { return futures; }

    public void addFuture(Future f) { futures.add(f); }

}

