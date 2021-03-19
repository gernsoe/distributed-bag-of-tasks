/*package bag_of_tasks;

import java.util.*;
import java.util.concurrent.*;

class Bag {

    private BlockingQueue<Task> taskBag;
    private List<OldWorker> workers;

    protected Bag(int numberOfWorkers){
        this.taskBag = new LinkedBlockingQueue<Task>(){};
        this.workers = new ArrayList<OldWorker>(){};
        for (int i = 0; i < numberOfWorkers; ++i) {
            OldWorker worker = new OldWorker(this);
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

class OldWorker extends Thread {
    Bag bag;
    Boolean master;

    protected OldWorker(Bag bag, boolean master) {
        this.bag = bag;
        this.master = master;
    }

    public void run() {
        while (true) {
            work();
        }
    }

    public void work() {
        Task task = bag.getTask();

        System.out.println("Started working on a task");

        task.run();

    }
}

 */

