package bag_of_tasks;

import java.rmi.RemoteException;
import java.util.TimerTask;

public class SignalSender extends TimerTask {
    NodeBag nodeBag;
    public SignalSender(NodeBag nodeBag){
        this.nodeBag = nodeBag;
    }

    public void run() {
        try{
            nodeBag.stub.signal(nodeBag.getBagID());
        }catch (RemoteException e){}
    }
}