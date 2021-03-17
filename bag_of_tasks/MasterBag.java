package bag_of_tasks;

import java.rmi.RemoteException;
import java.util.concurrent.LinkedBlockingQueue;

public class MasterBag extends BagOfTasks {

    protected LinkedBlockingQueue<Task> finishedTasks = new LinkedBlockingQueue<Task>();

    public MasterBag(int numberOfWorkers) throws RemoteException {
       localBag = new Bag(numberOfWorkers);
    }

    public void submitTask(Task t) {
        localBag.addTask(t);
    }

    public Task getTaskForRemote(){
        return localBag.getTask();
    }

    public void addFinishedTask(Task task){
        finishedTasks.add(task);
    }
}