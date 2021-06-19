package bag_of_tasks;

import java.rmi.RemoteException;

abstract class Worker extends Thread {

    public void run() {
        while (true) {
            try {
                work();
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }

    abstract public void work() throws RemoteException;

}
