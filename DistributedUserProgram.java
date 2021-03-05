import bag_of_tasks.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class DistributedUserProgram {

    public static void main(String[] args) {
        int primesToFind;
        List<Task> futures = new ArrayList<Task>(){};

        BagOfTasks masterBag = new MasterBag(1);
        UserNode node = new UserNode(5,masterBag);
        Thread thread = new Thread(node);
        thread.start();

        System.out.println("This program will calculate the n first prime numbers.");
        System.out.println("Input how many prime numbers do you want to compute:");

        while(true) {
            Scanner in = new Scanner(System.in);
            String input = in.next();

            if (isInt(input)){
                primesToFind = Integer.parseInt(input);
                System.out.println("Finding first " + primesToFind + " prime numbers...");

                for (int i = 1; i <= primesToFind; ++i) {
                    Task task = new PrimeTask(i);
                    masterBag.submitTask(task);
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

    public static class UserNode extends Thread{

        NodeBag node;

        UserNode(int numberOfWorkers, BagOfTasks masterBag){
            node = new NodeBag(numberOfWorkers, masterBag);
        }

        public void run(){
            while(true){
                node.takeTaskFromMaster();
                System.out.println("spam");
            }
        }
    }
}




