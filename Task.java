import java.util.concurrent.Callable;

abstract class Task implements Callable<Integer>{
    int ID;
    Integer result;

    public Task(int ID) {
        this.ID = ID;
    }

    public Integer getResult() {
        return result;
    }

    public int getID() {
        return ID;
    }

}