package com.moutamid.mycalender;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.mycalender.databinding.ActivityAllNotesBinding;

import java.util.ArrayList;

public class AllNotesActivity extends AppCompatActivity {
    ActivityAllNotesBinding binding;
    NotesAdapterr adapter;
    ArrayList<SimpleNotes> it = new ArrayList<>();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference(Constants.SimpleNotes);
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth=FirebaseAuth.getInstance();

        // Initialize firebase user


        binding.cardAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AllNotesActivity.this, SIMPLENOTESADDACTIVITY.class);
                startActivity(i);
            }
        });
        getData();


    }

    public void getData() {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        it.clear();
        Query myMostViewedPostsQuery = root.orderByChild("date");

        myMostViewedPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                it.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    SimpleNotes not = postSnapshot.getValue(SimpleNotes.class);
                    if(firebaseUser!=null&&not.getUid()!=null)
                    {
                       if(not.getUid().equals(firebaseUser.getUid())){
                           it.add(not);
                       }

                    }
                }
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("datasss", it + "");
                        binding.recAllNotes.setLayoutManager(
                                new LinearLayoutManager(getApplicationContext()));
                        adapter = new NotesAdapterr(AllNotesActivity.this, it,true);
                        // Connecting Adapter class with the Recycler view*/
                        binding.recAllNotes.setAdapter(adapter);
                        adapter.setOnItemClick(new NotesAdapterr.OnitemClickListener() {
                            @Override
                            public void onEditClick(int position) {
                                showBottomSheetDialog(it.get(position).getId());
                            }

                            @Override
                            public void onDeleteClick(int position) {
                                root.child(it.get(position).getId()).removeValue();
                                it.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }, 1000);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void showBottomSheetDialog(String key) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AllNotesActivity.this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_edit);
        EditText text = bottomSheetDialog.findViewById(R.id.editText);
        Button add = bottomSheetDialog.findViewById(R.id.btnDone);

        TextView title = bottomSheetDialog.findViewById(R.id.tvEditDiary);
        bottomSheetDialog.show();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isEmpty(text)) {
                    Utils.toast(getApplicationContext(), "please add note");
                } else {
                    root.child(key).child("note").setValue(text.getText().toString());
                    Utils.toast(getApplicationContext(),"Updated Succesfully");
                    bottomSheetDialog.hide();
                }
            }
        });
        title.setText("Edit Note");


    }
}