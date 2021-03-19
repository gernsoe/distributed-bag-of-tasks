import bag_of_tasks.*;

public class PrimeTask extends Task {
    int numberToFind;

    public PrimeTask(int numberToFind){
        this.numberToFind = numberToFind;
    }

    public Integer call(){
        return (int) Math.floor((fact(numberToFind)%(numberToFind+1))/numberToFind)*(numberToFind-1)+2;
    }

    public int fact(int n) {
        if (n==0) {
            return 1;
        } else {
            return (n*fact(n-1));
        }
    }

    public int getInput() {
        return numberToFind;
    }
}