package bag_of_tasks;
import java.util.*;

public class TimeoutMonitor extends TimerTask {
    MasterBag masterBag;
    HashMap<UUID, Boolean> timeouts;
    public TimeoutMonitor(MasterBag masterBag){
        this.masterBag = masterBag;
        this.timeouts = masterBag.getTimeouts();
    }

    public void run() {
        synchronized (masterBag) {
            List<UUID> toRefresh = new ArrayList<UUID>();
            List<UUID> toTimeout = new ArrayList<UUID>();
            Iterator it = timeouts.keySet().iterator();
            while(it.hasNext()){
                UUID nodeID = (UUID) it.next();
                if(timeouts.get(nodeID)){
                    toRefresh.add(nodeID);
                }else{
                    toTimeout.add(nodeID);
                }
            }

            for(UUID nodeID: toRefresh){
                timeouts.put(nodeID,false);
            }
            for(UUID nodeID: toTimeout){
                masterBag.restoreLostTasks(nodeID);
                timeouts.remove(nodeID);
                System.out.println(nodeID + " Timed out");
            }
        }
    }
}