package com.moutamid.mycalender;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.mycalender.databinding.FragmentAllNotesBinding;

import java.util.ArrayList;

public class AllNotesFragment extends Fragment {
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference(Constants.DiaryNotes);
    NotesAdapterr NoteAdapter;
    FirebaseAuth firebaseAuth;
    FragmentAllNotesBinding binding;
    public AllNotesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        binding = FragmentAllNotesBinding.inflate(getLayoutInflater());
        getNotes();
        return binding.getRoot();
    }

    public void getNotes() {
        ArrayList<SimpleNotes> its = new ArrayList<>();
        Query myMostViewedPostsQuery = root.orderByChild("date");
        myMostViewedPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    SimpleNotes not = postSnapshot.getValue(SimpleNotes.class);

                    if (firebaseAuth.getUid().equals(not.getUid())) {
                        its.add(not);
                    }

                }
                binding.recAllNotess.setLayoutManager(new LinearLayoutManager(getActivity()));
                NoteAdapter = new NotesAdapterr(getActivity(), its,true);
                binding.recAllNotess.setAdapter(NoteAdapter);
                NoteAdapter.setOnItemClick(new NotesAdapterr.OnitemClickListener() {
                    @Override
                    public void onEditClick(int position) {
                        showBottomSheetDialog(its.get(position).getId(), 1);
                    }

                    @Override
                    public void onDeleteClick(int position) {
                        root.child(its.get(position).getId()).removeValue();
                        its.remove(position);
                        NoteAdapter.notifyDataSetChanged();
                    }
                });
                Log.d("ListNotesItmes", NoteAdapter.getItemCount() + "");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showBottomSheetDialog(String key, int type) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_edit);
        EditText text = bottomSheetDialog.findViewById(R.id.editText);
        Button add = bottomSheetDialog.findViewById(R.id.btnDone);

        TextView title = bottomSheetDialog.findViewById(R.id.tvEditDiary);
        bottomSheetDialog.show();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isEmpty(text)) {
                    Utils.toast(getActivity(), "please add note");
                } else {

                    root.child(key).child("note").setValue(text.getText().toString());
                    Utils.toast(getActivity(), "Updated Succesfully");
                    bottomSheetDialog.hide();
                }
            }
        });
        title.setText("Edit Note");


    }
}