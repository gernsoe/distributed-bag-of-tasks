import java.util.*;
import java.util.concurrent.*;

class Bag {

    private ArrayList<Worker> workers;
    private BlockingQueue<Task> taskBag;
    public static List<Future<Integer>> futures;


    public Bag(int numberOfWorkers){
        this.taskBag = new LinkedBlockingQueue<>(){};
        this.futures = new ArrayList<Future<Integer>>() {};
        this.workers = new ArrayList<>(){};
        for (int i = 0; i < numberOfWorkers; ++i) {
            workers.add(new Worker(this));
        }
    }

    public void addTask(Task task) {
        try {
            taskBag.put(task);
        } catch (InterruptedException e) {}

    }

    public Task getTask() {
        Task task = null;
        try {
            task = taskBag.take();
        } catch (InterruptedException e) {}

        return task;
    }

    public List<Future<Integer>> getFutures() { return futures; }

    public void addFuture(Future f) { futures.add(f); }

}

class Worker extends Thread {
    Bag bag;

    public Worker(Bag bag) {
        this.bag = bag;
    }

    public void run() {
        while (true) {
            Task task = bag.getTask();

            System.out.println("Started working on task with input: " + task.getID());

            task.run();
        }
    }
}

