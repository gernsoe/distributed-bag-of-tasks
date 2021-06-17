import bag_of_tasks.*;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

class MasterUser {

    public static String logFileName;

    public static void main(String[] args) throws RemoteException, AlreadyBoundException, Exception {
        setHost(args);

        int numberOfWorkers = 1;
        UI masterBag = new UI(numberOfWorkers,60000,2000);
        MasterBag.register();

        logFileName = LogRunTime.createFile(); //Create a logfile for the results

        int runs = 1;
        int warmups = 1;
        int tasksToRun = 20; //amount of cycles in the loop that generates tasks, so right now it's more like taskstorun*4 tasks
        System.out.println("Warming up "+warmups+" times");
        for(int i = 0; i<warmups; i++){
            runStuff(masterBag,tasksToRun,false);
        }

        System.out.println("Warmed up, now running "+runs+" runs");
        LogRunTime.writeFile(logFileName, "Running " + tasksToRun + " tasks");

        long startTime = System.nanoTime();
        for(int i = 0; i<runs; i++){
            runStuff(masterBag,tasksToRun,true);
        }

        double time = ((System.nanoTime()-startTime) / 1e9)/runs;
        String averageOutput = "Average execution time across "+runs+" runs: "+time+"s";
        System.out.println(averageOutput);
        LogRunTime.writeFile(logFileName, averageOutput);
        System.out.println("Average execution time across "+runs+" runs: "+time+"s");
        masterBag.cancelTimer();
        LogRunTime.writeFile(logFileName, "Number of nodes: " + masterBag.getNumberOfNodes());
        LogRunTime.writeFile(logFileName, "Number of master workers: " + numberOfWorkers);
        LogRunTime.writeFile(logFileName, "Total number of workers across nodes: " + masterBag.getTotalWorkers());

    }

    public static void setHost(String[] ipv4){
        try {
            if (ipv4[0].equals("marc")) {
                System.setProperty("java.rmi.server.hostname", "80.162.217.75");
            } else if (ipv4[0].equals("christian")) {
                System.setProperty("java.rmi.server.hostname", "62.198.63.48");
            } else {
                System.setProperty("java.rmi.server.hostname", ipv4[0]);
            }
        }catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Please supply host public ipv4 address as argument");
            System.exit(0);
        }
        System.out.println("Host is: "+System.getProperty("java.rmi.server.hostname"));
    }

    public static void runStuff(UI masterBag,int numOfTasks, boolean writeToFile) throws Exception{
        ArrayList<Task> futures = new ArrayList<Task>();
        long startTime;
        startTime = System.nanoTime();
        for(int i = 0; i<numOfTasks; i++){
            Task t = new PrimeTask(i);
            masterBag.submitTask(t);
            Task t2 = masterBag.continueWith(t,a->{
                int c = 0;
                for(int k=0; k < Integer.MAX_VALUE/2; k++){
                    for(int j=0; j < 10; j++){
                        c++;
                        if(c > (Integer.MAX_VALUE/4)){
                            c = 0;
                        }
                    }
                }
                return (int)a;
            });
            Task t3 = masterBag.combineWith(t,t2,(a,b)->{
                int c = 0;
                for(int k=0; k < Integer.MAX_VALUE/2; k++){
                    for(int j=0; j < 10; j++){
                        c++;
                        if(c > (Integer.MAX_VALUE/4)){
                            c = 0;
                        }
                    }
                }
                return (int)a+(int)b;
            });
            futures.add(t);
            futures.add(t2);
            futures.add(t3);
        }

        ArrayList<Integer> results = new ArrayList<Integer>();
        for(Task t : futures) {
            int res = (int) t.getResult();
            System.out.println(res);
            results.add(res);
        }


        String outputString = "Time to process: "+masterBag.getTaskCount()+" tasks "+((double)System.nanoTime()-startTime)/1e9+"s";
        masterBag.resetTaskCount();
        System.out.println(outputString);
        if (writeToFile) {
            LogRunTime.writeFile(logFileName, outputString);
        }
        //masterBag.flush();
    }
}