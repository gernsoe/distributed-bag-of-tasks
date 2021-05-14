import bag_of_tasks.NodeBag;

import java.rmi.RemoteException;

public class NodeUser {
    public static void main(String []args) throws RemoteException {
        setHost(args);

        NodeBag node = new NodeBag(10, System.getProperty("java.rmi.server.hostname"));
    }

    public static void setHost(String[] ipv4){
        try {
            if (ipv4[0].equals("marc")) {
                System.setProperty("java.rmi.server.hostname", "80.162.217.75");
            } else {
                System.setProperty("java.rmi.server.hostname", ipv4[0]);
            }
        }catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Please supply host public ipv4 address as argument");
            System.exit(0);
        }

        System.out.println("Host is: "+System.getProperty("java.rmi.server.hostname"));
    }
}