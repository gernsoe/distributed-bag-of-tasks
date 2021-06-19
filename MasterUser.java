import bag_of_tasks.*;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.AbstractMap;
import java.util.ArrayList;

class MasterUser {

    public static String logFileName;

    public static void main(String[] args) throws RemoteException, AlreadyBoundException, Exception {
        int numberOfWorkers = 4;
        MasterUI masterBag = new MasterUI(numberOfWorkers, args);
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
            runStuffFunctional(masterBag,tasksToRun,true);
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

    public static void runStuff(MasterUI masterBag, int numOfTasks, boolean writeToFile) throws Exception{
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

        //ArrayList<Integer> results = new ArrayList<Integer>();
        for(Task t : futures) {
            //int res = (int) t.getResult();
            System.out.println(t.getResult());
            //results.add(res);
        }


        String outputString = "Time to process: "+masterBag.getTaskCount()+" tasks "+((double)System.nanoTime()-startTime)/1e9+"s";
        masterBag.resetTaskCount();
        System.out.println(outputString);
        if (writeToFile) {
            LogRunTime.writeFile(logFileName, outputString);
        }
        //masterBag.flush();
    }

    public static void runStuffFunctional(MasterUI masterBag, int numOfTasks, boolean writeToFile) throws Exception{

        long startTime;
        ArrayList<Task> results = new ArrayList<Task>();
        startTime = System.nanoTime();
        for(int i = 1; i<numOfTasks; i++){
            Task t = new squareTask(i);
            Task tMsg = new MessageTask("The area of a circle with radius "+i+" (r*r,area) : ");
            Task t2 = masterBag.continueWith(t,a->{
                try {
                    Thread.sleep(2000);
                }catch (InterruptedException e){}
                return (int)a*Math.PI;
            });
            Task t3 = masterBag.combineWith(t,t2,(a,b)->{
                AbstractMap.SimpleEntry<Integer,Double> pair = new AbstractMap.SimpleEntry(a,b);
                return pair;
            });
            Task t4 = masterBag.combineWith(t3,tMsg,(r,msg)->{
                return (String) msg + r;
            });
            masterBag.submitTask(t);
            masterBag.submitTask(tMsg);

            results.add(t4);
        }

        for(Task t : results) {
            System.out.println(t.getResult());
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