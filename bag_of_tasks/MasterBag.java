package bag_of_tasks;

public class MasterBag extends BagOfTasks {

    public MasterBag(int numberOfWorkers){
        localBag = new Bag(numberOfWorkers);
    }


}