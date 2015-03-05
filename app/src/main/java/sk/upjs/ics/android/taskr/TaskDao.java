package sk.upjs.ics.android.taskr;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public enum TaskDao {
    INSTANCE;

    private List<Task> tasks = new LinkedList<Task>();

    private long idGenerator = 0;

    private TaskDao() {
        Task zubar = new Task("Zubár", /* is done */ false);
        saveOrUpdate(zubar);

        Task obed = new Task("Obed", /* is done */ true);
        saveOrUpdate(obed);
    }

    public void saveOrUpdate(Task task) {
        if (task.getId() == null) {
            task.setId(idGenerator++);
            tasks.add(task);
        } else {
            Iterator<Task> iterator = tasks.iterator();
            int index = 0;
            while (iterator.hasNext()) {
                Task t = iterator.next();
                if (t.getId().equals(task.getId())) {
                    iterator.remove();
                    break;
                }
                index++;
            }
            tasks.add(index, task);
        }
    }

    public List<Task> list() {
        return new LinkedList<Task>(this.tasks);
    }

    public Task getTask(long taskId) {
        for (Task task : this.tasks) {
            if (task.getId() == taskId) {
                return task;
            }
        }
        return null;
    }

    public void delete(Task task) {
        Iterator<Task> iterator = this.tasks.iterator();
        while(iterator.hasNext()) {
            Task t = iterator.next();
            if(t.getId() == task.getId()) {
                iterator.remove();
            }
        }
    }
} 