package bag_of_tasks;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class MasterBag extends BagOfTasks implements MasterAPI {
    protected HashMap<UUID, Task> remoteTasks; //Catalogues all the tasks by their IDs so the results from remote nodes can be properly assigned
    protected HashMap<UUID, Set<UUID>> nodeTasks; //Catalogues all the tasks by their IDs so the results from remote nodes can be properly assigned
    protected DependencyGraph continuations;
    private static MasterAPI api;
    private int numberOfWorkers;
    protected int taskCount=0;
    private int numberOfNodes = 0;
    private int totalNumberOfWorkers = 0;

    public MasterBag(int numberOfWorkers) throws RemoteException {
       super();
       this.numberOfWorkers = numberOfWorkers;
       this.remoteTasks = new HashMap<UUID, Task>();
       this.nodeTasks = new HashMap<UUID,Set<UUID>>();
       this.continuations = new DependencyGraph(this);
       System.out.println("MasterBag initialized with: "+numberOfWorkers+" workers");
       initWorkers(numberOfWorkers);
       api = this;
    }

    public void identify(UUID nodeID, int numberOfNodeWorkers){
        System.out.println("Node connected with ID: "+nodeID);
        nodeTasks.put(nodeID,new HashSet<UUID>());
        numberOfNodes++;
        totalNumberOfWorkers += numberOfNodeWorkers;
    }

    public synchronized void submitTask(Task t) {
        addTask(t);
        remoteTasks.put(t.getID(),t);
    }

    public synchronized Task continueWith(Task predecessor, ContinueInput inputFunction) throws Exception{
        SystemTask sysTask = new ContinueTask(inputFunction,predecessor.getID());
        submitIfReady(sysTask, predecessor);
        return sysTask;
    }

    public synchronized Task combineWith(Task predecessor1, Task predecessor2, CombineInput inputFunction) throws Exception{
        SystemTask sysTask = new CombineTask(inputFunction,predecessor1.getID(),predecessor2.getID());
        submitIfReady(sysTask, predecessor1, predecessor2);
        return sysTask;
    }

    protected synchronized void submitIfReady(SystemTask sysTask, Task predecessor)throws Exception{
        if(predecessor.getIsDone()){
            sysTask.setParameter(predecessor.getID(),predecessor.getResult());
            submitTask(sysTask);
        }else{
            //System.out.println("added to continuations");
            continuations.addContinuation(predecessor,sysTask);
        }
    }

    public synchronized void submitIfReady(SystemTask sysTask, Task predecessor1, Task predecessor2)throws Exception{
        if(predecessor1.getIsDone() && predecessor2.getIsDone()){
            sysTask.setParameter(predecessor1.getID(),predecessor1.getResult());
            sysTask.setParameter(predecessor2.getID(),predecessor2.getResult());
            submitTask(sysTask);
        }else if(predecessor1.getIsDone()){
            sysTask.setParameter(predecessor1.getID(),predecessor1.getResult());
            continuations.addContinuation(predecessor2,sysTask);
        }else if(predecessor2.getIsDone()){
            sysTask.setParameter(predecessor2.getID(),predecessor2.getResult());
            continuations.addContinuation(predecessor1,sysTask);
        }else{
            continuations.addContinuation(predecessor1,sysTask);
            continuations.addContinuation(predecessor2,sysTask);
        }
    }

    public Task getRemoteTask(UUID nodeID){
        Task t = getTask();
        synchronized (this){
            nodeTasks.get(nodeID).add(t.getID());
        }
        return t;
    }

    public synchronized <T> void returnFinishedTask(T result, UUID ID, UUID nodeID){
        try {
            Task t = remoteTasks.remove(ID);
            t.setResult(result);
            //System.out.println("Releasing continuations..");
            continuations.releaseContinuations(t);
            taskCount++;
            if(nodeID != this.bagID){ //Since tasks are only added to nodeTasks through getRemoteTask call, the MasterBag ID is not present in nodeTasks
                nodeTasks.get(nodeID).remove(ID);
            }
        } catch (Exception e){
            System.out.println("Failed with ID: "+ID);
            e.printStackTrace();}
    }

    public static void register() throws RemoteException, AlreadyBoundException {
        MasterAPI stub = (MasterAPI) UnicastRemoteObject.exportObject(api,1099);
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.bind("BoT",stub);
        System.out.println("Master Bag registered");
    }

    public void initWorkers(int numberOfWorkers){
        for (int i = 0; i < numberOfWorkers; ++i) {
            Worker worker = new MasterWorker(this);
            worker.start();
            workers.add(worker);
        }
    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public int getTotalWorkers() {
        return totalNumberOfWorkers;
    }

    public int getTaskCount(){
        return taskCount;
    }

    public synchronized void resetTaskCount(){
        taskCount = 0;
    }
}

class MasterWorker extends Worker {
    MasterBag masterBag;

    public MasterWorker(MasterBag masterBag){
        this.masterBag = masterBag;
    }
    public void work() {
            Task task = masterBag.getTask();
            task.run();
            try {
                masterBag.returnFinishedTask(task.getResult(), task.getID(), masterBag.getBagID());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

