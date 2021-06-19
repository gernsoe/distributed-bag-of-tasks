package bag_of_tasks;

import java.net.NoRouteToHostException;
import java.rmi.ConnectIOException;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutionException;

abstract class Worker extends Thread {

    public void run() {
        while (true) {
            try {
                work();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    abstract public void work() throws RemoteException;

}
