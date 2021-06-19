package bag_of_tasks;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Timer;

public class NodeBag extends BagOfTasks {
    MasterAPI stub;
    private int numberOfWorkers;

    public NodeBag(int numberOfWorkers, String hostname) {
        super();
        taskBag.setThreshold(numberOfWorkers);
        this.numberOfWorkers = numberOfWorkers;
        int port = 1099;
        try {
            Registry registry = LocateRegistry.getRegistry(hostname,port);
            this.stub = (MasterAPI) registry.lookup("BoT");
            System.out.println("Stub received from: "+hostname);
            notifyMaster();
        } catch (Exception e ) {
            System.out.println("Failed to connect");
            e.printStackTrace();
        }
        initWorkers(numberOfWorkers);
        TaskRetriever taskRetriever = new TaskRetriever(this);
        taskRetriever.start();
        Timer timer = new Timer();
        timer.schedule(new SignalSender(this),0,3000);
        System.setProperty("sun.rmi.transport.tcp.responseTimeout", "10000");
    }

    public void initWorkers(int numberOfWorkers){
        for (int i = 0; i < numberOfWorkers; ++i) {
            Worker worker = new NodeWorker(this);
            worker.start();
            workers.add(worker);
        }
    }

    public void takeTaskFromMaster() throws RemoteException {
            Task task = stub.getRemoteTask(this.getBagID());
            System.out.println("Got task with ID: " + task.getID() + " from master");
            addTask(task);
    }

    public void notifyMaster() throws RemoteException {
        System.out.println("Node ID: " + bagID);
        stub.identify(bagID,numberOfWorkers);
    }
}

class TaskRetriever extends Thread {

    NodeBag nodeBag;

    protected TaskRetriever(NodeBag nodeBag) {
        this.nodeBag = nodeBag;
    }

    public void run() {
        while (true) {
            try {
                nodeBag.takeTaskFromMaster();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}

class NodeWorker extends Worker {
    NodeBag nodeBag;
    public NodeWorker(NodeBag nodeBag){
        this.nodeBag = nodeBag;
    }

    public void work() {

        Task task = nodeBag.getTask();
        nodeBag.taskBag.decrementThreshold();
        task.run();
        try {
            nodeBag.stub.returnFinishedTask(task.getResult(), task.getID(), nodeBag.getBagID());
            System.out.println("Returned task with ID: " + task.getID() + " and result: " + task.getResult() + " to master");
            nodeBag.taskBag.incrementThreshold();
        }catch(Exception e){e.printStackTrace();}
    }
}

