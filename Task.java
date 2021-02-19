import java.util.concurrent.Callable;

abstract class Task implements Callable<Integer>, Runnable {
    int ID;
    Boolean isDone = false;
    Integer result;

    public Task(int ID) {
        this.ID = ID;
    }

    public synchronized Integer getResult() {
        while(!isDone){
            try{
                wait();
            }catch(InterruptedException e){}
        }

        return result;
    }

    public synchronized void setResult(Integer result) {
        this.result = result;
        isDone = true;
        notifyAll();
    }

    public int getID() {
        return ID;
    }

}