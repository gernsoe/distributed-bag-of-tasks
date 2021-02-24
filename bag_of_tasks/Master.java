package bag_of_tasks;

public class Master {
    public static Bag bag;

    public Master(int numberOfWorkers){
        bag = new Bag(numberOfWorkers);
    }

    public void submitTask(Task task){
        bag.addTask(task);
    }
}