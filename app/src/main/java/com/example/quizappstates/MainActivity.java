package com.example.quizappstates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
TextView questionText,greet,questionnum;
Spinner spinner;
Button start,next;
byte score;
byte index;
String answer;
MediaPlayer player;
ArrayList<String>items=new ArrayList<>();
   List<Question> questions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        questionText=findViewById(R.id.questionText);
        start=findViewById(R.id.start);
        next=findViewById(R.id.next);
        spinner=findViewById(R.id.spinner);
        questionnum=findViewById(R.id.questionnum);
        greet=findViewById(R.id.greet);
        spinner.setEnabled(false);
        String name = getIntent().getStringExtra("name");
        greet.setText("hello "+name);


    }
    @Override
    public void onBackPressed() {
        if (player!=null)
            player.stop();
        super.onBackPressed();
    }
    public void start(View view) {
        if (player!=null)
            player.stop();
        index=0;
        score=0;
        next.setEnabled(true);
        spinner.setEnabled(true);
        questions= Arrays.asList(
                new Question("egypt","cairo"),
                new Question("usa","ws"),
                new Question("france","paris"),
                new Question("uk","london"));
        Collections.shuffle(questions);
        items.clear();
        Collections.addAll(items,"please select",
                "cairo",
                "damascus",
                "ws",
                "khartoum",
                "baghdad",
                "london",
                "tripoli",
                "paris",
                "toronto");
        ArrayAdapter adapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,items);
        spinner.setAdapter(adapter);
        questionText.setText("what is the capital of "+questions.get(index).getCountry());
        questionnum.setText("Question 1 of "+questions.size());
    }

    public void next(View view) {
        answer=spinner.getSelectedItem().toString();
        spinner.setSelection(0);
        if (answer.equalsIgnoreCase("please select")){
            Toast.makeText(this, "please answer", Toast.LENGTH_SHORT).show();
            return;
        }
        if(answer.equals(questions.get(index).getCapital())){
            score++;
            items.remove(answer);

        }
        index++;
        if(index<questions.size()){
            questionText.setText("what is the capital of "+questions.get(index).getCountry());
            questionnum.setText("Question "+ (index+1)+" of "+questions.size());
        }
        else {
            next.setEnabled(false);
            spinner.setEnabled(false);
//            Toast.makeText(this, "score=" + score, Toast.LENGTH_LONG).show();

            if (score>(questions.size()/2)){
                player=MediaPlayer.create(this,R.raw.clapping);
                player.start();
            }else{
                player=MediaPlayer.create(this,R.raw.sad);
                player.start();
            }
            Intent a=new Intent(this, PlayerActivity.class);
            a.putExtra("score",score);
            setResult(RESULT_OK,a);
            finish();

        }
        Collections.shuffle(items.subList(1,items.size()));
    }

}