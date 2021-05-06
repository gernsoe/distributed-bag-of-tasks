package bag_of_tasks;

import java.util.UUID;

public class CombineTask<A,T> extends SystemTask<A,T>{

    CombineInput inputFunction;
    public CombineTask(CombineInput inputFunction, UUID predecessor1_ID, UUID predecessor2_ID){
        setType(TaskType.COMBINE);
        this.inputFunction = inputFunction;
        this.predecessor_1_ID = predecessor1_ID;
        this.predecessor_2_ID = predecessor2_ID;
    }

    public T call(){
        return (T) inputFunction.combine(parameter1, parameter2);
    }
}
