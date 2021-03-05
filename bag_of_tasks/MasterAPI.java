package bag_of_tasks;
import java.rmi.*;

public interface MasterAPI extends Remote{

    public Task getRemoteTask() throws RemoteException;

}  