package bag_of_tasks;
import java.rmi.*;
import java.util.UUID;

public interface MasterAPI extends Remote{

    public Task getRemoteTask() throws RemoteException;
    public <T> void returnFinishedTask(T result, UUID ID) throws RemoteException;
    public void identify(String ipv4, int numberOfNodeWorkers) throws RemoteException;

}  