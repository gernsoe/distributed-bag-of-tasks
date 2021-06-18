package bag_of_tasks;

import java.util.concurrent.LinkedBlockingQueue;

public class ExtendedQueue<T> extends LinkedBlockingQueue<T> {

    int threshold = Integer.MAX_VALUE;

    public ExtendedQueue(){
        super();
    }

    public T eTake() throws  InterruptedException{
        T e = take();
        synchronized (this) {
            notify();
        }
        return e;
    }

    public synchronized void ePut(T e) throws InterruptedException{
        while(size() > threshold){
                try {
                    wait();
                } catch (InterruptedException err) {
                    err.printStackTrace();
                }
        }
        put(e);
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold){
        this.threshold = threshold;
    }

    public synchronized void incrementThreshold(){
        threshold++;
        notify();
    }

    public synchronized void decrementThreshold(){
        threshold--;
    }


}
