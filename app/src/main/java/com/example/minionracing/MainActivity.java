package com.example.minionracing;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.minionracing.Dtos.User;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnLogout, btnToggleImage;
    private static final int INIT_AMOUNT = 10000;
    private static final int WIN_SCORE = 100;
    private TextView tvCurrentAmount, resultNotify;
    private SeekBar sbPlayer1, sbPlayer2, sbPlayer3;
    private Button btnStart, btnReset;
    private EditText etPayNumber1, etPayNumber2, etPayNumber3;
    private int totalWin = 0, totalLose = 0;
    private Handler handler;
    private boolean isImage1 = true; // Trạng thái của hình ảnh
    private MediaPlayer mediaPlayer;
    private User user;
    private List<User> userList;
    private boolean isSoundOn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        //logout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);

        // Toggle Image button
        btnToggleImage = findViewById(R.id.btnToggleImage);
//        btnToggleImage.setOnClickListener(v -> toggleImage());
        btnToggleImage.setOnClickListener(v -> muteSound());
        //bet race
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

        //get user from login
        user = getIntent().getParcelableExtra("user");
        userList = LoginActivity.userList;

//        tvCurrentAmount.setText(String.valueOf(INIT_AMOUNT));
        tvCurrentAmount.setText(String.valueOf(user.getMoney()));

        sbPlayer1.setProgress(0);
        sbPlayer2.setProgress(0);
        sbPlayer3.setProgress(0);
        mediaPlayer = MediaPlayer.create(this, R.raw.music); // Khởi tạo mediaPlayer

        btnStart.setOnClickListener(v -> startRace(mediaPlayer, MainActivity.this));
        btnReset.setOnClickListener(v -> resetPlayers());

        handler = new Handler();
    }
  
    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void logout() {
        System.out.printf(user + "");
        for (User userL:
             userList) {
            if(userL.getUsername().equals(user.getUsername())){
                userL.setMoney(user.getMoney());
                break;
            }
        }
        mediaPlayer.pause();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogout) {
            showLogoutConfirmationDialog();
        }
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
        if(!mediaPlayer.isPlaying() && !isSoundOn){
            mediaPlayer.start();
        }
        disableButtons();

        Random random = new Random();
        int[] progress = new int[3];
        int[] playerScores = new int[3];
        int[] betAmounts = new int[]{betAmount1, betAmount2, betAmount3};
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 3; i++) {
                    if (playerScores[i] >= WIN_SCORE) {
                        int currentAmount = Integer.parseInt(tvCurrentAmount.getText().toString());
                        int winningAmount = calculateWinningAmount(betAmounts[i]);
                        currentAmount += winningAmount - totalBetAmount;
                        user.setMoney(currentAmount);
                        tvCurrentAmount.setText(String.valueOf(currentAmount));
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
        }, 100);
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
    private int calculateWinningAmount(int betAmount) {
        return betAmount * 3 - (betAmount/100 * 10);
    }


    private void muteSound() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                isSoundOn = true;
                mediaPlayer.pause();
                btnToggleImage.setBackgroundResource(R.drawable.volume); // Đổi hình ảnh khi tắt âm thanh
            } else {
                isSoundOn = false;
                mediaPlayer.start();
                btnToggleImage.setBackgroundResource(R.drawable.sound); // Đổi hình ảnh khi bật âm thanh
            }
        }
    }
}