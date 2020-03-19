package com.example.to_dolist;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import static com.example.to_dolist.R.id.action_add_text;
import static com.example.to_dolist.R.id.add_text;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD = 1002 ;
    DbHelper dbHelper;
    ArrayAdapter<String> mAdapter;
    ListView FirstTask;
    private FloatingActionButton add_text;
    private RecyclerView recyclerView;
    private SQLiteDatabase database;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper=new DbHelper(this);

        FirstTask=(ListView) findViewById(R.id.FirstTask);

        loadTaskList();

    }

    private void loadTaskList() {
        ArrayList<String> taskList=dbHelper.getTaskList();
        if (mAdapter==null) {
            mAdapter=new ArrayAdapter<String>(this,R.layout.row,R.id.task_title,taskList);
            FirstTask.setAdapter(mAdapter);
        }
        else{
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        Drawable icon = menu.getItem(0).getIcon();
        icon.mutate();
        icon.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case action_add_text :
            final EditText taskEditText=new EditText(this);
                AlertDialog dialog=new AlertDialog.Builder(this)
                        .setTitle("Add New Task")
                        .setMessage("What do you want to do next?")
                        .setView(taskEditText)
                        .setPositiveButton("add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task=String.valueOf(taskEditText.getText());
                                dbHelper.insertNewTask(task);
                                loadTaskList();
                            }
                        })
                        .setNegativeButton("cancel", null)
                        .create();
                dialog.show();
                        return true;

        }

        return super.onOptionsItemSelected(item);
    }
    public void deleteTask(View view) {
        View parent=(View)view.getParent();
        TextView taskTextView=(TextView)parent.findViewById(R.id.task_title);
        String task=String.valueOf(taskTextView.getText());
        dbHelper.deleteTask(task);
        loadTaskList();

    }

}

