package bag_of_tasks;

import java.rmi.RemoteException;

public class NodeUI {
    static NodeBag nodeBag;

    public NodeUI(int numberOfWorkers, String[] args) throws RemoteException {
        nodeBag = new NodeBag(numberOfWorkers, args);
    }
}
