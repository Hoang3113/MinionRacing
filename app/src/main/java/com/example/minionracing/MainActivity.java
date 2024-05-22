package com.example.minionracing;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final int INIT_AMOUNT = 10000;
    private static final int WIN_SCORE = 100;
    private TextView tvCurrentAmount, resultNotify;
    private SeekBar sbPlayer1, sbPlayer2, sbPlayer3;
    private Button btnStart, btnReset;
    private EditText etPayNumber1, etPayNumber2, etPayNumber3;
    private int totalWin = 0, totalLose = 0;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCurrentAmount = findViewById(R.id.tvCurrentAmount);
        resultNotify = findViewById(R.id.result);
        sbPlayer1 = findViewById(R.id.seekbarPlayer1);
        sbPlayer2 = findViewById(R.id.seekbarPlayer2);
        sbPlayer3 = findViewById(R.id.seekbarPlayer3);
        btnStart = findViewById(R.id.btnStart);
        btnReset = findViewById(R.id.btnReset);
        etPayNumber1 = findViewById(R.id.etPayNumberPlayer1);
        etPayNumber2 = findViewById(R.id.etPayNumberPlayer2);
        etPayNumber3 = findViewById(R.id.etPayNumberPlayer3);

        tvCurrentAmount.setText(String.valueOf(INIT_AMOUNT));
        sbPlayer1.setEnabled(false);
        sbPlayer2.setEnabled(false);
        sbPlayer3.setEnabled(false);

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.music);

        btnStart.setOnClickListener(v -> startRace(mediaPlayer, MainActivity.this));
        btnReset.setOnClickListener(v -> resetPlayers());

        handler = new Handler();
    }

    private void startRace(MediaPlayer mediaPlayer, Context context) {
        if (etPayNumber1.getText().toString().isEmpty() && etPayNumber2.getText().toString().isEmpty() && etPayNumber3.getText().toString().isEmpty()) {
            Toast.makeText(this, "You must fill bet amount", Toast.LENGTH_SHORT).show();
            return;
        }

        int currentAmount = Integer.parseInt(tvCurrentAmount.getText().toString());
        int betAmount1 = etPayNumber1.getText().toString().isEmpty() ? 0 : Integer.parseInt(etPayNumber1.getText().toString());
        int betAmount2 = etPayNumber2.getText().toString().isEmpty() ? 0 : Integer.parseInt(etPayNumber2.getText().toString());
        int betAmount3 = etPayNumber3.getText().toString().isEmpty() ? 0 : Integer.parseInt(etPayNumber3.getText().toString());

        if (betAmount1 == 0 && betAmount2 == 0 && betAmount3 == 0) {
            Toast.makeText(context, "You must fill bet amount", Toast.LENGTH_SHORT).show();
            return;
        }

        int totalBetAmount = betAmount1 + betAmount2 + betAmount3;
        if (totalBetAmount > currentAmount) {
            Toast.makeText(context, "Cannot bet larger than your balance." +
                    "\nYou have: " + currentAmount +
                    "\nYou bet: " + totalBetAmount, Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentAmount <= 0) {
            Toast.makeText(context, "Your balance is insufficient. You will be given 10000 again. \n+ 10000", Toast.LENGTH_LONG).show();
            tvCurrentAmount.setText(String.valueOf(INIT_AMOUNT));
            return;
        }

        resultNotify.setVisibility(View.VISIBLE);
        mediaPlayer.start();
        disableButtons();

        Random random = new Random();
        int[] progress = new int[3];
        int[] playerScores = new int[3];

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 3; i++) {
                    if (playerScores[i] >= WIN_SCORE) {
                        resultNotify.setText("Player " + (i + 1) + " wins!");
                        enableButtons();
                        return;
                    }
                    progress[i] = random.nextInt(10) + 1;
                    playerScores[i] += progress[i];
                    animateSeekBar(i, playerScores[i]);
                }
                handler.postDelayed(this, 600);
            }
        }, 600);
    }

    private void resetPlayers() {
        sbPlayer1.setProgress(0);
        sbPlayer2.setProgress(0);
        sbPlayer3.setProgress(0);
        etPayNumber1.setText("");
        etPayNumber2.setText("");
        etPayNumber3.setText("");
        resultNotify.setText("Result...");
        resultNotify.setVisibility(View.INVISIBLE);
        handler.removeCallbacksAndMessages(null);
    }

    private void disableButtons() {
        btnStart.setEnabled(false);
        btnReset.setEnabled(false);
    }

    private void enableButtons() {
        btnStart.setEnabled(true);
        btnReset.setEnabled(true);
    }

    private void animateSeekBar(int player, int progress) {
        SeekBar seekBar;
        switch (player) {
            case 0:
                seekBar = sbPlayer1;
                break;
            case 1:
                seekBar = sbPlayer2;
                break;
            case 2:
                seekBar = sbPlayer3;
                break;
            default:
                return;
        }
        seekBar.setProgress(progress);
    }
}