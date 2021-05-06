package bag_of_tasks;

public class ContinueTask<A,T> extends SystemTask<A,T>{

    ContinueInput inputFunction;
    public ContinueTask(ContinueInput inputFunction){
        setType(TaskType.CONTINUE);
        this.inputFunction = inputFunction;
    }

    public T call(){
        return (T) inputFunction.exec(parameter1);
    }
}