package bag_of_tasks;

import java.util.*;

class UI {

    public static Bag bag;
    public static void main(String[] args) {
        int primesToFind;
        List<Task> tasks = new ArrayList<Task>(){};

        bag = new Bag(10);

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
                    bag.addTask(task);
                    tasks.add(task);
                    System.out.println("Added task " + i + " to the bag");
                }
                break;
            }
        }

        for (Task t : tasks) {
            System.out.println("The result is: " + t.getResult());
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




