package com.eventtracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.eventtracker.model.EventDetails;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Personal_ToDo_Fragment extends Fragment  {

    FloatingActionButton btnAddToDo;
    GridView lstOnGoingToDo;
    GridView lstCompletedToDo;
    View mView;
    Context context ,appContext;
    AlertDialog toDoDialog;
    MainActivity mainActivityInstance;
    Calendar currentDate;
    int day, month, year;
    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> adapter;
    private List<ApplicationInfo> mAppList;
    int counter = 0;

    TextView colorPriority;

    RadioGroup radioGroup;
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PRIORITY = "priority";
    // Declare a member variable to keep track of a task's selected mPriority
    private String mPriority;
    // Class variables for the Cursor that holds task data and the Context
    private Cursor mCursor;
    private Context mContext;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TaskDetails taskDetails;
    SwipeMenuListView listView;

    public Personal_ToDo_Fragment()
    {
       // this.mainActivityInstance = (MainActivity)MainActivity.getActivityClass();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_personal_todo, container, false);

        View rootView = inflater.inflate(R.layout.todo_add_task, container, false);


        context = getActivity().getApplicationContext();
        appContext = getContext();
        listView = (SwipeMenuListView) mView.findViewById(R.id.listView);


        taskDetails = new TaskDetails();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("GetItDone").child("Users").child("UserID").child("TO-DO").child("TaskID");

        adapter = new ArrayAdapter<String>(appContext,android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem editItem = new SwipeMenuItem(
                        context);
                // set item background
                editItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                editItem.setWidth(170);
                // set item title
                editItem.setIcon(R.drawable.edit);
                // set item title fontsize
                editItem.setTitleSize(18);
                // add to menu
                menu.addMenuItem(editItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        context);
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        toDoDialog.show();
                        break;
                    case 1:
                        // delete
                        Toast.makeText(getActivity(), "Removed item",
                                Toast.LENGTH_LONG).show();
                        adapter.remove(list.get(position));
                        adapter.notifyDataSetChanged();
                        String pos = String.valueOf(position);
                        databaseReference = firebaseDatabase.getReference("GetItDone").child("Users").child("UserID").child("TO-DO").child("TaskID").child(pos);
                        System.out.println("*8*8/*/*/*/*/"+databaseReference);
                        databaseReference.child("TaskTitle").removeValue();
                        databaseReference.child("TaskDueDate").removeValue();
                        databaseReference.child("TaskPriority").removeValue();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        //prepare components
        prepareUiComponenents();


        //prepare dialogue box
        prepareUiDialogueBoxInvokers();

        viewTasks();

        currentDate = Calendar.getInstance();
        day = currentDate.get(Calendar.DAY_OF_MONTH);
        month = currentDate.get(Calendar.MONTH);
        year = currentDate.get(Calendar.YEAR);

        month = month+1;
        return mView;


    }

    private void viewTasks() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                list.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"+postSnapshot);

                    try{
                        TaskDetails task = postSnapshot.getValue(TaskDetails.class);
                        System.out.println("*************"+task.getTaskTitle().toString()+"-----------");
                        list.add(task.getTaskTitle().toString()+"\n\n"+task.getTaskPriority().toString()+"\n\n"+task.getTaskDueDate());
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                     //String vale = taskDetails.TaskTitle;
                    // System.out.println("*********************"+vale+"******************");
                     //list.add(taskDetails.TaskTitle);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
            protected void prepareUiComponenents()
    {
        btnAddToDo =mView.findViewById (R.id.btnAddToDo);
        mPriority = "High";
    }

    protected void prepareUiDialogueBoxInvokers()
    {
        //for the add button
        btnAddToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //create dialog builder
                AlertDialog.Builder aBuilder = new AlertDialog.Builder(getContext());
                View tView = getLayoutInflater().inflate(R.layout.todo_add_task, null);


                RadioButton radioButton =  ((RadioButton) tView.findViewById(R.id.radButton1));
                radioButton.setChecked(true);

                Button btnAddSave = (Button) tView.findViewById(R.id.btnAddSave);
                Button btnAddCancel = (Button) tView.findViewById(R.id.btnAddCancel);

                Button btnRadio1 = (Button) tView.findViewById(R.id.buttonP1);

                final EditText edtAddTitle = (EditText) tView.findViewById(R.id.editAddTitle);
                final EditText edtAddDueDate = (EditText) tView.findViewById(R.id.editAddDueDate);

                edtAddDueDate.setText(day+"/"+month+"/"+year);

                edtAddDueDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog dpd = new DatePickerDialog(appContext, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                month = month+1;
                                edtAddDueDate.setText(day+"/"+month+"/"+year);
                            }
                        },year,month,day);
                        dpd.show();
                    }
                });
                btnAddCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //cancel dialogue
                        toDoDialog.cancel();
                    }
                });

                btnAddSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //save todo to list

                        // Check if EditText is empty, if not retrieve input and store it in a ContentValues object
                        // If the EditText input is empty -> don't create an entry
                        String title = edtAddTitle.getText().toString();
                        if(title.length() == 0){
                            Toast.makeText(getActivity(), "Please fill the input fields", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // Create new empty ContentValues object
//                        ContentValues contentValues = new ContentValues();
//                        // Put the task description and selected mPriority into the ContentValues
//                        contentValues.put("                              ", mPriority);
//                        Toast.makeText(getActivity(), "Priority is: "+contentValues, Toast.LENGTH_LONG ).show();


//                        GradientDrawable priorityCircle = new GradientDrawable();
//                        int priorityColor = getPriorityColor(mPriority);
//                        priorityCircle.setColor(priorityColor);

//                        edtAddDueDate.setBackground(priorityCircle);
                        String dueDate = edtAddDueDate.getText().toString();
                        saveToDoDetails(title, mPriority , dueDate);

                    }
                });


                RadioGroup radioGroup = (RadioGroup) tView .findViewById(R.id.radioGroup);

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                {
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // checkedId is the RadioButton selected

                        switch(checkedId) {
                            case R.id.radButton1:
                                //Toast.makeText(getActivity(), "Radio 1", Toast.LENGTH_LONG ).show();
                                mPriority = "Priority:High";
                                break;
                            case R.id.radButton2:

                                //Toast.makeText(getActivity(), "Radio 2", Toast.LENGTH_LONG ).show();
                                mPriority = "Priority:Medium";
                                break;
                            case R.id.radButton3:
                                mPriority = "Priority:Low";
                                //Toast.makeText(getActivity(), "Radio 3", Toast.LENGTH_LONG ).show();
                                break;
                        }
                    }
                });

                aBuilder.setView(tView);
                toDoDialog = aBuilder.create();
                toDoDialog.show();

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(appContext,android.R.layout.simple_list_item_1, list) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);

                //if (flag == True) {
                text.setTextColor(Color.RED);
                // }

                return view;
            }
        };

    }

    public void saveToDoDetails(String title, String mPriority, String dueDate){
        //save to do
        //list.add(title+"\n\n"+dueDate);
        String id = databaseReference.push().getKey();
        databaseReference.child(id).child("TaskTitle").setValue(title);
        databaseReference.child(id).child("TaskDueDate").setValue(dueDate);
        databaseReference.child(id).child("TaskPriority").setValue(mPriority);
        //adapter.notifyDataSetChanged();
        toDoDialog.cancel();
    }

    /*
    Helper method for selecting the correct priority circle color.
    P1 = red, P2 = orange, P3 = yellow
    */
    private int getPriorityColor(int priority) {
        int priorityColor = 0;

        switch(priority) {
            case 1: priorityColor = ContextCompat.getColor(context, R.color.materialRed);
                break;
            case 2: priorityColor = ContextCompat.getColor(context, R.color.materialOrange);
                break;
            case 3: priorityColor = ContextCompat.getColor(context, R.color.materialYellow);
                break;
            default: break;
        }
        return priorityColor;
    }


    public void refreshToDoLists(){
        //refresh to do
    }

}