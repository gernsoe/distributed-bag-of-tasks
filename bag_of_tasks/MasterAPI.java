package bag_of_tasks;
import java.rmi.*;

public interface MasterAPI extends Remote{

    public Task getRemoteTask() throws RemoteException;
    public <T> void returnFinishedTask(T result, int ID) throws RemoteException;

}  