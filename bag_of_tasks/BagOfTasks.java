package bag_of_tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class BagOfTasks {

    protected ExtendedQueue<Task> taskBag;
    protected List<Worker> workers;



    protected UUID bagID;

    protected BagOfTasks(){
        bagID = UUID.randomUUID();
        this.taskBag = new ExtendedQueue<Task>(){};
        this.workers = new ArrayList<Worker>(){};
    }

    protected void addTask(Task task) {
        try {
            taskBag.ePut(task);
        } catch (InterruptedException e) {e.printStackTrace();}
    }

    protected Task getTask() {
        Task task = null;
        try {
            task = taskBag.eTake();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return task;
    }

    abstract public void initWorkers(int numberOfWorkers);

    public UUID getBagID() {
        return bagID;
    }
}