package com.example.kalkulator;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    private Context Ctx;
    private FirebaseFirestore db;

    private List<ProsesHitung> hitungList;
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView IDHide, Variabel1, Variabel2, Operator, Result, Smd;
        Button Hapus;
        RelativeLayout index_htg;
        MyViewHolder(View view) {
            super(view);
            Variabel1 = view.findViewById(R.id.Variabel1);
            Variabel2 = view.findViewById(R.id.Variabel2);
            Operator = view.findViewById(R.id.Operator);
            Result = view.findViewById(R.id.Result);
            Smd = view.findViewById(R.id.hasill);
            IDHide = view.findViewById(R.id.IDHide);
            index_htg = view.findViewById(R.id.index_hitung);
            IDHide.setVisibility(View.INVISIBLE);
            Hapus = view.findViewById(R.id.Hapus);
        }
    }

    public Adapter(Context ctx, List<ProsesHitung> hitungList) {
        Ctx = ctx;
        this.hitungList = hitungList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_history, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        db = FirebaseFirestore.getInstance();
        final ProsesHitung prosesHitung = hitungList.get(position);
        holder.Variabel1.setText(prosesHitung.getVariabel1());
        holder.Operator.setText(prosesHitung.getOperator());
        holder.Variabel2.setText(prosesHitung.getVariabel2());
        holder.Smd.setText("=");
        holder.Result.setText(prosesHitung.getResult());
        holder.IDHide.setText(prosesHitung.getID());
        holder.Hapus.setOnClickListener(v ->

                db.collection("data_hitung").document(prosesHitung.getID())
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Intent intent = new Intent(Ctx,MainActivity.class);
                            intent.putExtra("MsgDel", prosesHitung.getID());
                            Ctx.startActivity(intent);
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Intent intent = new Intent(Ctx,MainActivity.class);
                                intent.putExtra("MsgDel", "0");
                                Ctx.startActivity(intent);
                            }
                        }));

    }

    @Override
    public int getItemCount() {
        return hitungList.size();
    }
}
