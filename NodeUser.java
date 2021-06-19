import bag_of_tasks.NodeUI;

import java.rmi.RemoteException;

public class NodeUser {
    public static void main(String[] args) throws RemoteException {
        NodeUI node = new NodeUI(4, args);
    }
}