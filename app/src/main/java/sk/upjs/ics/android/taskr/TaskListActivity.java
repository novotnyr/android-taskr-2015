package sk.upjs.ics.android.taskr;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Paint;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import static sk.upjs.ics.android.taskr.TaskDetailActivity.TASK_ID_EXTRA;


public class TaskListActivity extends ActionBarActivity {

    private ListView tasksListView;

    private TaskDao taskDao = TaskDao.INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        tasksListView = (ListView) findViewById(R.id.tasksListView);

        tasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = taskDao.list().get(position);

                Intent intent = new Intent(TaskListActivity.this, TaskDetailActivity.class);
                intent.putExtra(TASK_ID_EXTRA, task.getId());

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayAdapter<Task> taskAdapter = new ArrayAdapter<Task>(this, android.R.layout.simple_list_item_1, taskDao.list()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView listItemView = (TextView) super.getView(position, convertView, parent);

                Task task = getItem(position);
                if(task.isDone()) {
                    listItemView.setPaintFlags(listItemView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                listItemView.setText(task.getName());
                return listItemView;
            }
        };
        tasksListView.setAdapter(taskAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.addNewTaskMenu) {
            Intent intent = new Intent(this, TaskDetailActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
