package com.example.firstapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    private DatabaseHelper dbHelper; // Для работы с базой данных

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        dbHelper = new DatabaseHelper(this); // Инициализация DatabaseHelper

        EditText emailField = findViewById(R.id.email);
        EditText passwordField = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                // Проверка учетных данных в базе данных
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
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM Users WHERE login = ? AND password = ?",
                new String[]{email, password}
        );

        boolean isValid = cursor.getCount() > 0; // Если нашлась запись, то данные корректны
        cursor.close();
        return isValid;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close(); // Закрываем базу данных при уничтожении активности
    }
}
