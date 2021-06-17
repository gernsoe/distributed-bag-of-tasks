import bag_of_tasks.*;


public class MessageTask extends Task {
    public String msg;

    public MessageTask(String msg){
        this.msg = msg;
    }

    public String call(){
        return msg;
    }

}
