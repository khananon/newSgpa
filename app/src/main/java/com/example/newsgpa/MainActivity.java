package com.example.newsgpa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    ArrayList<SubjectModel> arrSub = new ArrayList<>();
    RvAdapter adapter;
    ImageView Dp;
    TextView StudentName, collegeName, helloTextView;

    RecyclerView Rv;
    Button Calculatebtn,lgButton;
    ExtendedFloatingActionButton btnOpenDlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("My_setting", MODE_PRIVATE);
        String name = preferences.getString("name", "");
        String gender = preferences.getString("gender", "");
        String college = preferences.getString("college", "");

        Dp = findViewById(R.id.displayPic);
        StudentName = findViewById(R.id.StudentName);
        collegeName = findViewById(R.id.clgName);
        helloTextView = findViewById(R.id.helloTextView);
        StudentName.setText(name);
        collegeName.setText(college);
        lgButton=findViewById(R.id.lgbutton);
        lgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                finish();
                Toast.makeText(MainActivity.this, "Activity Logout!!", Toast.LENGTH_SHORT).show();
            }});
        // Set the profile image based on gender
        if (gender != null) {
            if (gender.equalsIgnoreCase("male")) {
                Dp.setImageResource(R.drawable.boy);
                helloTextView.setTextColor(getResources().getColor(R.color.Blue));
            } else if (gender.equalsIgnoreCase("female")) {
                Dp.setImageResource(R.drawable.girl);
                helloTextView.setTextColor(getResources().getColor(R.color.Pink));
            } else {
                // Set a default image if gender is not recognized
                Dp.setImageResource(R.drawable.boy);
                helloTextView.setTextColor(getResources().getColor(R.color.Blue));
            }
        } else {
            // Handle the case where gender is null (provide a default or show an error message)
            Dp.setImageResource(R.drawable.boy); // Setting a default image for null gender
        }

        Rv = findViewById(R.id.Rview);
        btnOpenDlg = findViewById(R.id.AddSubBtn);


        btnOpenDlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.add_update);
                EditText edName = dialog.findViewById(R.id.ubjectNameD);
                EditText edCredit = dialog.findViewById(R.id.CreditD);
                EditText edGrade = dialog.findViewById(R.id.GradeD);
                Button btnAction = dialog.findViewById(R.id.BtnD);
                btnAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String SubjectName = " ", GradeM = " ", CreditM = " ";
                        if (!edName.getText().toString().equals("")) {
                            SubjectName = edName.getText().toString();
                        } else {
                            Toast.makeText(MainActivity.this, "Please Enter Subject Name", Toast.LENGTH_SHORT).show();
                        }
                        if (!edCredit.getText().toString().equals("")) {
                            CreditM = edCredit.getText().toString();
                        } else {
                            Toast.makeText(MainActivity.this, "Please Enter Credit", Toast.LENGTH_SHORT).show();
                        }
                        if (!edGrade.getText().toString().equals("")) {
                            GradeM = edGrade.getText().toString();
                        } else {
                            Toast.makeText(MainActivity.this, "Please Enter Grade", Toast.LENGTH_SHORT).show();
                        }
                        arrSub.add(new SubjectModel(SubjectName, GradeM, CreditM));
                        adapter.notifyItemInserted(arrSub.size() - 1);
                        Rv.scrollToPosition(arrSub.size() - 1);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        Calculatebtn = findViewById(R.id.Calculatebutton);
        Calculatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double sgpa = calculateSGPA();
                TextView SGPA = findViewById(R.id.SGPA);
                SGPA.setText(new DecimalFormat("##.##").format(sgpa));
            }
        });

        Rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RvAdapter(this, arrSub);
        Rv.setAdapter(adapter);

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

        if (totalCredits == 0) {
            return 0.0;
        }

        return totalCreditPoints / totalCredits;
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