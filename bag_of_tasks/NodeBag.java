package bag_of_tasks;
import jdk.nashorn.internal.runtime.ECMAException;

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
}

class NodeWorker extends Worker {
    NodeBag nodeBag;
    public NodeWorker(NodeBag nodeBag){
        this.nodeBag = nodeBag;
    }

    public void work() throws RemoteException {
        Task task = nodeBag.stub.getRemoteTask();
        task.run();
        try {
            nodeBag.stub.returnFinishedTask(task.getResult(), task.getID());
        }catch(Exception e){}
        System.out.println("Finished task");
    }
}

