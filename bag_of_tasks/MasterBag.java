package bag_of_tasks;

import java.util.HashMap;

public class MasterBag<T> extends BagOfTasks {

    protected int taskCount = 0;
    protected HashMap<Integer, Task> waitingForResults;

    public MasterBag(int numberOfWorkers){
       this.waitingForResults = new HashMap<Integer, Task>();
       localBag = new Bag(numberOfWorkers);
    }

    // Midlertidig løsning - tasks bliver tilføjet til "waitingForResult" her,
    // TODO: tilføj først tasks til "waitingForResults" når tasken sendes til en node bag
    public void submitTask(Task t) {
        t.setID(taskCount);
        localBag.addTask(t);
        waitingForResults.put(t.getID(), t);
        taskCount++;
    }

    public void setRemoteResult(int id, T result) {
        waitingForResults.get(id).setResult(result);
    }
}