package bag_of_tasks;

import java.io.Serializable;

public interface CombineInput<A,T> extends Serializable {
    T combine(A param1, A param2);
}