package bag_of_tasks;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server implements MasterAPI {

    MasterBag masterBag;

    public Server(int numberOfWorkers) throws RemoteException{
        masterBag = new MasterBag(10);
    }

    public Task getRemoteTask() throws RemoteException{
        return masterBag.getTaskForRemote();
    }

    public void returnFinishedTask(Task task) throws RemoteException{
        masterBag.addFinishedTask(task);
    }

    public void startServer() throws RemoteException, InterruptedException, AlreadyBoundException{
        MasterAPI server = new Server(10);
        MasterAPI stub = (MasterAPI) UnicastRemoteObject.exportObject(server,0);
        Registry registry = LocateRegistry.createRegistry(5000);
        registry.bind("BoT",stub);
    }

}
