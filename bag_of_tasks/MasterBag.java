package bag_of_tasks;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class MasterBag extends BagOfTasks implements MasterAPI{
    protected HashMap<UUID, Task> runnableTasks; //Catalogues all unfinished independent tasks by their IDs so the results from remote nodes can be properly assigned
    protected HashMap<UUID, Set<UUID>> nodeTasks; //Keeps track of which nodes currently have which tasks.
    protected HashMap<UUID, Boolean> timeouts;
    protected DependencyGraph continuations;
    private static MasterAPI api;
    private int numberOfWorkers;
    protected int taskCount=0;
    private int numberOfNodes = 0;
    private int totalNumberOfWorkers = 0;
    private Timer timeoutTimer;
    public Timer statusTimer;
    int timeout_ms = 30000;
    int status_ms = 2000;


    protected MasterBag(int numberOfWorkers, String[] args) throws RemoteException {
       super();
       setHost(args);
       this.numberOfWorkers = numberOfWorkers;
       this.runnableTasks = new HashMap<UUID, Task>();
       this.nodeTasks = new HashMap<UUID,Set<UUID>>();
       this.timeouts = new HashMap<UUID, Boolean>();
       this.continuations = new DependencyGraph(this);
       System.out.println("MasterBag initialized with: "+numberOfWorkers+" workers");
       initWorkers(numberOfWorkers);
       api = this;
       this.timeoutTimer = new Timer();
       timeoutTimer.schedule(new TimeoutMonitor(this),0,timeout_ms);
       this.statusTimer = new Timer();
       statusTimer.schedule(new MasterMonitor(this),0,status_ms);
    }

    public synchronized void signal(UUID nodeID){
        timeouts.put(nodeID,true);
    }

    public synchronized void identify(UUID nodeID, int numberOfNodeWorkers){
        System.out.println("Node connected with ID: "+nodeID);
        nodeTasks.put(nodeID,new HashSet<UUID>());
        timeouts.put(nodeID,true);
        numberOfNodes++;
        totalNumberOfWorkers += numberOfNodeWorkers;
    }

    public synchronized void submitTask(Task t) {
        addTask(t);
        runnableTasks.put(t.getID(),t);
    }

    public Task continueWith(Task predecessor, ContinueInput inputFunction) throws Exception{
        SystemTask sysTask = new ContinueTask(inputFunction,predecessor.getID());
        submitIfReady(sysTask, predecessor);
        return sysTask;
    }

    public Task combineWith(Task predecessor1, Task predecessor2, CombineInput inputFunction) throws Exception{
        SystemTask sysTask = new CombineTask(inputFunction,predecessor1.getID(),predecessor2.getID());
        submitIfReady(sysTask, predecessor1, predecessor2);
        return sysTask;
    }

    protected void submitIfReady(SystemTask sysTask, Task predecessor)throws Exception{
        synchronized (continuations) {
            if (predecessor.getIsDone()) {
                sysTask.setParameter(predecessor.getID(), predecessor.getResult());
                submitTask(sysTask);
            } else {
                //System.out.println("added to continuations");
                continuations.addContinuation(predecessor, sysTask);
            }
        }
    }

    public void submitIfReady(SystemTask sysTask, Task predecessor1, Task predecessor2)throws Exception{
        synchronized (continuations) {
            if (predecessor1.getIsDone() && predecessor2.getIsDone()) {
                sysTask.setParameter(predecessor1.getID(), predecessor1.getResult());
                sysTask.setParameter(predecessor2.getID(), predecessor2.getResult());
                submitTask(sysTask);
            } else if (predecessor1.getIsDone()) {
                sysTask.setParameter(predecessor1.getID(), predecessor1.getResult());
                continuations.addContinuation(predecessor2, sysTask);
            } else if (predecessor2.getIsDone()) {
                sysTask.setParameter(predecessor2.getID(), predecessor2.getResult());
                continuations.addContinuation(predecessor1, sysTask);
            } else {
                continuations.addContinuation(predecessor1, sysTask);
                continuations.addContinuation(predecessor2, sysTask);
            }
        }
    }

    public synchronized void restoreLostTasks(UUID nodeID){
        for(UUID taskID : nodeTasks.get(nodeID)){
            addTask(runnableTasks.get(taskID));
        }
        nodeTasks.remove(nodeID);
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
            if(!runnableTasks.containsKey(ID)) {
                System.out.println("Duplicate result received, discarding duplicate");
                return;
            }
            Task t = runnableTasks.remove(ID);
            t.setResult(result);
            synchronized (continuations) {
                continuations.releaseContinuations(t);
            }
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

    public HashMap<UUID, Boolean> getTimeouts() {
        return timeouts;
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

