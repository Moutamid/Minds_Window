package com.example.mycalender;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mycalender.databinding.ActivityAddNotesBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SIMPLENOTESADDACTIVITY extends AppCompatActivity {
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("SimpleNotes");
    String Date;
    ActivityAddNotesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleNotes data = new SimpleNotes();
                data.id = root.push().getKey();
                data.note = binding.EtNote.getText().toString();
                data.date = System.currentTimeMillis();
                root.child(data.id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SIMPLENOTESADDACTIVITY.this, "Added Succesfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SIMPLENOTESADDACTIVITY.this, "bad", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}