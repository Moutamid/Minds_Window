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
import com.moutamid.mycalender.databinding.FragmentAllToDoBinding;
import java.util.ArrayList;
import java.util.List;
public class AllToDoFragment extends Fragment {
    List<notes> event_dates;
    FragmentAllToDoBinding binding;
    ToDoAdapter toDoAdapter;
    private DatabaseReference ToDoDb = FirebaseDatabase.getInstance().getReference(Constants.ToDo);
    FirebaseAuth firebaseAuth;
    public AllToDoFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        event_dates = new ArrayList<>();
        binding = FragmentAllToDoBinding.inflate(getLayoutInflater());
        firebaseAuth = FirebaseAuth.getInstance();
        getToDo();
        return binding.getRoot();
    }
    public void getToDo() {
        ArrayList<SimpleNotes> its = new ArrayList<>();
        Query myMostViewedPostsQuery = ToDoDb.orderByChild("date");
        myMostViewedPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                its.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    SimpleNotes not = postSnapshot.getValue(SimpleNotes.class);
                    Log.d("T0DoNode", not.getNote() + "haha");
                    if (firebaseAuth.getUid().equals(not.getUid())) {
                        its.add(not);
                    }
                }
                binding.recToTo.setLayoutManager(new LinearLayoutManager(getActivity()));
                toDoAdapter = new ToDoAdapter(getActivity(), its,true);
                binding.recToTo.setAdapter(toDoAdapter);
                toDoAdapter.setOnItemClick(new ToDoAdapter.OnitemClickListener() {
                    @Override
                    public void onEditClick(int position) {
                        showBottomSheetDialog(its.get(position).getId(),2);
                    }

                    @Override
                    public void onDeleteClick(int position) {
                        ToDoDb.child(its.get(position).getId()).removeValue();
                        its.remove(position);
                        toDoAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCheckClick(int position) {

                    }
                });
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
                    ToDoDb.child(key).child("note").setValue(text.getText().toString());
                    Utils.toast(getActivity(), "Updated Succesfully");
                    bottomSheetDialog.hide();
                }
            }
        });
        title.setText("Edit Note");


    }
}