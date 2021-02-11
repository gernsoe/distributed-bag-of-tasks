import java.util.concurrent.ConcurrentHashMap;

class Bag {
    ConcurrentHashMap<Integer,Integer> taskBag;
    int oldestID = 1;

    public void Bag(){
        this.taskBag = new ConcurrentHashMap<>();
    }

    public void addTask(int id, int i) {
        taskBag.put(id,i);
    }

    public Tuple getOldestTask() {
        int ret = oldestID;
        oldestID++;
        return new Tuple(ret,taskBag.get(ret));

    }

    public boolean isEmpty(){
        return taskBag.isEmpty();
    }



}

