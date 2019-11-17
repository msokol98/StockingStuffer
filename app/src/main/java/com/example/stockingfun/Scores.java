package com.example.stockingfun;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Scores extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("myTag", "Created activity");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        //Set up end text
        TextView finalScore = (TextView) findViewById(R.id.finalScore);
        finalScore.setVisibility(View.INVISIBLE);
        TextView playAgain = (TextView) findViewById(R.id.playAgain);
        playAgain.setVisibility(View.INVISIBLE);

        //displays final score
        finalScore = (TextView) findViewById(R.id.finalScore);
        GameFunctionalityImpl g = new GameFunctionalityImpl();
        String message = "Your score is " + g.getScore() + ".";
        finalScore.setText(message);
        finalScore.setTextSize(30);
        finalScore.setTextColor(Color.WHITE);
        finalScore.setElevation(5000);
        finalScore.setVisibility(View.VISIBLE);

        //tells user they can replay by tapping
        playAgain = (TextView) findViewById(R.id.playAgain);
        playAgain.setText("Tap to play again!");
        playAgain.setTextSize(30);
        playAgain.setTextColor(Color.WHITE);
        playAgain.setElevation(5000);
        playAgain.setVisibility(View.VISIBLE);


    }
    public void reset(View v) {
        EndHandler.restartGame();
        Intent i = new Intent(this, theGame.class);
        GameFunctionalityImpl.resetGame();
        this.startActivity(i);
        this.finish();
    }
}
