package org.example.mongo;

import org.bson.types.ObjectId;
import org.example.collection.Task;

import java.util.List;

public interface Mongo {

    /**
     *
     * @param task
     * @author Brano
     *
     */

    //vlozenie do databazy
    public void insertTask(Task task);

    //update
    public void setTaskToDone(ObjectId id);

    //ziskanie vsetkych
    public List<Task> getAllTasks();

    //ziskanie urobenych
    public List<Task> getAllTasks(boolean done);

    //ziskanie taskov podla priority
    public List<Task> getAllTasksByPriority(int priority);

    //ziskanie taskov podla mena
    public List<Task> getAllTasksByName(String name);

    //vymaze tasky Done
    public void deleteDoneTasks();
}
