package bag_of_tasks;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class NodeBag extends BagOfTasks {
    MasterAPI stub;

    public NodeBag(int numberOfWorkers, String hostname) {
        super();
        taskBag.setThreshold(1);
        int port = 1099;
        try {
            Registry registry = LocateRegistry.getRegistry(hostname,port);
            this.stub = (MasterAPI) registry.lookup("BoT");
            System.out.println("Stub received from: "+hostname);
        } catch (Exception e ) {
            e.printStackTrace();
        }
        initWorkers(numberOfWorkers);
        TaskRetriever taskRetriever = new TaskRetriever(this);
        taskRetriever.start();
    }

    public void initWorkers(int numberOfWorkers){
        for (int i = 0; i < numberOfWorkers; ++i) {
            Worker worker = new NodeWorker(this);
            worker.start();
            workers.add(worker);
        }
    }

    public void takeTaskFromMaster() throws RemoteException {

            Task task = stub.getRemoteTask();

            addTask(task);
    }

    public void notifyMaster() throws RemoteException {
        stub.ack(System.getProperty("java.rmi.server.hostname"));
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

        task.run();
        try {
            nodeBag.stub.returnFinishedTask(task.getResult(), task.getID());
        }catch(Exception e){e.printStackTrace();}
        //System.out.println("Finished task");
    }
}

