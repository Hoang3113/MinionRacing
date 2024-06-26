package com.example.minionracing;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.minionracing.Dtos.User;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etUsername, etPassword;
    private TextView tvNotAccountYet;
    private Button btnLogin;
    private final String REQUIRE = "Require";
    public static List<User> userList;
    ImageButton btnGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvNotAccountYet = findViewById(R.id.tvNotAccountYet);
        btnLogin.setOnClickListener(this);
        tvNotAccountYet.setOnClickListener(this);
        userData();
      
        btnGuide = findViewById(R.id.btnGuide);
        btnGuide.setOnClickListener(v ->{
            Intent intent = new Intent(this, GuideActivity.class);
            startActivity(intent);
        });
    }

    private void userData() {
        if (userList == null) {
            userList = new ArrayList<>();
            userList.add(new User("user1", "password1", 10000));
            userList.add(new User("user2", "password2", 10000));
            userList.add(new User("user3", "password3", 10000));
        }
    }

    private boolean checkInput() {
        //username
        if (TextUtils.isEmpty(etUsername.getText().toString())) {
            etUsername.setError(REQUIRE);
            return false;
        }
        //password
        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            etPassword.setError(REQUIRE);
            return false;
        }
        return true;
    }

    private User validateLogin(String username, String password) {
        for (User user : userList) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    private void signIn() {
        if (!checkInput()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        User user = validateLogin(username, password);
        if (user != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void signUpForm() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin) {
            signIn();
        } else if (v.getId() == R.id.tvNotAccountYet) {
            signUpForm();
        }
    }
}
