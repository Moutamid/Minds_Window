package com.moutamid.mycalender;

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

public class AddToDoActivity extends AppCompatActivity {
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference(Constants.ToDo);
    String Date;
    ActivityAddNotesBinding binding;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Bundle b = getIntent().getExtras();
        Date = b.getString("Date", "null");
        firebaseAuth=FirebaseAuth.getInstance();
        binding.uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                SimpleNotes data = new SimpleNotes();
                data.id = root.push().getKey();
                data.note = binding.EtNote.getText().toString();
                data.date = Long.parseLong(Date);
                data.status=false;
                if(firebaseUser!=null)
                {
                    data.uid=firebaseUser.getUid();

                }
                root.child(data.id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            binding.EtNote.setText("");
                            Toast.makeText(AddToDoActivity.this, "Added Succesfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddToDoActivity.this, "bad", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}