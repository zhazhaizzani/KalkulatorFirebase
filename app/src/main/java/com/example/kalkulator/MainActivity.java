package com.example.kalkulator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private static final String TAG = "MainActivity";
    private List<ProsesHitung> hitungList = new ArrayList<>();
    private Adapter mAdapter;

    EditText input1, input2;
    Button btnHitung;
    RadioGroup Group;
    TextView Hasil;
    android.widget.RadioButton RadioButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String MsgDel;
        MsgDel = intent.getStringExtra("MsgDel");
        if (MsgDel != null){
            if (MsgDel == "0" ){
                Toast.makeText(MainActivity.this, "Data Gagal Dihapus", Toast.LENGTH_SHORT).show();
                MsgDel = null;
            }else {
                Toast.makeText(MainActivity.this, "Data ID " + MsgDel + " Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                MsgDel = null;
            }
        }

        db = FirebaseFirestore.getInstance();
        RecyclerView recyclerView = findViewById(R.id.MyRecyclerview);
        mAdapter = new Adapter(this,hitungList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        DataHitung();

        input1 = findViewById(R.id.input1);
        input2 = findViewById(R.id.input2);
        btnHitung = findViewById(R.id.btnHitung);
        Hasil = findViewById(R.id.Hasil);
        Group = findViewById(R.id.radioGroup);

        btnHitung.setOnClickListener(v -> {
            Double Result;
            String Operand;
            int RadioId = Group.getCheckedRadioButtonId();
            RadioButton = findViewById(RadioId);
            Toast.makeText(MainActivity.this, "Memproses Data", Toast.LENGTH_SHORT).show();

            if (RadioButton.getText().equals("Bagi")) {
                Result = Double.parseDouble(input1.getText().toString()) / Double.parseDouble(input2.getText().toString());
                Operand = "/";
            } else if (RadioButton.getText().equals("Kurang")) {
                Result = Double.parseDouble(input1.getText().toString()) - Double.parseDouble(input2.getText().toString());
                Operand = "-";
            } else if (RadioButton.getText().equals("Kali")) {
                Result = Double.parseDouble(input1.getText().toString()) * Double.parseDouble(input2.getText().toString());
                Operand = "*";
            } else {
                Result = Double.parseDouble(input1.getText().toString()) + Double.parseDouble(input2.getText().toString());
                Operand = "+";
            }

            Map<String, Object> hitung = new HashMap<>();
            hitung.put("Variabel1", input1.getText().toString());
            hitung.put("Variabel2", input2.getText().toString());
            hitung.put("Operator", Operand);
            hitung.put("Result", Result.toString());

            Hasil.setText(" " + Result);

            db.collection("data_hitung")
                    .add(hitung)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(getBaseContext(),
                                "Input Data Berhasil",
                                Toast.LENGTH_SHORT).show();
                        input1.setText("0");
                        input2.setText("0");

                        DataHitung();

                    })
                    .addOnFailureListener(e ->
                        Log.e("firebase-error", e.getMessage()));

            DataHitung();

        });
    }

    private void DataHitung() {
        db.collection("data_hitung")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        hitungList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            ProsesHitung prosesHitung = new ProsesHitung(document.getId(), document.getData().get("Variabel1").toString(), document.getData().get("Variabel2").toString(), document.getData().get("Operator").toString(), document.getData().get("Result").toString());
                            hitungList.add(prosesHitung);
                            mAdapter.notifyDataSetChanged();
                        }

                    }else {
                        Log.w(TAG, "Error", task.getException());

                    }
                });
    }


}