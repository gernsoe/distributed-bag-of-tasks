package bag_of_tasks;

import java.util.HashMap;

public class MasterBag extends BagOfTasks {

    protected int taskCount = 0;
    protected HashMap<Integer, Task> waitingForResults;

    public MasterBag(int numberOfWorkers){
       this.waitingForResults = new HashMap<Integer, Task>();
       localBag = new Bag(numberOfWorkers);
    }

    // Midlertidig løsning - tasks bliver tilføjet til "waitingForResult" her,
    // TODO: tilføj først tasks til "waitingForResults" når tasken sendes til en node bag
    public void submitTask(Task t) {
        localBag.addTask(t);
        waitingForResults.put(++taskCount, t);
    }
}