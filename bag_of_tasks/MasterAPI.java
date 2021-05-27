package bag_of_tasks;
import java.rmi.*;
import java.util.UUID;

public interface MasterAPI extends Remote{

    public Task getRemoteTask(UUID nodeID) throws RemoteException;
    public <T> void returnFinishedTask(T result, UUID ID,UUID nodeID) throws RemoteException;
    public void identify(UUID nodeID, int numberOfNodeWorkers) throws RemoteException;

}  