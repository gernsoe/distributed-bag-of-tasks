package bag_of_tasks;

public class BagOfTasks {
    private Bag bag;

    public BagOfTasks(int numberOfWorkers){
        bag = new Bag(numberOfWorkers);
    }

    public void submitTask(Task task){
        bag.addTask(task);
    }
}