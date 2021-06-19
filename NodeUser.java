import bag_of_tasks.NodeUI;

import java.rmi.RemoteException;

public class NodeUser {
    public static void main(String[] args) throws RemoteException {
        int numberOfWorkers = 4;
        NodeUI node = new NodeUI(numberOfWorkers, args);
    }
}