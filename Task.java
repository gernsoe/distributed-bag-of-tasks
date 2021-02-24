import java.util.concurrent.Callable;

abstract class Task<T> implements Callable<T>, Runnable {
    int ID;
    Boolean isDone = false;
    T result;

    public Task(int ID) {
        this.ID = ID;
    }

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
        this.result = result;
        isDone = true;
        notifyAll();
    }

    public int getID() {
        return ID;
    }

}