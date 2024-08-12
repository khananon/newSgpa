package com.example.newsgpa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class loginActivity extends AppCompatActivity {

    private EditText nameEditText, collegeEditText;
    private RadioGroup genderGroup;
    private Button submitDetailsBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        nameEditText = findViewById(R.id.Namelogin);
        collegeEditText = findViewById(R.id.ClgNamelogin);
        genderGroup = findViewById(R.id.radioGroup);
        submitDetailsBtn = findViewById(R.id.buttonlogin);

        submitDetailsBtn.setOnClickListener(view -> {
            String name = nameEditText.getText().toString();
            String college = collegeEditText.getText().toString();
            RadioButton selectedGender = findViewById(genderGroup.getCheckedRadioButtonId());
            String gender = selectedGender != null ? selectedGender.getText().toString() : null;

            if (!name.isEmpty() && !college.isEmpty() && gender != null) {
                saveUserDetailsToFirestore(name, gender, college);
            } else {
                Toast.makeText(loginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserDetailsToFirestore(String name, String gender, String college) {
        String userId = mAuth.getCurrentUser().getUid();

        // Create a user object with the provided details
        User user = new User(name, gender, college);

        // Save user details to Firestore
        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    startActivity(new Intent(loginActivity.this, MainActivity.class));
                    finish(); // Finish the login activity to prevent going back to it
                })
                .addOnFailureListener(e -> Toast.makeText(loginActivity.this, "Failed to save user details", Toast.LENGTH_SHORT).show());
    }

    private static class User {
        private String name;
        private String gender;
        private String college;

        // Default constructor required for calls to DataSnapshot.getValue(User.class)
        public User() {
        }

        public User(String name, String gender, String college) {
            this.name = name;
            this.gender = gender;
            this.college = college;
        }

        // Getters
        public String getName() {
            return name;
        }

        public String getGender() {
            return gender;
        }

        public String getCollege() {
            return college;
        }

        // Setters
        public void setName(String name) {
            this.name = name;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public void setCollege(String college) {
            this.college = college;
        }
    }}
