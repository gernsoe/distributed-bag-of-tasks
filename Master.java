import java.util.*;
import java.util.concurrent.*;

class UI {

    public static Bag bag;
    public static void main(String[] args) {
        int primesToFind;

        bag = new Bag();

        Workers workers = new Workers(bag);
        Thread masterThread = new Thread(workers);
        masterThread.start();

        System.out.println("This program will calculate the n first prime numbers. Press \"r\" for results and \"q\" to quit.");

        while(true) {
            System.out.println("Input how many prime numbers do you want to compute:");

            Scanner in = new Scanner(System.in);

            String input  = in.next();

            if (input.equals("r")) {
                getResults();
            }
            else if (input.equals("q")) {
                System.exit(0);
            }
            else {
                primesToFind = Integer.parseInt(input);
                System.out.println("Finding first " + primesToFind + " prime numbers...");

                for (int i = 1; i <= primesToFind; ++i) {
                    Task task = new Task(i);
                    bag.addTask(task);
                    System.out.println("Added task " + i + " to the bag");
                }
            }
        }
    }

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
}

class Workers implements Runnable {
    Bag bag;

    public Workers(Bag bag) {
        this.bag = bag;
    }

    public void run() {
        try {
            ExecutorService engine = Executors.newFixedThreadPool(10);
            while (true) {
                Task task;

                while (true) {
                    task = bag.getTask();
                    if (task == null) {
                        Thread.sleep(1000);
                    } else {
                        break;
                    }
                }

                bag.addFuture(engine.submit(task));
                System.out.println("Started working on task with id: " + task.getID());
            }
        } catch (InterruptedException e) {}
    }
}


