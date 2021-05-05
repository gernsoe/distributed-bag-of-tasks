package bag_of_tasks;

public interface CombineInput<A,T>{
    T combine(A param1, A param2);
}