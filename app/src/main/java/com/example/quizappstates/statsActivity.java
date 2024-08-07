package com.example.quizappstates;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class statsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TextWatcher {
    ListView listview;
    ArrayList<Player> players;
    PlayerAdapter adapter;
    Spinner spinner2;
    EditText search;
    ArrayList<Player> master = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        listview=findViewById(R.id.listview);
        spinner2=findViewById(R.id.spinner2);
        spinner2.setOnItemSelectedListener(this);
        search=findViewById(R.id.search);
        search.addTextChangedListener(this);
        players = (ArrayList<Player>) getIntent().getSerializableExtra("players");

        master.addAll(players);
        Collections.sort(players, Comparator.comparingInt(Player::getScore).reversed());
         adapter=new PlayerAdapter(this,players);
         listview.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 1) { //desc
                Collections.sort(players, Comparator.comparingInt(Player::getScore).reversed());
            adapter.notifyDataSetChanged();//refresh the screen after edits

        } else if (position == 2) {//asc
                Collections.sort(players, Comparator.comparingInt(Player::getScore));
            adapter.notifyDataSetChanged();//refresh the screen after edits
        } else if (position == 3) {//desc
                Collections.sort(players, Comparator.comparing(Player::getName).reversed());
              adapter.notifyDataSetChanged();//refresh the screen after edits
        } else if (position == 4) {//asc
                Collections.sort(players, Comparator.comparing(Player::getName));

            adapter.notifyDataSetChanged();//refresh the screen after edits
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(search.getText().toString().isEmpty()){
            players.clear();
            players.addAll(master);
            adapter.notifyDataSetChanged();
            return;
        }
            players.clear();
            players.addAll(master);
            List<Player> filtered = players.stream()
                    .filter(player -> player.getName().contains(search.getText().toString()))
                    .collect(Collectors.toList());
            players.clear();
            players.addAll(filtered);
            adapter.notifyDataSetChanged();


    }

    class PlayerAdapter extends ArrayAdapter<Player>{

        public PlayerAdapter(@NonNull Context context, ArrayList<Player> players) {
            super(context, 0,players);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if(convertView==null){
                convertView = getLayoutInflater().inflate(R.layout.player_name, parent, false);
            }
            TextView playername=convertView.findViewById(R.id.player_name);
            TextView score=convertView.findViewById(R.id.soreText);
            playername.setText(getItem(position).getName());
            score.setText("Score: "+String.valueOf(getItem(position).getScore()));
            return convertView;

        }
    }

}