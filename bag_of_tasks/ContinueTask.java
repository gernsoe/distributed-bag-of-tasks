package bag_of_tasks;

import java.util.UUID;

public class ContinueTask<A,T> extends SystemTask<A,T>{

    ContinueInput inputFunction;
    public ContinueTask(ContinueInput inputFunction, UUID predecessor1_ID){
        setType(TaskType.CONTINUE);
        this.inputFunction = inputFunction;
        this.predecessor_1_ID = predecessor1_ID;
    }

    public T call(){
        return (T) inputFunction.exec(parameter1);
    }
}