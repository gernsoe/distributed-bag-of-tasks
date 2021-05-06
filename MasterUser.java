import bag_of_tasks.*;

import java.lang.reflect.Array;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

class MasterUser {

    public static void main(String[] args) throws RemoteException, AlreadyBoundException, Exception {
        ArrayList<Task> futures = new ArrayList<Task>();
        int primesToFind;
        setHost(args);

        MasterBag masterBag = new MasterBag(1);
        MasterBag.register();

        Task t1 = new squareTask(2);

        Task t2 = masterBag.continueWith(t1,(result) -> 3+(int)result);
        masterBag.submitTask(t1);

        futures.add(t1);
        futures.add(t2);

        /*
        System.out.println("This program will calculate the n first prime numbers.");
        System.out.println("Input how many prime numbers do you want to compute:");

        while(true) {
            Scanner in = new Scanner(System.in);
            String input = in.next();

            if (isInt(input)){
                primesToFind = Integer.parseInt(input);
                System.out.println("Finding first " + primesToFind + " prime numbers...");

                for (int i = 1; i <= primesToFind; ++i) {
                    Task task = new PrimeTask(i);
                    masterBag.submitTask(task);
                    futures.add(task);
                    System.out.println("Added task " + i + " to the bag");
                }
                break;
            }
        }

        */

        for(Task t : futures){
            try {
                System.out.println("The result of task with ID "+t.getID()+" is:"+t.getResult());
            } catch (Exception e){e.printStackTrace();;}
        }

    }

    public static boolean isInt(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Please input a valid positive integer.");
            return false;
        }
        return true;
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




