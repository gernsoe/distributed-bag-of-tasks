import java.util.concurrent.Callable;

abstract class Task implements Callable<Integer>, Runnable {
    Boolean isDone = false;
    Integer result;

    public Task() {

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
}