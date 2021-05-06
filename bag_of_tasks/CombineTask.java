package bag_of_tasks;

public class CombineTask<A,T> extends SystemTask<A,T>{

    CombineInput inputFunction;
    public CombineTask(CombineInput inputFunction){
        this.inputFunction = inputFunction;
    }

    public T call(){
        return (T) "Ost"; //inputFunction.exec(parameter1);
    }
}
