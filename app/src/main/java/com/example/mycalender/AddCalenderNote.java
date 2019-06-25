package com.example.mycalender;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mycalender.databinding.ActivityAddCalenderNoteBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class AddCalenderNote extends AppCompatActivity {
    ActivityAddCalenderNoteBinding binding;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("notes");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;
    String Date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCalenderNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Bundle b = getIntent().getExtras();
        Date = b.getString("Date", "null");
        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });
        binding.uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.isEmpty(binding.EtNote)||imageUri==null) {
                    Utils.toast(getApplicationContext(),"please input title");
                }
                else{
               addToCalender();
               uploadToFirebase(imageUri);
               binding.imgWord.setVisibility(View.VISIBLE);
               binding.cardView3.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            imageUri = data.getData();
            binding.imageView.setImageURI(imageUri);

        }
    }

    private void uploadToFirebase(Uri uri) {

        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        notes data = new notes();
                        data.id = root.push().getKey();
                        data.note = binding.EtNote.getText().toString();
                        data.imgUrl = uri.toString();
                        data.date = Long.parseLong(Date);
                        root.child(data.id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    binding.imgWord.setVisibility(View.INVISIBLE);
                                    binding.cardView3.setVisibility(View.VISIBLE);
                                    Toast.makeText(AddCalenderNote.this, "good", Toast.LENGTH_SHORT).show();
                                } else {
                                    binding.imgWord.setVisibility(View.INVISIBLE);
                                    binding.cardView3.setVisibility(View.VISIBLE);
                                    Toast.makeText(AddCalenderNote.this, "bad", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        binding.imgWord.setVisibility(View.INVISIBLE);
                        binding.cardView3.setVisibility(View.VISIBLE);
                        Toast.makeText(AddCalenderNote.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddCalenderNote.this, MainActivity.class));
                        finish();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                binding.imgWord.setVisibility(View.INVISIBLE);
                binding.cardView3.setVisibility(View.VISIBLE);
                Toast.makeText(AddCalenderNote.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri) {

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }
    public void addToCalender(){
        ContentResolver cr=AddCalenderNote.this.getContentResolver();
        ContentValues cv=new ContentValues();
        cv.put(CalendarContract.Events.TITLE,binding.EtNote.getText().toString());
        cv.put(CalendarContract.Events.DESCRIPTION,"");
        cv.put(CalendarContract.Events.DTSTART,Long.parseLong(Date));
        cv.put(CalendarContract.Events.DTEND,Long.parseLong(Date)+60+60*1000);
        cv.put(CalendarContract.Events.CALENDAR_ID,1);
        cv.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().getTimeZone().getID());
        cr.insert(CalendarContract.Events.CONTENT_URI,cv);


    }
}