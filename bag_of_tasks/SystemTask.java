package bag_of_tasks;
import java.util.UUID;

public abstract class SystemTask<A,T> extends Task<T>{

    UUID predecessor_1_ID;
    UUID predecessor_2_ID;

    A parameter1 = null;
    A parameter2 = null;

    TaskType type;
    boolean isReady = false;

    public void setParameter(UUID predecessorID, A parameter) throws Exception {
        if(predecessorID == predecessor_1_ID){
            this.parameter1 = parameter;
            checkIfReady();
        }else if(predecessorID == predecessor_2_ID){
            this.parameter2 = parameter;
            checkIfReady();
        } else {
            throw new Exception("The given ID didn't match any of the predecessor ID's");
        }
    }

    private void checkIfReady() {
        if (this.getType() == TaskType.CONTINUE && this.parameter1 != null) {
            isReady = true;
        } else if (this.getType() == TaskType.COMBINE && this.parameter1 != null && this.parameter2 != null) {
            isReady = true;
        }
    }

    protected TaskType getType() { return type; }

    protected void setType(TaskType type) { this.type = type; }

    protected boolean getIsReady() { return isReady; }
}