package com.example.newsgpa;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.Viewholder> {
    Context context;
    ArrayList<SubjectModel>arrSub;

    public RvAdapter(Context context, ArrayList<SubjectModel> arrSub){
        this.context=context;
        this.arrSub=arrSub;

    }
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=  LayoutInflater.from(context).inflate(R.layout.subject_row,parent,false);
        Viewholder viewholder=new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.txtSName.setText(arrSub.get(position).SubName);
        holder.txtGrade.setText(arrSub.get(position).Grade);
        holder.txtCredit.setText(arrSub.get(position).Credit);
        holder.llrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog= new Dialog(context);
                dialog.setContentView(R.layout.add_update);
                EditText edName= dialog.findViewById(R.id.ubjectNameD);
                EditText edCredit= dialog.findViewById(R.id.CreditD);
                EditText edGrade= dialog.findViewById(R.id.GradeD);
                Button btnAction=dialog.findViewById(R.id.BtnD);
                TextView txtTitle=dialog.findViewById(R.id.TitleD);
                txtTitle.setText("Update Subject");
                btnAction.setText("Update");
                edName.setText(arrSub.get(position).SubName);
                edCredit.setText(arrSub.get(position).Credit);
                edGrade.setText(arrSub.get(position).Grade);
                btnAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String SubjectName1=" ",Grade=" ",Credit=" ";
                        if(!edName.getText().toString().equals("")) {
                            SubjectName1 = edName.getText().toString();
                        }else{
                            Toast.makeText(context, "Please Enter Subject Name", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(!edCredit.getText().toString().equals("")) {
                            Credit = edCredit.getText().toString();
                        }else{
                            Toast.makeText(context, "Please Enter Credit", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(!edGrade.getText().toString().equals("")) {
                            Grade = edGrade.getText().toString();
                        }else{
                            Toast.makeText(context, "Please Enter Grade", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        arrSub.set(position,new SubjectModel(SubjectName1,Grade,Credit));
                        notifyItemChanged(position);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        holder.llrow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context)
                        .setTitle("Delete Subject")
                        .setMessage("Are you sure")
                        .setIcon(R.drawable.baseline_delete_24)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                arrSub.remove(position);
                                notifyItemRemoved(position);
                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.show();
                return true;
            }
        });
        holder.setAnimation(holder.itemView,position);
    }

    @Override
    public int getItemCount() {

        return arrSub.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView txtSName, txtGrade, txtCredit;
        LinearLayout llrow;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            txtSName = itemView.findViewById(R.id.SNametxt);
            txtCredit = itemView.findViewById(R.id.Credittxt);
            txtGrade = itemView.findViewById(R.id.Gradetxt);
            llrow = itemView.findViewById(R.id.llrow);
        }

        private void setAnimation(View viewAnimation, int position) {


            Animation slidein = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewAnimation.startAnimation(slidein);
        }
    }}

