import bag_of_tasks.*;
import java.util.*;

class userProgram {
    public static void main(String[] args) {
        int primesToFind;
        List<Task> futures = new ArrayList<Task>(){};

        BagOfTasks bag = new BagOfTasks(10);

        System.out.println("This program will calculate the n first prime numbers.");
        System.out.println("Input how many prime numbers do you want to compute:");

        while(true) {
            Scanner in = new Scanner(System.in);
            String input = in.next();

            if (isInt(input)){
                primesToFind = Integer.parseInt(input);
                System.out.println("Finding first " + primesToFind + " prime numbers...");

                for (int i = 1; i <= primesToFind; ++i) {
                    Task task = new primeTask(i);
                    bag.submitTask(task);
                    futures.add(task);
                    System.out.println("Added task " + i + " to the bag");
                }
                break;
            }
        }

        for (Task t : futures) {
            try {
                System.out.println("The result is: " + t.getResult());
            } catch (Exception e) {
                System.out.print(e);
            }

        }
    }

    public static boolean isInt(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Please input a valid positive integer.");
            return false;
        }
        return true;
    }
}

class primeTask extends Task {
    int numberToFind;

    public primeTask(int numberToFind){
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




