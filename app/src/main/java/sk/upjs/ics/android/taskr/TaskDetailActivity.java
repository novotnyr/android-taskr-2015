package sk.upjs.ics.android.taskr;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;


public class TaskDetailActivity extends ActionBarActivity {

    public static final String TASK_BUNDLE_KEY = "task";

    public static final String TASK_ID_EXTRA = "taskId";

    private EditText taskNameEditText;

    private CheckBox taskDoneCheckBox;

    private TaskDao taskDao = TaskDao.INSTANCE;

    private Task task;

    private boolean ignoreSaveOnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        taskNameEditText = (EditText) findViewById(R.id.taskNameEditText);
        taskDoneCheckBox = (CheckBox) findViewById(R.id.taskDoneCheckBox);

        if(savedInstanceState != null) {
            task = (Task) savedInstanceState.get(TASK_BUNDLE_KEY);
        } else {
            Long taskId = (Long) getIntent().getSerializableExtra(TASK_ID_EXTRA);
            if(taskId != null) {
                task = taskDao.getTask(taskId);
            } else {
                task = new Task();
            }
        }
        taskNameEditText.setText(task.getName());
        taskDoneCheckBox.setChecked(task.isDone());
    }

    private void saveTask() {
        if(ignoreSaveOnFinish) {
            ignoreSaveOnFinish = false;
            return;
        }

        task.setName(taskNameEditText.getText().toString());
        task.setDone(taskDoneCheckBox.isChecked());
        taskDao.saveOrUpdate(task);

        Log.d(getClass().getName(), "Task " + task + " was saved");
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveTask();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        saveTask();
        outState.putSerializable(TASK_BUNDLE_KEY, task);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.deleteTaskMenu) {
            taskDao.delete(task);
            ignoreSaveOnFinish = true;
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
