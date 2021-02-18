import java.util.concurrent.Callable;

abstract class Task implements Callable<Integer>, Runnable {
    int ID;

    Integer result;

    public Task(int ID) {
        this.ID = ID;
    }

    public synchronized Integer getResult() {
        return result;
    }

    public synchronized void setResult(Integer result) {
        this.result = result;
    }

    public int getID() {
        return ID;
    }

}