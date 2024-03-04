package com.example.newsgpa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheet extends BottomSheetDialogFragment {
    public BottomSheet(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.bottomdialog,container,false);
        Button btnAdd= view.findViewById(R.id.AddBtn);
        EditText subNameD= view.findViewById(R.id.SubD);
        EditText GradeD=view.findViewById(R.id.GradeD);
        EditText CreditD=view.findViewById(R.id.CreditD);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
