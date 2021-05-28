package bag_of_tasks;

import com.sun.org.apache.xpath.internal.operations.Bool;

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
                System.out.println(nodeID+" Timed Out");
            }
        });
    }
}