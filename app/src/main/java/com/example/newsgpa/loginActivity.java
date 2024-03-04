package com.example.newsgpa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class loginActivity extends AppCompatActivity {
Button Loginbtn;
EditText Name,  ClgName;
RadioGroup Rg;
RadioButton Rb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Rg= findViewById(R.id.radioGroup);
        Name=findViewById(R.id.Namelogin);
        ClgName=findViewById(R.id.ClgNamelogin);
        Loginbtn=findViewById(R.id.buttonlogin);
        SharedPreferences preferences = getSharedPreferences("My_setting", MODE_PRIVATE);
        String name = preferences.getString("name", null);
        if (name != null) {
            startActivity(new Intent(loginActivity.this, MainActivity.class));
            finish(); // Finish the login activity to prevent going back to it
        }

        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = Name.getText().toString();
                String collage = ClgName.getText().toString();
                if (!name.isEmpty() && !collage.isEmpty()) {
                    Rb = findViewById(Rg.getCheckedRadioButtonId());
                    String gender = Rb.getText().toString();
                    Intent intent = new Intent(loginActivity.this, MainActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("gender", gender);
                    intent.putExtra("college", collage);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("name", Name.getText().toString());
                    editor.putString("gender", Rb.getText().toString());
                    editor.putString("college", ClgName.getText().toString());
                    editor.apply();

                    startActivity(intent);
                    finish(); // Finish the login activity to prevent going back to it
                } else {
                    Toast.makeText(loginActivity.this, "Please Enter Name and College ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }}