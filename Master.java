import java.util.*;
import java.util.concurrent.*;
import java.io.*;

class Master {

    public static void main(String[] args) {
        try {
            int primesToFind;
            Bag taskBag = new Bag();

            System.out.println("This program will calculate the n first prime numbers.");
            System.out.println("Input how many prime numbers do you want to compute:");

            while (true) {
                Scanner in = new Scanner(System.in);

                try {
                    primesToFind = in.nextInt();
                    System.out.println("Finding first " + primesToFind + " prime numbers...");
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Please input a valid integer");
                    continue;
                }
            }

            ExecutorService engine = Executors.newFixedThreadPool(10);

            List<Worker> listOfWorkers = new ArrayList<Worker>();

            for (int i = 1; i <= primesToFind; ++i) {
                //int id = Bag.getId();
                //Bag.addTask(id, i);
                Worker worker = new Worker(i,taskBag);
                listOfWorkers.add(worker);
            }

            List<Integer> results = null;

            List<Future<Integer>> futures = engine.invokeAll(listOfWorkers);

            results = new ArrayList<Integer>();

            for (int i = 0; i < futures.size(); ++i) {
                Future<Integer> future = futures.get(i);
                Integer result = future.get();

                results.add(result);
            }

            Collections.sort(results);

            for (int i = 0; i < results.size(); i++) {
                System.out.printf("The %d. prime is: " + results.get(i), i);
                System.out.println();
            }

            engine.shutdown();

        } catch (InterruptedException e) {
            System.out.println(e);
        } catch (ExecutionException e) {
            System.out.println(e);
        }
    }
}


