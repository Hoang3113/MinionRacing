package com.example.minionracing;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    ImageButton btnGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnGuide = findViewById(R.id.btnGuide);

        btnGuide.setOnClickListener(v ->{
            Intent intent = new Intent(this, GuideActivity.class);
            startActivity(intent);
        });
    }
}
