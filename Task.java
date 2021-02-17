import java.util.concurrent.Callable;

class Task implements Callable<Integer> {
    int numberToFind;
    int result = 0;

    public Task(int numberToFind) {
        this.numberToFind = numberToFind;
    }

    public Integer call() {
        return (int) Math.floor((fact(numberToFind)%(numberToFind+1))/numberToFind)*(numberToFind-1)+2;
    }

    public int getNumberToFind() {
        return numberToFind;
    }

    public int fact(int n) {
        if (n==0) {
            return 1;
        } else {
            return (n*fact(n-1));
        }
    }
}