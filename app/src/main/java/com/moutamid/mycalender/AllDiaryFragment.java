package com.moutamid.mycalender;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.mycalender.databinding.FragmentAllDiaryBinding;
import com.moutamid.mycalender.databinding.FragmentAllToDoBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AllDiaryFragment extends Fragment {
    List<notes> event_dates, dated_events;
    FragmentAllDiaryBinding binding;
    noteAdapter adapter;

    public AllDiaryFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentAllDiaryBinding.inflate(getLayoutInflater()) ;
        event_dates=new ArrayList<>();
        readCalendarEvent();
        getEvents();
        return binding.getRoot();

    }
    public void readCalendarEvent() {
        Cursor cursor = getActivity().getContentResolver().query(Uri.parse("content://com.android.calendar/events"),
                (new String[]{"_id", "title", "description", "dtstart"}), "deleted = ?", new String[]{"0"}, null);
        List<notes> gCalendar = new ArrayList<notes>();
        try {

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    notes googleCalendar = new notes();
                    // event_ID: ID of tabel Event
                    int event_ID = cursor.getInt(0);
                    googleCalendar.setcId(event_ID);
                    // title of Event
                    String title = cursor.getString(1);
                    googleCalendar.setNote(title);
                    String mOrganizer = cursor.getString(2);
                    //  googleCalendar.setOrganizer(mOrganizer);
                    // Date start of Event
                    String dtStart = cursor.getString(3);
                    googleCalendar.setDate(Long.parseLong(dtStart));

                    gCalendar.add(googleCalendar);

                    Log.d("CaledarM", googleCalendar.getNote() + "date=" + googleCalendar.getDate());

                }
                event_dates = gCalendar;
            }
        } catch (AssertionError ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getEvents() {

        binding.recAllDiary.setLayoutManager(
                new LinearLayoutManager(getActivity()));
        adapter = new noteAdapter(getActivity(),event_dates,true);
        // Connecting Adapter class with the Recycler view*/
        binding.recAllDiary.setAdapter(adapter);
        adapter.setOnItemClick(new noteAdapter.OnitemClickListener() {
            @Override
            public void onEditClick(int position) {
                ContentResolver cr = getActivity().getContentResolver();
                ContentValues values = new ContentValues();
                values.put(CalendarContract.Events.TITLE, "Kickboxing");
                Uri updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, event_dates.get(position).getcId());
                int rows = getActivity().getContentResolver().update(updateUri, values, null, null);
                Log.i("Calendar", "Rows updated: " + rows);
            }

            @Override
            public void onDeleteClick(int position) {
                Uri CALENDAR_URI = Uri.parse("content://com.android.calendar/events");
                Uri uri = ContentUris.withAppendedId(CALENDAR_URI, event_dates.get(position).getcId());
                getActivity().getContentResolver().delete(uri, null, null);
                Utils.toast(getActivity(),"Deleted");
                readCalendarEvent();
            }
        });
    }
}