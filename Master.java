import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.io.*;

class Master {
    public static void main(String[] args) {
        int primesToFind;

        System.out.println("This program will calculate the n first prime numbers.");
        System.out.println("Input how many prime numbers do you want to compute:");

        while (true) {
            Scanner in = new Scanner(System.in);

            try {
                primesToFind = in.nextInt();
                System.out.println("Finding " + primesToFind + " prime numbers...");
                break;
            } catch (InputMismatchException e) {
                System.out.println("Please input a valid integer");
                continue;
            }
        }

        for (int i = 0; i < primesToFind; ++i) {
            //int id = Bag.getId();
            //Bag.addTask(id, i);
        }
    }
}
