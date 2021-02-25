package bag_of_tasks;

import java.util.*;
import java.util.concurrent.*;

class Bag {

    private BlockingQueue<Task> taskBag;
    private List<Worker> workers;

    protected Bag(int numberOfWorkers){
        this.taskBag = new LinkedBlockingQueue<Task>(){};
        this.workers = new ArrayList<Worker>(){};
        for (int i = 0; i < numberOfWorkers; ++i) {
            Worker worker = new Worker(this);
            worker.start();
            workers.add(worker);
        }
    }

    protected void addTask(Task task) {
        try {
            taskBag.put(task);
        } catch (InterruptedException e) {}

    }

    protected Task getTask() {
        Task task = null;
        try {
            task = taskBag.take();
        } catch (InterruptedException e) {}

        return task;
    }

}

class Worker extends Thread {
    Bag bag;

    protected Worker(Bag bag) {
        this.bag = bag;
    }

    public void run() {
        while (true) {
            Task task = bag.getTask();

            System.out.println("Started working on a task");

            task.run();
        }
    }
}

