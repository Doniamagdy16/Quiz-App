package com.example.quizappstates;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlayerActivity extends AppCompatActivity implements TextWatcher {
    EditText nameText;
    TextView lastWinner;
    Button startPlaying,stats;
    ArrayList<Player>players=new ArrayList<>();
    SharedPreferences pref;
    int size;

    ArrayList<Player> filtered=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        highest_scores.started=false;
        nameText=findViewById(R.id.nameText);
        startPlaying=findViewById(R.id.startPlaying);
        stats=findViewById(R.id.stats);
        lastWinner=findViewById(R.id.lastWinner);
        nameText.addTextChangedListener( this);
        pref=getSharedPreferences("players",MODE_PRIVATE);
        size=pref.getInt("size",0);
     //   filtered.clear();
        lastWinner.setText("");
        for (int i = 0; i < size; i++) {
            String name = pref.getString("name"+i, "");
            int score = pref.getInt("score"+i, 0);
                filtered.add(new Player(name, (byte) score));
                //highest_scores.started=false;
            if (!name.isEmpty()) {  // Check if retrieved name is not empty
                lastWinner.append("last winner is " + name+"\n");
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1000 && resultCode==RESULT_OK){
            if (data!=null){
                byte score = data
                        .getByteExtra("score", (byte) -1);
                Toast.makeText(this, "score ="+score, Toast.LENGTH_SHORT).show();
                players.add(new Player(nameText.getText().toString(), score));
                 nameText.setText("");
                if (players.size()>1)
                    stats.setEnabled(true);
            }

        }
    }

    @Override
    public void onBackPressed() {
        filtered.clear();
       // highest_scores.started=false;
        Optional<Player> optional;
            optional = players.stream().max(Comparator.comparingInt(Player::getScore));
            if (optional.isPresent()) {
                filtered = (ArrayList<Player>) players.stream().filter(x ->
                        x.getScore()==optional.get().getScore()
                ).collect(Collectors.toList());
                SharedPreferences.Editor editor = pref.edit();
               // editor.clear();
                for (int i = 0; i < filtered.size(); i++) {
                    editor.putString("name"+i, filtered.get(i).getName());
                    editor.putInt("score"+i,filtered.get(i).getScore());
                }
                editor.putInt("size" , filtered.size());
                editor.apply();
            }
            finish();
            super.onBackPressed();
    }

    public void startPlaying(View view) {
        highest_scores.started=true;
        String name=nameText.getText().toString().toLowerCase();
            if (players.stream().anyMatch(player -> player.getName().equals(name)) ){
                Optional<Player> optional = players.stream()
                        .filter(player -> player.getName().equals(name))
                        .findFirst();
                if (optional.isPresent()){
                    Player p=optional.get();
                    Toast.makeText(this, "score="+p.getScore(), Toast.LENGTH_SHORT).show();
                    return;
                }


        }
        Intent a=new Intent(this, MainActivity.class);
        a.putExtra("name",nameText.getText().toString());
//        startActivity(a);
        startActivityForResult(a,1000);
    }



    public void stats(View view) {
        Intent a=new Intent(this, statsActivity.class);
        a.putExtra("players",players);
        startActivity(a);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(s.length()==0) startPlaying.setEnabled(false);
        else startPlaying.setEnabled(true);
    }

    public void highest_score(View view) {
      //  if(highest_scores.started==false) filtered.clear();
        Intent a=new Intent(this, highest_scores.class);
        a.putExtra("data",filtered);
       // Toast.makeText(this, "size1"+filtered.size(), Toast.LENGTH_SHORT).show();
        startActivity(a);
    }
}