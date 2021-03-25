package bag_of_tasks;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class NodeBag extends BagOfTasks {
    MasterAPI stub;

    public NodeBag(int numberOfWorkers, String hostname) {
        super();
        int port = 1099;
        try {
            Registry registry = LocateRegistry.getRegistry(hostname,port);
            this.stub = (MasterAPI) registry.lookup("BoT");
            System.out.println("Stub received from: "+hostname);
        } catch (Exception e ) {
            System.out.println("Nodebag failed with: " + e);
        }
        initWorkers(numberOfWorkers);
    }

    public void initWorkers(int numberOfWorkers){
        for (int i = 0; i < numberOfWorkers; ++i) {
            Worker worker = new NodeWorker(this);
            worker.start();
            workers.add(worker);
        }
    }

    public void takeTaskFromMaster(int tasksToRetrieve) throws RemoteException {
        for (int i = 0; i < tasksToRetrieve; i++) {
            Task task = stub.getRemoteTask();
            addTask(task);
        }
    }
}

class TaskRetriever extends Thread {

    NodeBag nodeBag;
    int taskThreshold = 2;
    int tasksToRetrieve = 4;

    protected TaskRetriever(NodeBag nodeBag) {
        this.nodeBag = nodeBag;
    }

    public void run() {
        try {
            if (nodeBag.taskBag.size() <= taskThreshold) {
                nodeBag.takeTaskFromMaster(tasksToRetrieve);
            }
            Thread.sleep(5000);
        } catch (InterruptedException | RemoteException e) {}

    }
}

class NodeWorker extends Worker {
    NodeBag nodeBag;
    public NodeWorker(NodeBag nodeBag){
        this.nodeBag = nodeBag;
    }

    public void work() throws RemoteException {
        Task task = nodeBag.getTask();
        task.run();
        nodeBag.stub.returnFinishedTask(task);
        System.out.println("Finished task");
    }
}

