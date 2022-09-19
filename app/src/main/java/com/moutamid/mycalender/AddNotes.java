package com.moutamid.mycalender;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moutamid.mycalender.databinding.ActivityAddNotesBinding;

public class AddNotes extends AppCompatActivity {
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference(Constants.DiaryNotes);
    String Date;
    ActivityAddNotesBinding binding;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle b = getIntent().getExtras();
        firebaseAuth = FirebaseAuth.getInstance();
        // Initialize firebase user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        Date = b.getString("Date", "null");
        binding.textView2.setText("Add Note");
        binding.uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleNotes data = new SimpleNotes();
                data.id = root.push().getKey();
                data.note = binding.EtNote.getText().toString();
                data.date = Long.parseLong(Date);
                if (firebaseUser != null) {
                  data.uid=firebaseUser.getUid();
                }
                root.child(data.id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            binding.EtNote.setText("");
                            Toast.makeText(AddNotes.this, "Added Succesfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddNotes.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(AddNotes.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

}