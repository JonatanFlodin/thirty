package com.example.thirty;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CalculateScoreActivity extends AppCompatActivity {

    /** container for all dice currently in use **/
    private Dice[] dice;
    /** the sum of the current combos **/
    private int comboSum;
    /** keeps track of used sum types in current game session **/
    private boolean[] usedSumTypes;

    private final static String COMBO_SUM = "comboSum";

    /**
     * Called on creation of this activity
     * @param savedInstanceState instance of state before destruction of activity, if available
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_score);

        // Get dice from last activity
        Intent intent = getIntent();
        int[] savedDice = intent.getIntArrayExtra(GameActivity.DICES);
        usedSumTypes = intent.getBooleanArrayExtra(GameActivity.USED_SUM_TYPES);

        // Check if there is a recovered state
        if(savedInstanceState != null) {
            comboSum  = savedInstanceState.getInt(COMBO_SUM);
            dice = (Dice[]) savedInstanceState.getParcelableArray(GameActivity.DICE_ARRAY);
        } else {
            // Reconstruct the dice from last activity.
            comboSum = 0;
            dice = new Dice[GameActivity.DICE_AMOUNT];
            for(int i = 0; i < savedDice.length; i++) {
                dice[i] = new Dice(GameActivity.FACE_AMOUNT);
                dice[i].setNumber(savedDice[i]);
                String buttonID = "dice" + (i + 1);
                dice[i].setID(buttonID);
            }
        }

        // Set die image accordingly
        for(int i = 0; i < dice.length; i++) {
            View v = findViewById(android.R.id.content);
            GameActivity.toggleDiceImage(v, dice[i]);
        }

        // The string representations of the different score methods.
        String[] scoreOptions = getResources().getStringArray(R.array.scoreChoices);

        // Create spinner to let user choose method.
        Spinner spinner = (Spinner) findViewById(R.id.scoretype_spinner);
        ArrayList<String> scoreOptionsList = new ArrayList<>();

        for(int i = 0; i < scoreOptions.length; i++) {
            if(!usedSumTypes[i]) {
                scoreOptionsList.add(scoreOptions[i]);
            }

        }

        // Create callbacks for the spinner
        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        chooseType(view);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // Implementing interface
                    }
                });

        // Populate spinner with remaining methods.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, scoreOptionsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    /**
     * Called before destruction of activity
     * @param savedInstanceState bundle to save data to
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putParcelableArray(GameActivity.DICE_ARRAY, dice);
        savedInstanceState.putInt(COMBO_SUM, comboSum);
        savedInstanceState.putBooleanArray(GameActivity.USED_SUM_TYPES, usedSumTypes);

    }

    /**
     * Called when restoring old instance state
     * @param savedInstanceState state to restore data from
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        comboSum = savedInstanceState.getInt(COMBO_SUM);
        dice = (Dice[]) savedInstanceState.getParcelableArray(GameActivity.DICE_ARRAY);
        usedSumTypes = savedInstanceState.getBooleanArray(GameActivity.USED_SUM_TYPES);
    }

    /**
     * Converts the string representation to the
     * * integer representation of the scoring methods.
     * @param text The text to convert to a integer representation.
     * @return The integer representation of a given method string.
     */
    public int getPositionFromText(String text) {
        // The string representations of the different score methods.
        Resources r = getResources();
        if(text.equals("Low")) {
            return 0;
        } else if(text.equals("Four")) {
            return 1;
        } else if(text.equals("Five")) {
            return 2;
        } else if(text.equals("Six")) {
            return 3;
        } else if(text.equals("Seven")) {
            return 4;
        } else if(text.equals("Eight")) {
            return 5;
        } else if(text.equals("Nine")) {
            return 6;
        } else if(text.equals("Ten")) {
            return 7;
        } else if(text.equals("Eleven")) {
            return 8;
        } else if(text.equals("Twelve")) {
            return 9;
        } else {
            // No options left
            return -1;
        }

    }

    /**
     * Checks the currently chosen dice combo and calculates the score
     * of that combo, or hints about any eventual errors in the combo.
     * @param view The view of the pressed button.
     */
    public void checkCombo(View view) {
        Spinner spinner = (Spinner)findViewById(R.id.scoretype_spinner);
        String chosenString = spinner.getSelectedItem().toString();
        int chosenType = getPositionFromText(chosenString) + 3;
        int sum = 0;

        String lowStr = "Low";
        // Special case for the Low method.
        if(chosenString.equals(lowStr)) {
            for(int i = 0; i < dice.length; i++) {
                if(dice[i].isSaved()) {
                    if(dice[i].getNumber() <= 3) {
                        sum += dice[i].getNumber();
                    } else {
                        Context context = getApplicationContext();
                        CharSequence text = "Only dice with values below 4 added to sum";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }
            }

            // Make used dice red and unclickable
            // to discern from the rest.
            for(int i = 0; i < dice.length; i++) {
                if(dice[i].isSaved()) {
                    dice[i].setSaved(false);
                    dice[i].setDisabled(true);
                    View v = findViewById(android.R.id.content);
                    ImageButton b = GameActivity.getButton(v, dice[i].getID());
                    b.setEnabled(false);
                    GameActivity.toggleDiceImage(v, dice[i]);
                }
            }
            // Update combo text
            comboSum += sum;
            TextView text2 = (TextView)findViewById(R.id.current_value_score);
            text2.setText(String.format(getResources().getString(R.string.combo_score), comboSum));

        } else {
            // Count sum of current combo.
            for(int i = 0; i < dice.length; i++) {
                if(dice[i].isSaved()) {
                    sum += dice[i].getNumber();
                }
            }
            if(sum == chosenType) {
                // Make used dice red and unclickable
                for(int i = 0; i < dice.length; i++) {
                    if(dice[i].isSaved()) {
                        dice[i].setSaved(false);
                        View v = findViewById(android.R.id.content);
                        ImageButton b = GameActivity.getButton(v, dice[i].getID());
                        b.setEnabled(false);
                        dice[i].setDisabled(true);
                        GameActivity.toggleDiceImage(v, dice[i]);
                    }
                }
                comboSum += sum;
                int currCombos = comboSum / chosenType;
                TextView text = (TextView)findViewById(R.id.combo_amount);
                text.setText(String.format(getResources().getString(R.string.combo_count), currCombos, chosenString.toLowerCase()));
                TextView text2 = (TextView)findViewById(R.id.current_value_score);
                text2.setText(String.format(getResources().getString(R.string.combo_score), comboSum));
            } else {
                // Show hints about errors in combo
                Context context = getApplicationContext();

                CharSequence text = "Incorrect combo, try again!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

        }

    }

    /**
     * Saves the result for the chosen method, and then redirects
     * to the dice choosing activity.
     * @param view the view of the clicked button
     */
    public void saveCombos(View view) {
        Spinner spinner = (Spinner)findViewById(R.id.scoretype_spinner);
        String chosenString = spinner.getSelectedItem().toString();
        int chosenType = getPositionFromText(chosenString);
        usedSumTypes[chosenType] = true;
        GameActivity.scoreList[chosenType] = comboSum;

        // Check if game is over
        if(GameActivity.currentRound == 10) {
            Intent intent = new Intent(this, ResultActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra(GameActivity.USED_SUM_TYPES, usedSumTypes);
            startActivity(intent);
        }

    }

    /**
     * Reacts to the user selecting a method in the spinner. It sets some text to
     * accomodate the choice made.
     * @param view the view of the clicked spinner item
     */
    public void chooseType(View view) {
        Spinner spinner = (Spinner)findViewById(R.id.scoretype_spinner);
        String chosenType = spinner.getSelectedItem().toString();
        TextView text = (TextView)findViewById(R.id.combo_amount);
        if(chosenType.equals("Low")) {
            text.setText(getResources().getString(R.string.select_low_combo));
        } else {
            int chosenInt = getPositionFromText(chosenType) + 3;
            int currCombos = comboSum / chosenInt;
            text.setText(String.format(getResources().getString(R.string.combo_count), currCombos, chosenType.toLowerCase()));
        }
        TextView text2 = (TextView)findViewById(R.id.current_value_score);
        text2.setText(String.format(getResources().getString(R.string.combo_score), comboSum));
    }

    /**
     * Toggles the save state of the clicked die and sets the
     * image according to current state
     * @param view The view of the clicked die
     */
    public void saveDice(View view) {
        switch(view.getId()) {
            case R.id.dice1:
                dice[0].toggleSaved();
                GameActivity.toggleDiceImage(view, dice[0]);
                break;
            case R.id.dice2:
                dice[1].toggleSaved();
                GameActivity.toggleDiceImage(view, dice[1]);
                break;
            case R.id.dice3:
                dice[2].toggleSaved();
                GameActivity.toggleDiceImage(view, dice[2]);
                break;
            case R.id.dice4:
                dice[3].toggleSaved();
                GameActivity.toggleDiceImage(view, dice[3]);
                break;
            case R.id.dice5:
                dice[4].toggleSaved();
                GameActivity.toggleDiceImage(view, dice[4]);
                break;
            case R.id.dice6:
                dice[5].toggleSaved();
                GameActivity.toggleDiceImage(view, dice[5]);
                break;
        }
    }

}