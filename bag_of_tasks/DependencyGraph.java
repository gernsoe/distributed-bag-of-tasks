
package bag_of_tasks;

import java.util.*;
import java.util.concurrent.BlockingQueue;

public class DependencyGraph {
    Map<Task, Set<SystemTask>> dependencyMap;
    BlockingQueue<Task> taskBag;

    public DependencyGraph(BlockingQueue<Task> taskBag) {
        dependencyMap = new HashMap<Task, Set<SystemTask>>();
        this.taskBag = taskBag;
    }

    public void addContinuation(Task dependant, SystemTask continueTask) {
        if (dependencyMap.containsKey(dependant)) {
            dependencyMap.get(dependant).add(continueTask);
        } else {
            Set continueSet = new HashSet<SystemTask>();
            continueSet.add(continueTask);
            dependencyMap.put(dependant, continueSet);
        }
    }

    public void releaseContinuations(Task task) throws InterruptedException, Exception {
        if (!dependencyMap.containsKey(task)) {
            return; //If no key task in InverseMap, nothing depends on task, therefore return
        }

        Set<SystemTask> continueSet = dependencyMap.remove(task);
        for(SystemTask continueTask : continueSet){
            continueTask.setParameter(task.getID(), task.getResult());
            if(!containsSysTask(continueTask)){
                taskBag.add(continueTask);
            }
        }
    }

    public Boolean containsSysTask(SystemTask sysTask){
        Boolean b = false;
        for(Map.Entry<Task, Set<SystemTask>> entry : dependencyMap.entrySet()) {
            Set<SystemTask> set = entry.getValue();
            if(set.contains(sysTask)){
                b = true;
            }
        }
        return b;
    }

}