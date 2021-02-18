public class primeTask extends Task {
    int numberToFind;

    public primeTask(int ID){
        super(ID);
        this.numberToFind = ID;
    }

    @Override
    public Integer call(){
        return (int) Math.floor((fact(numberToFind)%(numberToFind+1))/numberToFind)*(numberToFind-1)+2;
    }

    public void run(){
        int r = call();
        setResult(r);
    }

    public int fact(int n) {
        if (n==0) {
            return 1;
        } else {
            return (n*fact(n-1));
        }
    }
}
