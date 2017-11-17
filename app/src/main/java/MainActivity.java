package com.example.lenovo.scarnesdice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
//import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {
    Button roll, hold, reset;
    ImageView diceImageView;
    TextView scoreView;
    int userTurnScore = 0, userOverallScore = 0, compTurnScore = 0, compOverallScore = 0;
    boolean userTurn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v("Lifecycle", "is onCreate");

        hold = (Button)findViewById(R.id.hold);
        reset = (Button)findViewById(R.id.reset);
        roll = (Button)findViewById(R.id.roll);
        diceImageView  = (ImageView)findViewById(R.id.ImageView_dice);
        scoreView = (TextView)findViewById(R.id.TextView_score);

        hold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Hold", "Pressed hold");

                userOverallScore += userTurnScore;
                userTurnScore = 0;
                updateScore();
                //computer's turn
                switchTurn();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Reset", "Resetting");
                resetScore();
                updateScore();
            }
        });

        roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Roll", "Rolling");
                int score = RollDice();
                hold.setEnabled(true);

                if(score == 1) {
                    userTurnScore = 0;
                    //userTurn = false;
                    updateScore();
                    //computer's turn
                    switchTurn();
                } else {
                    userTurnScore += score;
                    updateScore();
                }
            }
        });

        resetScore();       //start the game
    }

    private void resetScore() {
        userTurnScore = 0;
        userOverallScore = 0;
        compOverallScore = 0;
        compTurnScore = 0;

        Random random = new Random(1);
        userTurn = random.nextBoolean();

        if(userTurn) {
            roll.setEnabled(true);
            hold.setEnabled(true);
        } else {
            roll.setEnabled(false);
            hold.setEnabled(false);
        }
        //hold.setEnabled(false);
        updateScore();
    }


    private int RollDice() {
        Random random = new Random();
        int rollValue = random.nextInt(6) + 1;

        int[] diceFaces = {
                R.drawable.dice1,
                R.drawable.dice2,
                R.drawable.dice3,
                R.drawable.dice4,
                R.drawable.dice5,
                R.drawable.dice6
        };
        diceImageView.setImageResource(diceFaces[rollValue - 1]);
        return rollValue;
    }

    private void updateScore() {
        String scoreText = "Your score: " + userOverallScore + " Computer score: " + compOverallScore;

        if(userTurn) {
            if((userOverallScore + userTurnScore) >= 100) {
                String winner = "\nYour score: " + String.valueOf(userTurnScore + userOverallScore) + "You win!\n";
                scoreText += winner;
                scoreView.setText(scoreText);
                resetScore();
            } else {
                roll.setEnabled(true);
                scoreText += "\nYour score: " + userTurnScore;
            }
        } else {
            if(compOverallScore + compTurnScore >= 100) {
                String winner = "\nComputer's score: " + String.valueOf(compTurnScore + compOverallScore) + "Computer win!\n";
                scoreText += winner;
                scoreView.setText(scoreText);
                resetScore();
            } else {
                roll.setEnabled(false);
                hold.setEnabled(false);
                scoreText += "\nComputer score: " + compTurnScore;
            }
        }

        scoreView.setText(scoreText);
    }

    private void ComputerTurn() {
        updateScore();
        //Roll the dice till we get a 1 or decide to hold
        hold.setEnabled(false);
        roll.setEnabled(false);
        final Handler handler = new Handler();
        Runnable computerRoll = new Runnable() {
            public void run() {
                int score = RollDice();

                if(score == 1) {
                    compTurnScore = 0;
                    switchTurn();
                } else if(compTurnScore > 20) {
                    compOverallScore += compTurnScore;
                    compTurnScore = 0;
                    userTurn = true;
                    updateScore();
                } else {
                    userTurn = false;
                    compTurnScore += score;
                }
                updateScore();

                if(!userTurn)
                    handler.postDelayed(this, 1000);
            }

        };
        handler.postDelayed(computerRoll, 1000);
    }

    private void switchTurn() {
        if(userTurn)
            scoreView.setText("Computer's turn\n");
        else
            scoreView.setText("Your turn\n");

        userTurn = !userTurn;

        if(userTurn) {
            roll.setEnabled(true);
            hold.setEnabled(true);
        } else {
            ComputerTurn();
        }
    }
/*
    @Override
    protected void onStart() {
        super.onStart();
        Log.v("Lifecycle", "is onStart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("Lifecycle", "is onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("Lifecycle", "is onPause");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.v("Lifecycle", "is onPostResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("Lifecycle", "is onStop");
    }*/
}

