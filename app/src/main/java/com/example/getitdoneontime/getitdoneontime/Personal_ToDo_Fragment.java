package com.example.getitdoneontime.getitdoneontime;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class Personal_ToDo_Fragment extends Fragment {
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

    public Personal_ToDo_Fragment()
    {
        this.mainActivityInstance = (MainActivity)MainActivity.getActivityClass();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_personal_todo, container, false);
        context = getActivity().getApplicationContext();
        appContext = getContext();
        //prepare components
        prepareUiComponenents();


        //prepare dialogue box
        prepareUiDialogueBoxInvokers();

        currentDate = Calendar.getInstance();
        day = currentDate.get(Calendar.DAY_OF_MONTH);
        month = currentDate.get(Calendar.MONTH);
        year = currentDate.get(Calendar.YEAR);

        month = month+1;
        return mView;
    }

    protected void prepareUiComponenents()
    {

        btnAddToDo = (FloatingActionButton) mView.findViewById (R.id.btnAddToDo);
        //lstOnGoingToDo = (GridView) mView.findViewById (R.id.lstTodo);
        //lstCompletedToDo = (GridView) mView.findViewById (R.id.lstComplete);
    }

    protected void prepareUiDialogueBoxInvokers()
    {
        //for the add button
        btnAddToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //create dialog builder
                AlertDialog.Builder aBuilder = new AlertDialog.Builder(mainActivityInstance);
                View tView = getLayoutInflater().inflate(R.layout.todo_add_task, null);

                Button btnAddSave = (Button) tView.findViewById(R.id.btnAddSave);
                Button btnAddCancel = (Button) tView.findViewById(R.id.btnAddCancel);
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
                        String title = edtAddTitle.getText().toString();
                        String dueDate = edtAddDueDate.getText().toString();

                        saveToDoDetails(title, dueDate);
                    }
                });

                aBuilder.setView(tView);
                toDoDialog = aBuilder.create();
                toDoDialog.show();

            }
        });

//        lstOnGoingToDo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                //create dialog builder
//                AlertDialog.Builder aBuilder = new AlertDialog.Builder(mainActivityInstance);
//                View tView = getLayoutInflater().inflate(R.layout.todo_edit_task, null);
//
//                Button btnEditSave = (Button) tView.findViewById(R.id.btnEditSave);
//                Button btnEditCancel = (Button) tView.findViewById(R.id.btnEditCancel);
//                final EditText edtEditTitle = (EditText) tView.findViewById(R.id.editEditTitle);
//                final EditText edtEditDueDate = (EditText) tView.findViewById(R.id.editEditDueDate);
//
//                btnEditCancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        //cancel dialogue
//                        toDoDialog.cancel();
//                    }
//                });
//
//                btnEditSave.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        //save todo to list
//                        String title = edtEditTitle.getText().toString();
//                        String dueDate = edtEditDueDate.getText().toString();
//
//                        saveToDoDetails(title, dueDate);
//                    }
//                });
//
//                aBuilder.setView(tView);
//                toDoDialog = aBuilder.create();
//                toDoDialog.show();
//            }
//        });
    }

    public void saveToDoDetails(String title, String dueDate){
        //save to do
        toDoDialog.cancel();
    }

    public void refreshToDoLists(){
        //refresh to do
    }

}
