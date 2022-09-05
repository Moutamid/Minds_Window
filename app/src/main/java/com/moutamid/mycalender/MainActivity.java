package com.moutamid.mycalender;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference(Constants.DiaryNotes);
    private DatabaseReference ToDoDb = FirebaseDatabase.getInstance().getReference(Constants.ToDo);
    CompactCalendarView calendarView;
    TextView showMonth;
    int status;
    String dates;
    Button btnNotes, btnDiary, btnToDo;
    List<notes> event_dates, dated_events;
    private RecyclerView recyclerView, recNoting, recToTo;
    noteAdapter adapter;
    FirebaseAuth firebaseAuth;
    ToDoAdapter toDoAdapter;
    GoogleSignInClient googleSignInClient;
    NotesAdapterr NoteAdapter;// Create Object of the Adapter class
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Initialize();
        onClicks();

    }

    public void getToDo(Long data) {
        ArrayList<SimpleNotes> its = new ArrayList<>();
        Query myMostViewedPostsQuery = ToDoDb.orderByChild("date").equalTo(data);
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
                recToTo.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                toDoAdapter = new ToDoAdapter(getApplicationContext(), its);
                recToTo.setAdapter(toDoAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getNotes(Long data) {
        ArrayList<SimpleNotes> its = new ArrayList<>();
        Query myMostViewedPostsQuery = root.orderByChild("date").equalTo(data);
        myMostViewedPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    SimpleNotes not = postSnapshot.getValue(SimpleNotes.class);

                    if (firebaseAuth.getUid().equals(not.getUid())) {
                        its.add(not);
                    }

                }
                recNoting.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                NoteAdapter = new NotesAdapterr(getApplicationContext(), its);
                recNoting.setAdapter(NoteAdapter);
                Log.d("ListNotesItmes", NoteAdapter.getItemCount() + "");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showHide() {
        Log.d("staates", status + "");
        if (status==1) {
            btnNotes.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            btnNotes.setTextColor(getResources().getColor(R.color.white));
            btnDiary.setBackgroundColor(getResources().getColor(R.color.white));
            btnDiary.setTextColor(getResources().getColor(R.color.colorPrimary));
            btnToDo.setBackgroundColor(getResources().getColor(R.color.white));
            btnToDo.setTextColor(getResources().getColor(R.color.colorPrimary));
            recNoting.setVisibility(View.VISIBLE);
            Log.d("status", "hide recdialry");
            recyclerView.setVisibility(View.GONE);
            recToTo.setVisibility(View.GONE);
        }
        if(status==2) {
            btnDiary.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            btnDiary.setTextColor(getResources().getColor(R.color.white));
            btnNotes.setBackgroundColor(getResources().getColor(R.color.white));
            btnNotes.setTextColor(getResources().getColor(R.color.colorPrimary));
            btnToDo.setBackgroundColor(getResources().getColor(R.color.white));
            btnToDo.setTextColor(getResources().getColor(R.color.colorPrimary));
            recNoting.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recToTo.setVisibility(View.GONE);
            Log.d("status", "hide notes");
        }
        if(status==3){
            btnToDo.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            btnToDo.setTextColor(getResources().getColor(R.color.white));
            btnNotes.setBackgroundColor(getResources().getColor(R.color.white));
            btnNotes.setTextColor(getResources().getColor(R.color.colorPrimary));
            btnDiary.setBackgroundColor(getResources().getColor(R.color.white));
            btnDiary.setTextColor(getResources().getColor(R.color.colorPrimary));
            recNoting.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            recToTo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        System.exit(0);

    }

    public void onClicks() {
        btnNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = 1;
                showHide();
                getNotes(Long.parseLong(dates));
            }
        });
        btnDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = 2;
                showHide();
                getEvents(Long.parseLong(dates));
            }
        });
        btnToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status=3;
                showHide();
                getToDo(Long.parseLong(dates));
            }
        });
        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                dates = dateClicked.getTime() + "";
                getEvents(dateClicked.getTime());
                getNotes(dateClicked.getTime());
                getToDo(dateClicked.getTime());
                showHide();
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                showMonth.setText(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });
        findViewById(R.id.cardAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, NoteOptionActivity.class);
                i.putExtra("Date", dates);
                startActivity(i);
            }
        });
    }

    public void Initialize() {
        status = 1;
        // Initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize firebase user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {

            Utils.toast(getApplicationContext(), firebaseUser.getDisplayName());

        }

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(MainActivity.this
                , GoogleSignInOptions.DEFAULT_SIGN_IN);
        dated_events = new ArrayList<>();
        recyclerView = findViewById(R.id.rec);
        btnToDo = findViewById(R.id.btnToDose);
        recToTo = findViewById(R.id.recToDo);
        recNoting = findViewById(R.id.recNotes);
        dates = System.currentTimeMillis() + "";
        btnDiary = findViewById(R.id.btnDiaryEvents);
        btnNotes = findViewById(R.id.btnNotesevents);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, Calendar.DATE);

        event_dates = new ArrayList<>();

        showHide();
        //Asigning variable
        calendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        showMonth = (TextView) findViewById(R.id.viewselect);
        //First Day of Week
        calendarView.setFirstDayOfWeek(Calendar.SATURDAY);
        calendarView.setUseThreeLetterAbbreviation(true);
        calendarView.shouldSelectFirstDayOfMonthOnScroll(false);
        calendarView.displayOtherMonthDays(true);

        final int callbackId = 42;
        checkPermissions(callbackId, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR);
        readCalendarEvent(getApplicationContext());
        showMonth.setText(dateFormatMonth.format(new Date(calendar.getTimeInMillis())));
        getEvents(calendar.getTimeInMillis());
        getNotes(calendar.getTimeInMillis());
        getToDo(calendar.getTimeInMillis());
    }

    private void checkPermissions(int callbackId, String... permissionsId) {
        boolean permissions = true;
        for (String p : permissionsId) {
            permissions = permissions && ContextCompat.checkSelfPermission(this, p) == PERMISSION_GRANTED;
        }

        if (!permissions)
            ActivityCompat.requestPermissions(this, permissionsId, callbackId);
    }

    public void readCalendarEvent(Context context) {

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(Uri.parse("content://com.android.calendar/events"), (new String[]{"_id", "title", "organizer", "dtstart", "dtend"}), null, null, null);

        List<notes> gCalendar = new ArrayList<notes>();
        try {

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    notes googleCalendar = new notes();
                    // event_ID: ID of tabel Event
//                    int event_ID = cursor.getInt(0);
//                    googleCalendar.setEvent_id(event_ID);
                    // title of Event
                    String title = cursor.getString(1);
                    googleCalendar.setNote(title);
                    String mOrganizer = cursor.getString(2);
                    //  googleCalendar.setOrganizer(mOrganizer);
                    // Date start of Event
                    String dtStart = cursor.getString(3);
                    googleCalendar.setDate(Long.parseLong(dtStart));
                    Event ev1 = new Event(Color.rgb(217, 217, 217), Long.parseLong(dtStart), "Language Martyr's Day");
                    calendarView.addEvent(ev1);

                    //    Log.d("StartDate",new Date(dtStart).getDay()+"");
//                    googleCalendar.setDate(dtStart);
//                    // Date end of Event
//                    String dtEnd = cursor.getString(4);
//                    googleCalendar.setDtend(dtEnd);
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

    public void getEvents(Long d) {
        // Creating date from milliseconds
        // using Date() constructor
        Date result = new Date(d);
        Log.d("Dating", d + "");
        List<notes> finalDate = new ArrayList<>();
        for (notes it : event_dates) {
            Date resutl2 = new Date(it.getDate());
            if (resutl2.getDate() == result.getDate()) {
                finalDate.add(it);
            }
        }
        Log.d("timing", finalDate + "" + d);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getApplicationContext()));
        adapter = new noteAdapter(getApplicationContext(), finalDate);
        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(adapter);
    }
}
