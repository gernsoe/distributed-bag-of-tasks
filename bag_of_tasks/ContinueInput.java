package bag_of_tasks;

public interface ContinueInput<A,T>{
    T exec(A result);
}