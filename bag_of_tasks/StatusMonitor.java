package bag_of_tasks;

import java.util.TimerTask;

public class StatusMonitor extends TimerTask {
    MasterBag masterBag;
    public StatusMonitor(MasterBag masterBag){
        this.masterBag = masterBag;
    }

    public void run(){
        System.out.println("TaskCount: "+masterBag.getTaskCount());
        System.out.println("Queue Size: "+masterBag.taskBag.size());
        System.out.println("RemoteTasks Size: "+masterBag.runnableTasks.size());
    }
}