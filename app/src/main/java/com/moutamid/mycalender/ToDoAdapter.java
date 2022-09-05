package com.moutamid.mycalender;


import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Random;


//adapter is a class which we used to show list of data for example this adapter is used to show all the compaings in the project
public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.View_Holder> {
    private static final String TAG = "NotesAdapter";
    private ToDoAdapter.OnitemClickListener mListener;
    Context context;
    TextToSpeech textToSpeech;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("notes");

    public interface OnitemClickListener {
        void OnItemClick(int position);//

        void onaddclick(int position);

    }

    public void setOnItemClick(ToDoAdapter.OnitemClickListener listener) {
        mListener = listener;
    }

    LayoutInflater layoutInflater;
    List<SimpleNotes> users;


    public ToDoAdapter(Context ctx, List<SimpleNotes> users) {
        this.layoutInflater = LayoutInflater.from(ctx);
        this.users = users;
        context = ctx;

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
//        holder.date.setText(users.get(position).getDate());
    //    holder.img.setImageResource(R.drawable.ic_circle_svgrepo_com);

    }


    @Override
    public int getItemCount() {
        return users.size();
    }

    class View_Holder extends RecyclerView.ViewHolder {
        TextView title;


        public View_Holder(@NonNull View itemView, final ToDoAdapter.OnitemClickListener listener) {
            super(itemView);
            //here we are initializing our components that were in the roww_all_views
            title = (TextView) itemView.findViewById(R.id.toDoTitle);
       //     img = itemView.findViewById(R.id.imageView3);
//            cardView=itemVie
//            date = itemView.findViewById(R.id.tvDate);

        }
    }
}





