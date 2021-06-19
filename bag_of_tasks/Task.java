package bag_of_tasks;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.Callable;

public abstract class Task<T> implements Callable<T>, Runnable, Serializable {

    UUID ID;

    Boolean isDone = false;
    String errorMsg = null;
    Exception exception;
    protected T result;

    protected Task(){
        ID = UUID.randomUUID();
    }

    public void run() {
        T r;
        try {
            r = call();
            setResult(r);
        } catch (Exception e) {
            setFailure(e);
        }
    }

    public synchronized T getResult() throws Exception {

        while (!isDone) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (errorMsg == null) {
            return result;
        } else {
            return (T) errorMsg;
        }
    }

    public synchronized void setResult(T result) {
        if (isDone) {
            return;
        }
        this.result = result;
        isDone = true;
        notifyAll();
    }

    public synchronized void setFailure(Exception e) {
        if (isDone) {
            return;
        }
        exception = e;
        this.errorMsg = "Task("+ID+") failed with: " + e;
        isDone = true;
        notifyAll();
    }

    public UUID getID() {
        return ID;
    }

    public Boolean getIsDone() {
        return isDone;
    }

    public void printStackTrace(){
        exception.printStackTrace();
    }
}