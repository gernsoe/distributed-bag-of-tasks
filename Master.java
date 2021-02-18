import java.util.*;
import java.util.concurrent.*;

class UI {

    public static Bag bag;
    public static void main(String[] args) {
        int primesToFind;
        List<Task> tasks = new ArrayList<>(){};

        bag = new Bag(10);

        System.out.println("This program will calculate the n first prime numbers. Press \"r\" for results and \"q\" to quit.");
        System.out.println("Input how many prime numbers do you want to compute:");

        while(true) {
            Scanner in = new Scanner(System.in);

            String input = in.next();

            if (isInt(input)){
                primesToFind = Integer.parseInt(input);
                System.out.println("Finding first " + primesToFind + " prime numbers...");

                for (int i = 1; i <= primesToFind; ++i) {
                    Task task = new primeTask(i);
                    bag.addTask(task);
                    tasks.add(task);
                    System.out.println("Added task " + i + " to the bag");
                }
                break;
            }
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {}

        for (int i = 0; i < tasks.size(); ++i) {
            Task task = tasks.get(i);
            System.out.println("The result of task: " + task.getID() + ". is: " + task.getResult());
        }
    }

    /*
    public static void getResults() {
        try {
            List<Future<Integer>> futures = bag.getFutures();
            for (int i = 0; i < futures.size(); ++i) {
                Integer result = futures.get(i).get();
                System.out.println("The " + i + ". result is: " + result);
            }
        } catch (InterruptedException e) {
            System.out.println(e);
        } catch (ExecutionException e) {
            System.out.println(e);
        }
    }
    */

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




