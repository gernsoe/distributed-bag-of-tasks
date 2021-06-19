package bag_of_tasks;

import java.util.TimerTask;

public class MasterMonitor extends TimerTask {
    MasterBag masterBag;
    public MasterMonitor(MasterBag masterBag){
        this.masterBag = masterBag;
    }

    public void run(){
        System.out.println("TaskCount: "+masterBag.getTaskCount());
        System.out.println("Queue Size: "+masterBag.taskBag.size());
        System.out.println("Connected nodes: "+masterBag.nodeTasks.size());
    }
}