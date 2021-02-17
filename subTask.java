public class subTask extends Task {

    public subTask(int ID){
        super(ID);
    }

    @Override
    public Integer call(){
        return 2;
    }
}
