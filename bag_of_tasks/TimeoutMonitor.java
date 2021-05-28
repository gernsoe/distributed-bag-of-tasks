package bag_of_tasks;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.UUID;

public class TimeoutMonitor extends TimerTask {
    MasterBag masterBag;
    HashMap<UUID, Boolean> timeouts;
    public TimeoutMonitor(MasterBag masterBag){
        this.masterBag = masterBag;
        this.timeouts = masterBag.getTimeouts();
    }

    public void run(){
        timeouts.forEach((nodeID,b) -> {
            if(b){
                timeouts.put(nodeID,false);
            } else {
                masterBag.restoreLostTasks(nodeID);
            }
        });
    }
}