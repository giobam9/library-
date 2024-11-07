package com.example.firstapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {

    // Массив с корректными данными для входа
    String[] validEmails = {"niurchenko@sfedu.ru"};
    String[] validPasswords = {"123"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        EditText emailField = findViewById(R.id.email);
        EditText passwordField = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                boolean isValid = checkCredentials(email, password);
                if (isValid) {
                    // Если корректно, переходим на следующую активность
                    Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                    startActivity(intent);
                } else {
                    // Если некорректно, меняем цвет текста полей на красный
                    emailField.setTextColor(Color.RED);
                    passwordField.setTextColor(Color.RED);
                    Toast.makeText(MainActivity2.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Метод для проверки email и пароля
    private boolean checkCredentials(String email, String password) {
        for (int i = 0; i < validEmails.length; i++) {
            if (validEmails[i].equals(email) && validPasswords[i].equals(password)) {
                return true;
            }
        }
        return false;
    }
}

