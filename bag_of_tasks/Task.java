package bag_of_tasks;

import java.util.concurrent.Callable;

public abstract class Task<T> implements Callable<T>, Runnable {
    Boolean isDone = false;
    T result;

    public void run(){
        T r;
        try{
          r = call();
        } catch(Exception e){return;}
        setResult(r);
    }

    public synchronized T getResult() {
        while(!isDone){
            try{
                wait();
            }catch(InterruptedException e){}
        }

        return result;
    }

    public synchronized void setResult(T result) {
        if(isDone){
            return;
        }
        this.result = result;
        isDone = true;
        notifyAll();
    }
}