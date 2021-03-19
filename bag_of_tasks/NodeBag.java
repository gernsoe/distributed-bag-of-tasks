package bag_of_tasks;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class NodeBag extends BagOfTasks {
    MasterAPI stub;

    public NodeBag(int numberOfWorkers, String hostname) {
        localBag = new Bag(numberOfWorkers);

        int port = 1099;
        try {
            Registry registry = LocateRegistry.getRegistry(hostname,port);
            this.stub = (MasterAPI) registry.lookup("BoT");
            System.out.println("Stub received from: "+hostname);
        } catch (Exception e ) {
            System.out.println("Nodebag failed with: " + e);
        }
    }

    public void takeTaskFromMaster() throws RemoteException {
        Task t = stub.getRemoteTask();
        localBag.addTask(t);
    }
}

