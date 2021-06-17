import bag_of_tasks.*;

public class PrimeTask extends Task {
    int numberToFind;

    public PrimeTask(int numberToFind){
        this.numberToFind = numberToFind;
    }

    public Integer call() throws InterruptedException{
        int counter = 0;
        for(int i=0; i < Integer.MAX_VALUE/2; i++){
            for(int j=0; j < 10; j++){
                counter++;
                if(counter > (Integer.MAX_VALUE/4)){
                    counter = 0;
                }
            }
        }
        return numberToFind;
    }
}