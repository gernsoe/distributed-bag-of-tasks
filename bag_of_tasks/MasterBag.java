package bag_of_tasks;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.LinkedBlockingQueue;

public class MasterBag extends BagOfTasks implements MasterAPI {

    protected LinkedBlockingQueue<Task> finishedTasks = new LinkedBlockingQueue<Task>();
    private static MasterAPI api;

    public MasterBag(int numberOfWorkers) throws RemoteException {
       localBag = new Bag(numberOfWorkers);
       api = this;
    }

    public void submitTask(Task t) {
        localBag.addTask(t);
    }

    public Task getRemoteTask(){
        return localBag.getTask();
    }

    public void returnFinishedTask(Task task){
        finishedTasks.add(task);
    }

    public static void register() throws RemoteException, InterruptedException , AlreadyBoundException {
        MasterAPI stub = (MasterAPI) UnicastRemoteObject.exportObject(api,0);
        Registry registry = LocateRegistry.createRegistry(5000);
        registry.bind("MasterBag",stub);

        System.out.print("Master Bag bound");
    }

}