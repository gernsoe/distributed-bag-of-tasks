import java.util.concurrent.Callable;

class Task implements Callable<Integer> {
    int ID;
    int result = 0;

    public Task(int ID) {
        this.ID = ID;
    }

    public Integer call() {
        /*
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
        */

        return 1;
    }

    public int getID() {
        return ID;
    }

    public int fact(int n) {
        if (n==0) {
            return 1;
        } else {
            return (n*fact(n-1));
        }
    }
}