package com.example.newsgpa;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<SubjectModel> arrSub = new ArrayList<>();
    RvAdapter adapter;
    ImageView Dp;
    TextView StudentName, collegeName, helloTextView;
    RecyclerView Rv;
    Button Calculatebtn, lgButton;
    ExtendedFloatingActionButton btnOpenDlg;

    private FirebaseAuth mAuth;
    private  FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Dp = findViewById(R.id.displayPic);
        StudentName = findViewById(R.id.StudentName);
        collegeName = findViewById(R.id.clgName);
        helloTextView = findViewById(R.id.helloTextView);
         TextView backgroundtext= findViewById(R.id.textView3);

        lgButton = findViewById(R.id.lgbutton);
        lgButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, googleAuth.class));
            finish();
            Toast.makeText(MainActivity.this, "Logged out!", Toast.LENGTH_SHORT).show();
        });

        fetchUserData();

        Rv = findViewById(R.id.Rview);
        btnOpenDlg = findViewById(R.id.AddSubBtn);

        btnOpenDlg.setOnClickListener(view -> {
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.add_update);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

               backgroundtext.setVisibility(View.INVISIBLE);

            EditText edName = dialog.findViewById(R.id.ubjectNameD);
            EditText edCredit = dialog.findViewById(R.id.CreditD);
            EditText edGrade = dialog.findViewById(R.id.GradeD);
            Button btnAction = dialog.findViewById(R.id.BtnD);

            btnAction.setOnClickListener(view1 -> {
                String subjectName = edName.getText().toString().trim();
                String creditM = edCredit.getText().toString().trim();
                String gradeM = edGrade.getText().toString().trim();

                if (subjectName.isEmpty()) {
                    edName.setError("Please Enter Subject Name");
                    return;
                }
                if (creditM.isEmpty()) {
                    edCredit.setError("Please Enter Credit");
                    return;
                }
                if (gradeM.isEmpty()) {
                    edGrade.setError("Please Enter Grade");
                    return;
                }

                try {
                    double credit = Double.parseDouble(creditM);
                    if (credit <= 0) {
                        edCredit.setError("Credit must be positive");
                        return;
                    }
                } catch (NumberFormatException e) {
                    edCredit.setError("Invalid credit value");
                    return;
                }

                arrSub.add(new SubjectModel(subjectName, gradeM, creditM));
                adapter.notifyItemInserted(arrSub.size() - 1);
                Rv.scrollToPosition(arrSub.size() - 1);
                dialog.dismiss();
                Calculatebtn.setVisibility(View.VISIBLE);
            });
            dialog.show();
        });

        Calculatebtn = findViewById(R.id.Calculatebutton);
        Calculatebtn.setOnClickListener(view -> {
            double sgpa = calculateSGPA();
            TextView SGPA = findViewById(R.id.SGPA);
            SGPA.setText(new DecimalFormat("##.##").format(sgpa));
        });

        Rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RvAdapter(this, arrSub);
        Rv.setAdapter(adapter);
    }

    private void fetchUserData() {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String name = document.getString("name");
                            String college = document.getString("college");
                            String gender = document.getString("gender");

                            StudentName.setText(name);
                            collegeName.setText(college);

                            // Set the profile image based on gender
                            if ("male".equalsIgnoreCase(gender)) {
                                Dp.setImageResource(R.drawable.boy);
                                helloTextView.setTextColor(getResources().getColor(R.color.Blue));
                            } else if ("female".equalsIgnoreCase(gender)) {
                                Dp.setImageResource(R.drawable.girl);
                                helloTextView.setTextColor(getResources().getColor(R.color.Pink));
                            } else {
                                Dp.setImageResource(R.drawable.boy);
                                helloTextView.setTextColor(getResources().getColor(R.color.Blue));
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "User data not found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Error fetching user data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private double calculateSGPA() {
        double totalCreditPoints = 0.0;
        double totalCredits = 0.0;

        for (SubjectModel subject : arrSub) {
            double credit = Double.parseDouble(subject.Credit);
            double grade = convertGradeToNumeric(subject.Grade);

            totalCreditPoints += credit * grade;
            totalCredits += credit;
        }

        return totalCredits == 0 ? 0.0 : totalCreditPoints / totalCredits;
    }

    private double convertGradeToNumeric(String grade) {
        switch (grade) {
            case "A+":
                return 10.0;
            case "A":
                return 9.0;
            case "B+":
                return 8.0;
            case "B":
                return 7.0;
            case "C+":
                return 6.0;
            case "C":
                return 5.0;
            case "D":
                return 4.0;
            default:
                return 0.0;
        }
    }
}
