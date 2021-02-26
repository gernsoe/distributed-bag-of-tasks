package bag_of_tasks;

public class SingleBag extends BagOfTasks {

    public SingleBag(int numberOfWorkers){
        localBag = new Bag(numberOfWorkers);
    }

}