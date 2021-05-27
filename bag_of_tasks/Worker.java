package bag_of_tasks;

import java.rmi.RemoteException;

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

    abstract public void work() throws Exception;

}
