package me.phum.pocketigl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    private ArrayList<String> Users = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_picker_view);

        Users.add("string1");
        Users.add("string2");
        Users.add("string3");
        Users.add("string4");
        Users.add("string5");

        initRecyclerView();
    }
    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.player_list);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(Users, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
