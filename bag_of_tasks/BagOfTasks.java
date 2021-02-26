package bag_of_tasks;

public abstract class BagOfTasks {

    Bag localBag;

    public void submitTask(Task task){
        localBag.addTask(task);
    }
}