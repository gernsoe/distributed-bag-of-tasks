import bag_of_tasks.*;

import java.lang.reflect.Array;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.LinkedBlockingQueue;

class MasterUser {

    public static void main(String[] args) throws RemoteException, AlreadyBoundException, Exception {
        setHost(args);

        MasterBag masterBag = new MasterBag(5);
        MasterBag.register();
        Timer timer = new Timer();
        timer.schedule(new MasterMonitor(masterBag),0,2000);

        int runs = 5;
        int warmups = 3;
        int tasksToRun = 50;

        System.out.println("Warming up "+warmups+" times");
        for(int i = 0; i<warmups; i++){
            runStuff(masterBag,tasksToRun);

        }

        System.out.println("Warmed up, now running "+runs+" runs");

        long startTime = System.nanoTime();
        for(int i = 0; i<runs; i++){
            runStuff(masterBag,tasksToRun);
        }
        double time = ((System.nanoTime()-startTime) / 1e9)/runs;
        System.out.println("Average execution time across "+runs+" runs: "+time+"s");
        timer.cancel();

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

    public static void runStuff(MasterBag masterBag,int numOfTasks) throws Exception{
        ArrayList<Task> futures = new ArrayList<Task>();
        long startTime;
        startTime = System.nanoTime();
        for(int i = 0; i<numOfTasks; i++){
            Task t = new PrimeTask(i);
            masterBag.submitTask(t);
            Task t2 = masterBag.continueWith(t,a->(int)a/2);
            Task t3 = masterBag.continueWith(t,a->(int)a/2);
            Task t4 = masterBag.combineWith(t2,t3,(a,b)->(int)a-(int)b);
            futures.add(t);
            futures.add(t2);
            futures.add(t3);
            futures.add(t4);
        }
        /*
        for(Task t : futures){
            try {
                System.out.println("The result of task "+t+" is:"+t.getResult());
            } catch (Exception e){e.printStackTrace();;}
        }
         */

        System.out.println("\nFinal result: "+futures.get(futures.size()-1).getResult());
        System.out.println("Time to process: "+((double)System.nanoTime()-startTime)/1e9+"s");
        masterBag.resetTaskCount();

    }
}