import bag_of_tasks.NodeBag;

import java.rmi.RemoteException;

public class NodeUser {
    public static void main(String []args) throws RemoteException {
        NodeBag node = new NodeBag(5);
        while(true){
            node.takeTaskFromMaster();
            System.out.println("spam");
        }
    }
}