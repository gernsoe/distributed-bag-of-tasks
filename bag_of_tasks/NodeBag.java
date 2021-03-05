package bag_of_tasks;

public class NodeBag extends BagOfTasks {
    Bag mastersBag;

    public NodeBag(int numberOfWorkers, BagOfTasks mastersBag){
       localBag = new Bag(numberOfWorkers);
       this.mastersBag = mastersBag.localBag;
    }

    public void takeTaskFromMaster(){
        Task t = mastersBag.getTask();
        localBag.addTask(t);
    }

}