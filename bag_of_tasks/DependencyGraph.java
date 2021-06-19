
package bag_of_tasks;
import java.util.*;

public class DependencyGraph {
    Map<Task, Set<SystemTask>> dependencyMap;
    MasterBag masterBag;

    public DependencyGraph(MasterBag masterBag) {
        dependencyMap = new HashMap<Task, Set<SystemTask>>();
        this.masterBag = masterBag;
    }

    public void addContinuation(Task antecedent, SystemTask continueTask) {
        if (dependencyMap.containsKey(antecedent)) {
            dependencyMap.get(antecedent).add(continueTask);
        } else {
            Set continueSet = new HashSet<SystemTask>();
            continueSet.add(continueTask);
            dependencyMap.put(antecedent, continueSet);
        }
    }

    public void releaseContinuations(Task task) throws Exception {
        if (!dependencyMap.containsKey(task)) {
            return; //If no key task in InverseMap, nothing depends on task, therefore return
        }

        Set<SystemTask> continueSet = dependencyMap.remove(task);
        for(SystemTask continueTask : continueSet){
            continueTask.setParameter(task.getID(), task.getResult());
            if(continueTask.getIsReady()){
                masterBag.submitTask(continueTask);
            }
        }
    }
}