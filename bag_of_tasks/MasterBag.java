package bag_of_tasks;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MasterBag extends BagOfTasks implements MasterAPI {
    protected ConcurrentHashMap<UUID, Task> remoteTasks = new ConcurrentHashMap<UUID, Task>(); //Catalogues all the tasks by their IDs so the results from remote nodes can be properly assigned


    protected DependencyGraph continuations;
    private static MasterAPI api;

    public MasterBag(int numberOfWorkers) throws RemoteException {
       super();
       this.continuations = new DependencyGraph(this);
       initWorkers(numberOfWorkers);
       api = this;
    }

    public void submitTask(Task t) {
        addTask(t);
        remoteTasks.put(t.getID(),t);
    }

    public synchronized Task continueWith(Task predecessor, ContinueInput inputFunction) throws Exception{
        /*
        SystemTask sysTask = new SystemTask() {
            @Override
            public Object call() throws Exception {
                return inputFunction.exec(parameter1);
            }
        };

         */

        SystemTask sysTask = new ContinueTask(inputFunction);
        sysTask.setPredecessor_1_ID(predecessor.getID());

        submitIfReady(sysTask, predecessor);

        return sysTask;
    }

    protected synchronized void submitIfReady(SystemTask sysTask, Task predecessor)throws Exception{
        if(predecessor.getIsDone()){
            sysTask.setParameter(predecessor.getID(),predecessor.getResult());
            addTask(sysTask);
        }else{
            System.out.println("added to continuations");
            continuations.addContinuation(predecessor,sysTask);
        }
    }


    public Task getRemoteTask(){
        Task t =getTask();
        System.out.println(t);
        return t;
    }

    public <T> void returnFinishedTask(T result, UUID ID){
        try {
            remoteTasks.get(ID).setResult(result);
            synchronized (this) {
                System.out.println("Releasing continuations..");
                continuations.releaseContinuations(remoteTasks.get(ID));
            }
        } catch (Exception e){e.printStackTrace();}
    }

    public static void register() throws RemoteException, AlreadyBoundException {
        MasterAPI stub = (MasterAPI) UnicastRemoteObject.exportObject(api,1099);
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.bind("BoT",stub);
        System.out.print("Master Bag registered");
    }

    public void initWorkers(int numberOfWorkers){
        for (int i = 0; i < numberOfWorkers; ++i) {
            Worker worker = new MasterWorker(this);
            worker.start();
            workers.add(worker);
        }
    }

}

class MasterWorker extends Worker {
    MasterBag masterBag;
    public MasterWorker(MasterBag masterBag){
        this.masterBag = masterBag;
    }

    public void work() throws RemoteException {
        Task task = masterBag.getTask();
        System.out.println("MasterWorker started on task");
        task.run();
        try {
            masterBag.returnFinishedTask(task.getResult(), task.getID());
        }catch(Exception e){e.printStackTrace();}
    }
}
