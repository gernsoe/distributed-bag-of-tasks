package bag_of_tasks;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

public class MasterBag extends BagOfTasks implements MasterAPI {
    protected ConcurrentHashMap<Integer, Task> remoteTasks = new ConcurrentHashMap<Integer, Task>();
    int counter = 0;

    private static MasterAPI api;

    public MasterBag(int numberOfWorkers) throws RemoteException {
       super();
       initWorkers(numberOfWorkers);
       api = this;
    }

    public void submitTask(Task t) {
        t.setID(counter);
        addTask(t);
        remoteTasks.put(counter,t);
        counter++;
    }

    public Task getRemoteTask(){
        return getTask();
    }

    public <T> void returnFinishedTask(T result, int ID){
        try {
            remoteTasks.get(ID).setResult(result);
        } catch (Exception e){}
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
        }catch(Exception e){}
    }
}
