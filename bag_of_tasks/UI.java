package bag_of_tasks;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

public class UI {
    static MasterBag masterBag;

    public UI(int numberOfWorkers,int timeout_ms, int status_ms) throws RemoteException {
        masterBag = new MasterBag(numberOfWorkers, timeout_ms, status_ms);
    }

    public synchronized void submitTask(Task t) {
        masterBag.submitTask(t);
    }

    public Task continueWith(Task predecessor, ContinueInput inputFunction) throws Exception {
        return masterBag.continueWith(predecessor, inputFunction);
    }

    public Task combineWith(Task predecessor1, Task predecessor2, CombineInput inputFunction) throws Exception {
        return masterBag.combineWith(predecessor1, predecessor2, inputFunction);
    }

    public static void register() throws AlreadyBoundException, RemoteException {
        masterBag.register();
    }

    public int getTaskCount() {
        return masterBag.getTaskCount();
    }

    public void resetTaskCount() {
        masterBag.resetTaskCount();
    }

    public int getNumberOfNodes() {
        return masterBag.getNumberOfNodes();
    }

    public int getTotalWorkers() {
        return masterBag.getTotalWorkers();
    }

    public void cancelTimer() {
        masterBag.statusTimer.cancel();
    }
}
