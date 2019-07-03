package com.example.thirty;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //playGame();

    }

    public void playGame(View view){
        GameActivity.currentRound = 0;
        GameActivity.scoreList = new int[10];
        boolean[] usedSumTypes = new boolean[10];
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.USED_SUM_TYPES, usedSumTypes);
        startActivity(intent);
    }

    public void readInstructions(View view){
        Intent intent = new Intent(this, InstructionsActivity.class);
        startActivity(intent);
    }



}
