package com.example.thirty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    /**
     * Called on creation of this activity
     * @param savedInstanceState instance of state before destruction of activity, if available
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
    }}
        // Grab the result list to fill with result values
       /* ListView resultListView = (ListView)findViewById(R.id.result_list);

        // Contains all the different score methods in the game
        String[] scoreOptions = getResources().getStringArray(R.array.scoreChoices);

        // Fill each list item with the individual results
        int finalSum = 0;
        ArrayList<String> resultList = new ArrayList<>();
        for(int i = 0; i < GameActivity.scoreList.length; i++) {
            // Calculate total sum
            finalSum += GameActivity.scoreList[i];
            String resultListItemStr = String.format(getResources().getString(R.string.result_item),
                    scoreOptions[i], GameActivity.scoreList[i]);
            resultList.add(resultListItemStr);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, resultList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        resultListView.setAdapter(adapter);

        // Update text that represents final sum
        TextView finalSumView = (TextView) findViewById(R.id.final_sum);
        finalSumView.setText("Final sum: " + finalSum);
    }

    /**
     * Callback function that sends the user to the main menu
     * when user click on the main menu button
     * @param view view of clicked button
     */
    /*public void backToMainMenu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }*/
