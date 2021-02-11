import java.util.concurrent.Callable;

class Worker implements Callable<Integer> {
    int ID;
    Bag taskBag;
    int result = 0;

    public Worker(int ID, Bag taskBag) {
        this.ID = ID;
        this.taskBag = taskBag;
    }

    public Integer call() {
        while(true){

            while(taskBag.isEmpty()){
                try{wait();} catch (InterruptedException e){}
            }

            Tuple task = taskBag.getOldestTask();
            int taskID = task.getKey();
            int numberToFind = task.getValue();

            result = (int) Math.floor((fact(numberToFind)%(numberToFind+1))/numberToFind)*(numberToFind-1)+2;

            taskBag.addTask();


        }


        return result;
    }

    public int fact(int n) {
        if (n==0) {
            return 1;
        } else {
            return (n*fact(n-1));
        }
    }
}