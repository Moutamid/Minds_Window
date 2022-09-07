package com.moutamid.mycalender;


import android.content.Context;
import android.media.Image;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Random;


//adapter is a class which we used to show list of data for example this adapter is used to show all the compaings in the project
public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.View_Holder> {
    private static final String TAG = "NotesAdapter";
    private ToDoAdapter.OnitemClickListener mListener;
    private DatabaseReference ToDoDb = FirebaseDatabase.getInstance().getReference(Constants.ToDo);
    Context context;
    Boolean states=false;
    TextToSpeech textToSpeech;

    public interface OnitemClickListener {
        void onEditClick(int position);//

        void onDeleteClick(int position);
        void onCheckClick(int position);

    }

    public void setOnItemClick(ToDoAdapter.OnitemClickListener listener) {
        mListener = listener;
    }

    LayoutInflater layoutInflater;
    List<SimpleNotes> users;


    public ToDoAdapter(Context ctx, List<SimpleNotes> users,Boolean state) {
        this.layoutInflater = LayoutInflater.from(ctx);
        this.users = users;
        context = ctx;
        this.states=state;

    }

    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called");
        View view = layoutInflater.inflate(R.layout.row_to_do, parent, false);//here we define what view is our adapter showing here we are showing row_all_compaings view which you can see in res->layout
        return new View_Holder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, int position) {
        SimpleNotes currentItem = users.get(position);
        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        //   holder.MainCard.setCardBackgroundColor(randomAndroidColor);
        holder.title.setText(users.get(position).getNote());
        Boolean state = false;
        if (currentItem.getStatus() == null) {
            state = false;
        } else {
            state = currentItem.getStatus();
        }
        holder.chk.setChecked(state);
        holder.chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ToDoDb.child(currentItem.getId()).child("status").setValue(b);

            }
        });
        holder.imgEdit.setImageResource(R.drawable.ic_baseline_edit_24);
        holder.imgDelete.setImageResource(R.drawable.ic_baseline_delete_24);
        if(states){
            holder.date.setVisibility(View.VISIBLE);
            SimpleDateFormat dateFormatMonth = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            holder.date.setText(dateFormatMonth.format(users.get(position).getDate()));

        }
        else{
            holder.date.setVisibility(View.GONE);
        }
//        holder.date.setText(users.get(position).getDate());
        //    holder.img.setImageResource(R.drawable.ic_circle_svgrepo_com);

    }


    @Override
    public int getItemCount() {
        return users.size();
    }

    class View_Holder extends RecyclerView.ViewHolder {
        TextView title,date;
        CheckBox chk;
        ImageView img, imgEdit, imgDelete;

        public View_Holder(@NonNull View itemView, final ToDoAdapter.OnitemClickListener listener) {
            super(itemView);
            //here we are initializing our components that were in the roww_all_views
            title = (TextView) itemView.findViewById(R.id.toDoTitle);
            chk = itemView.findViewById(R.id.chkBox);
            date=itemView.findViewById(R.id.tvToDoDate);
            //     img = itemView.findViewById(R.id.imageView3);
//            cardView=itemVie
//            date = itemView.findViewById(R.id.tvDate);
            imgEdit = itemView.findViewById(R.id.toDoEdit);
            imgDelete = itemView.findViewById(R.id.toDoDelete);
            imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onEditClick(position);
                        }
                    }
                }
            });

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

        }
    }
}





