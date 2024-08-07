package com.example.quizappstates;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class highest_scores extends AppCompatActivity {
    ListView scores_list;
    static boolean started=false;
    ArrayList<Player> data =new ArrayList<>();;
    Button clear;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ArrayList<Player>names=new ArrayList<>();
    int size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highest_scores);
        scores_list=findViewById(R.id.scores_list);
        clear=findViewById(R.id.clear);;
        data= (ArrayList<Player>) getIntent().getSerializableExtra("data");
        pref = getSharedPreferences("players_data" , MODE_PRIVATE);
        editor=pref.edit();
        scores_adapter adapter = new scores_adapter(this, data);
        scores_list.setAdapter(adapter);
        size= pref.getInt("Size",0);
        for (int i = 0; i < size; i++) {
            String existingName = pref.getString("names" + i, "");
            byte score = Byte.parseByte(pref.getString("scores"+i, ""));
            names.add(new Player(existingName, score) );
        }
        List<Player> uniquePlayers = data.stream()
                .filter(player -> !names.stream()
                        .anyMatch(existing -> existing.getName().equals(player.getName()) &&
                                existing.getScore() == player.getScore()))
                .collect(Collectors.toList());
        if (uniquePlayers.size() != 0) {
            for (int i = 0; i < data.size(); i++) {
                int index = size + i;
                editor.putString("names" + index, data.get(i).getName());
                editor.putString("scores" + index, String.valueOf(data.get(i).getScore()));
                editor.putInt("Size", (data.size() + size));
             }
            editor.apply();
            }


        data.clear();
        for (int i = 0; i < size; i++) {
                String name = pref.getString("names"+i, "");
                byte score = Byte.parseByte(pref.getString("scores"+i, ""));
                if(!name.isEmpty()||!name.equals("")) {
                    data.add(new Player(name, score));
                    adapter.notifyDataSetChanged();
                    clear.setEnabled(true);
                }
                else clear.setEnabled(false);
            }


        }

    public void clear(View view) {

        if (data != null) {
            data.clear();
        }
        editor.clear();
        editor.apply();
        Toast.makeText(this, "cleared", Toast.LENGTH_SHORT).show();
        finish();
    }


    class scores_adapter extends ArrayAdapter<Player>{

        public scores_adapter(@NonNull Context context, ArrayList<Player> players) {
            super(context, 0,players);

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
           if(convertView==null)
               convertView=getLayoutInflater().inflate(R.layout.player_name,parent,false);
            TextView player_name=convertView.findViewById(R.id.player_name);
            TextView score=convertView.findViewById(R.id.soreText);
            player_name.setText("");
            score.setText("");
            player_name.setText(getItem(position).getName());
            score.setText(String.valueOf(getItem(position).getScore()));
            return convertView;
        }
    }
}