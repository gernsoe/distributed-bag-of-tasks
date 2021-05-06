package bag_of_tasks;

import java.io.Serializable;

public interface ContinueInput<A,T> extends Serializable{
    T exec(A result);
}