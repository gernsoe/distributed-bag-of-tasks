import bag_of_tasks.NodeBag;

import java.rmi.RemoteException;

public class NodeUser {
    public static void main(String []args) throws RemoteException {
        MasterUser.setHost(args);

        NodeBag node = new NodeBag(5, args[0]);
    }
}