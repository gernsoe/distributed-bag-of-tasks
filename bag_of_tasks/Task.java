package bag_of_tasks;

import java.util.concurrent.Callable;

public abstract class Task<T> implements Callable<T>, Runnable {
    Boolean isDone = false;
    String errorMsg = null;
    T result;

    public void run(){
        T r;
        try{
          r = call();
          setResult(r);
        } catch(Exception e){
            errorMsg = "This task failed: " + e;
            isDone = true;
            return;
        }
    }

    public synchronized T getResult() throws Exception {
        while(!isDone){
            try{
                wait();
            }catch(InterruptedException e){}
        }

        if (errorMsg == null) {
            return result;
        } else {
            throw new Exception(errorMsg);
        }
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