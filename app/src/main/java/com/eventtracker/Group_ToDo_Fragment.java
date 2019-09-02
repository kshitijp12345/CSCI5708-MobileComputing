package com.eventtracker;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class Group_ToDo_Fragment extends Fragment {
    FloatingActionButton btnFAB;
    AlertDialog addGroupDialog;
    View groupView;
    Context context ,appContext;
    ArrayList<String> data = new ArrayList<>();
    ArrayAdapter<String> adapter;


    public Group_ToDo_Fragment() {}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        groupView = inflater.inflate(R.layout.fragment_team_todo, container, false);

        context = getActivity().getApplicationContext();
        appContext = getContext();
        //data.add() is all that needs to be done to appear on screen. ezpz
        adapter = new ArrayAdapter(appContext, R.layout.activity_listview, data);
        ListView listView = groupView.findViewById(R.id.listViewList);
        listView.setAdapter(adapter);
        initEvents();



    return groupView;
    }
    protected void initEvents(){
        btnFAB = groupView.findViewById (R.id.btnFAB);
        btnFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                View addGroupView = getLayoutInflater().inflate(R.layout.create_todo_group, null);
                Button btnSave = (Button) addGroupView.findViewById(R.id.btnSave);
                Button btnCancel = (Button) addGroupView.findViewById(R.id.btnCancel);
                final EditText groupName = (EditText) addGroupView.findViewById(R.id.groupName);
                final EditText groupMembers = (EditText) addGroupView.findViewById(R.id.groupMembers);

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = groupName.getText().toString();
                        String members = groupMembers.getText().toString();
                        data.add(name+"\n"+members);
                        adapter.notifyDataSetChanged();
                        addGroupDialog.cancel();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addGroupDialog.cancel();
                    }
                });
                alertBuilder.setView(addGroupView);
                addGroupDialog = alertBuilder.create();
                addGroupDialog.show();
            }
        });

    }

    public ArrayList<String> getData (){
        return data;
    }
    public void setData (ArrayList<String> d){
        data = d;
        adapter.notifyDataSetChanged();
    }
    public void addData (String s){
        data.add(s);
        adapter.notifyDataSetChanged();
    }

}
