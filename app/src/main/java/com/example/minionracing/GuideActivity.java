package com.example.minionracing;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {
    Button btnBack;

    TextView txtRule1;
    TextView txtRule2;
    TextView txtRule3;
    TextView txtRule4;
    TextView txtRule5;

    List<String> rules = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        Mapping();

        txtRule1.setText(rules.get(0));
        txtRule2.setText(rules.get(1));
        txtRule3.setText(rules.get(2));
        txtRule4.setText(rules.get(3));
        txtRule5.setText(rules.get(4));

        btnBack.setOnClickListener(v ->{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void Mapping() {
        btnBack = findViewById(R.id.btnBack);
        btnBack.setText("Back");
        txtRule1 = findViewById(R.id.txtRule1);
        txtRule2 = findViewById(R.id.txtRule2);
        txtRule3 = findViewById(R.id.txtRule3);
        txtRule4 = findViewById(R.id.txtRule4);
        txtRule5 = findViewById(R.id.txtRule5);
        rules.add("Bước 1: Bạn cần phải đăng nhập để có thể tham gia trò chơi.");
        rules.add("Bước 2: Hãy chọn minion mà bạn tin rằng nó mang về chiến thắng cho bạn bằng cách chọn check box phía trước nó.");
        rules.add("Bước 3: Nhập số tiền mà bạn muốn cược để cho minion. Hãy đảm bảo rằng bạn có đủ tiền để cược!");
        rules.add("Bước 4: Hãy đặt lòng tin, cổ vũ và chờ đợi chú minion của bạn chiến thắng!");
        rules.add("Bước 5: Cậu ta đã mang chiến thắng đến cho bạn. Thật tuyệt vời! Hãy reset và bắt đầu 1 ván cược mới nào!");
    }
}
