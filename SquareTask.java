import bag_of_tasks.Task;

class squareTask extends Task {
    public int numberToSquare;

    public squareTask(int numberToSquare){
        this.numberToSquare = numberToSquare;
    }

    public Integer call() throws InterruptedException{
        Thread.sleep(5000);
        return numberToSquare*numberToSquare;
    }
}