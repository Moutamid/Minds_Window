package com.moutamid.mycalender;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.mycalender.databinding.FragmentAllDiaryBinding;
import com.moutamid.mycalender.databinding.FragmentAllToDoBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AllDiaryFragment extends Fragment {
    List<notes>  dated_events;
    FragmentAllDiaryBinding binding;
    noteAdapter adapter;
    FirebaseAuth firebaseAuth;
    private DatabaseReference calender = FirebaseDatabase.getInstance().getReference(Constants.CalenderNotes);

    public AllDiaryFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAllDiaryBinding.inflate(getLayoutInflater());
        firebaseAuth = FirebaseAuth.getInstance();
        dated_events = new ArrayList<>();
        readCalendarEvent1(getActivity());
        return binding.getRoot();

    }

    public void readCalendarEvent1(Context context) {
        ArrayList<notes> its = new ArrayList<>();
        Query myMostViewedPostsQuery = calender.orderByChild("date");

        myMostViewedPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                its.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    notes not = postSnapshot.getValue(notes.class);
                    if (firebaseAuth.getUid().equals(not.getuId())) {
                        its.add(not);
//                        Event ev1 = new Event(Color.rgb(217, 217, 217), not.getDate(), "Language Martyr's Day");
//                        binding.compactcalendarView.addEvent(ev1);
                    }
                }
                getEvents(its);
                Log.d("checkDta1", its + "");
             //   event_dates = its;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void readCalendarEvent() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        ArrayList<notes> its = new ArrayList<>();
        Query myMostViewedPostsQuery = calender.orderByChild("date");

        myMostViewedPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    notes not = postSnapshot.getValue(notes.class);
//                    firebaseUser.getUid().equals(not.getuId()))

//                        Event ev1 = new Event(Color.rgb(217, 217, 217), not.getDate(), "Language Martyr's Day");
//                        binding.compactcalendarView.addEvent(ev1);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        Cursor cursor = getActivity().getContentResolver().query(Uri.parse("content://com.android.calendar/events"),
//                (new String[]{"_id", "title", "description", "dtstart"}), "deleted = ?", new String[]{"0"}, null);
//        List<notes> gCalendar = new ArrayList<notes>();
//        try {
//
//            if (cursor.getCount() > 0) {
//                while (cursor.moveToNext()) {
//                    notes googleCalendar = new notes();
//                    // event_ID: ID of tabel Event
//                    int event_ID = cursor.getInt(0);
//                    googleCalendar.setcId(event_ID);
//                    // title of Event
//                    String title = cursor.getString(1);
//                    googleCalendar.setNote(cursor.getString(2));
//                    googleCalendar.setTitle(title);
//                    //  googleCalendar.setOrganizer(mOrganizer);
//                    // Date start of Event
//                    String dtStart = cursor.getString(3);
//                    googleCalendar.setDate(Long.parseLong(dtStart));
//
//                    gCalendar.add(googleCalendar);
//
//                    Log.d("CaledarM", googleCalendar.getNote() + "date=" + googleCalendar.getDate());
//
//                }
//                event_dates = gCalendar;
//            }
//        } catch (AssertionError ex) {
//            ex.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void getEvents(List<notes> list) {

        dated_events=list;
        Log.d("eventsDilary", list + "");
        binding.recAllDiary.setVisibility(View.VISIBLE);
        binding.recAllDiary.setLayoutManager(
                new LinearLayoutManager(getActivity()));
        adapter = new noteAdapter(getActivity(), list, true);
        // Connecting Adapter class with the Recycler view*/
        binding.recAllDiary.setAdapter(adapter);
        adapter.setOnItemClick(new noteAdapter.OnitemClickListener() {
            @Override
            public void onEditClick(int position) {
                 showBottomSheetDialog(dated_events.get(position).getId(), 3);
            }

            @Override
            public void onDeleteClick(int position) {
                calender.child(dated_events.get(position).getId()).removeValue();
                dated_events.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
//       Utils.toast(getActivity(),adapter.getItemCount()+"");
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
                        calender.child(key).child("note").setValue(text.getText().toString());

                    Utils.toast(getActivity(), "Updated Succesfully");
                    bottomSheetDialog.hide();
                }
            }
        });
        title.setText("Edit Diary");


    }
}