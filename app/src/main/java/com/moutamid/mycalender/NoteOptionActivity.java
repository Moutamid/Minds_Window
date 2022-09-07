package com.moutamid.mycalender;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.moutamid.mycalender.databinding.ActivityNoteOptionBinding;

public class NoteOptionActivity extends AppCompatActivity {
ActivityNoteOptionBinding binding;
String Date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNoteOptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Bundle b=getIntent().getExtras();
        Date=b.getString("Date","null");
        binding.btnNormalDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NoteOptionActivity.this, AddCalenderNote.class);
                i.putExtra("Date", Date);
                startActivity(i);
            }
        });
        binding.btnSimpleNotePad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NoteOptionActivity.this, AddNotes.class);
                i.putExtra("Date", Date);
                startActivity(i);
            }
        });
        binding.btnToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NoteOptionActivity.this, AddToDoActivity.class);
                i.putExtra("Date", Date);
                startActivity(i);
            }
        });
        binding.btnNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NoteOptionActivity.this, AllNotesActivity.class);
                i.putExtra("Date", Date);
                startActivity(i);
            }
        });
        binding.backPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NoteOptionActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        binding.btnAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NoteOptionActivity.this, AllData.class);
                startActivity(i);
                finish();
            }
        });

    }
}