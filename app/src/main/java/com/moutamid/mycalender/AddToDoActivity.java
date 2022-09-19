package com.moutamid.mycalender;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
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
import com.moutamid.mycalender.databinding.ActivityAddToBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddToDoActivity extends AppCompatActivity {
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference(Constants.ToDo);
    String Date;
    ActivityAddToBinding binding;
    FirebaseAuth firebaseAuth;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddToBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Bundle b = getIntent().getExtras();
        Date = b.getString("Date", "null");
        binding.textView6.setText(dateFormatMonth.format(new Date(Long.parseLong(Date))));
        firebaseAuth=FirebaseAuth.getInstance();
        binding.textView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddToDoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        final Calendar c1 = Calendar.getInstance();
                        c1.set(Calendar.YEAR,year);
                        c1.set(Calendar.MONTH,monthOfYear);
                        c1.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        binding.textView6.setText(dateFormatMonth.format(new Date(c1.getTimeInMillis())));
                        Date=c1.getTimeInMillis()+"";
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }

        });
        binding.uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                SimpleNotes data = new SimpleNotes();
                data.id = root.push().getKey();
                data.note = binding.EtNote.getText().toString();
                data.date = Long.parseLong(Date.trim());
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