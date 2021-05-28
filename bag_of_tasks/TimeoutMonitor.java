package bag_of_tasks;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.UUID;

public class TimeoutMonitor extends TimerTask {
    MasterBag masterBag;
    HashMap<UUID, Boolean> timeouts;
    public TimeoutMonitor(MasterBag masterBag){
        this.masterBag = masterBag;
        this.timeouts = masterBag.getTimeouts();
    }

    public void run() {
        synchronized (masterBag) {
            Iterator it = timeouts.keySet().iterator();
            while(it.hasNext()){
                UUID nodeID = (UUID) it.next();
                if(timeouts.get(nodeID)){
                    timeouts.put(nodeID, false);
                }else{
                    masterBag.restoreLostTasks(nodeID);
                    timeouts.remove(nodeID);
                    System.out.println(nodeID + " Timed out");
                }
            }
        }
    }
}