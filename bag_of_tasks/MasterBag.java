package bag_of_tasks;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MasterBag extends BagOfTasks implements MasterAPI {
    protected HashMap<UUID, Task> remoteTasks; //Catalogues all the tasks by their IDs so the results from remote nodes can be properly assigned
    protected DependencyGraph continuations;
    private static MasterAPI api;
    private int numberOfWorkers;
    protected int taskCount=0;

    public MasterBag(int numberOfWorkers) throws RemoteException {
       super();
       this.numberOfWorkers = numberOfWorkers;
       this.remoteTasks = new HashMap<UUID, Task>();
       this.continuations = new DependencyGraph(this);
       System.out.println("MasterBag initialized with: "+numberOfWorkers+" workers");
       initWorkers(numberOfWorkers);
       api = this;
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


    public Task getRemoteTask(){
        return getTask();
    }

    public <T> void returnFinishedTask(T result, UUID ID){
        try {
            synchronized (this) {
                remoteTasks.get(ID).setResult(result);
                //System.out.println("Releasing continuations..");
                continuations.releaseContinuations(remoteTasks.get(ID));
                taskCount++;
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

    public void flush(){
        this.taskBag = new ExtendedQueue<Task>(){};
        this.remoteTasks = new HashMap<UUID, Task>();
        this.continuations = new DependencyGraph(this);
        this.workers = new ArrayList<Worker>(){};
        initWorkers(numberOfWorkers);
        System.out.println("MasterBag flushed");
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
                masterBag.returnFinishedTask(task.getResult(), task.getID());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

