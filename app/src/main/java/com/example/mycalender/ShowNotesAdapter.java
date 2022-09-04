package com.example.mycalender;


import static com.bumptech.glide.load.engine.DiskCacheStrategy.AUTOMATIC;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Random;

//adapter is a class which we used to show list of data for example this adapter is used to show all the compaings in the project
public class ShowNotesAdapter extends RecyclerView.Adapter<ShowNotesAdapter.View_Holder> {
    private ShowNotesAdapter.OnitemClickListener mListener;
    private static final String TAG = "NotesAdapter";
    Context context;
//    DatabaseReference database = FirebaseDatabase.getInstance().getReference("notes");

    public interface OnitemClickListener {
        void OnItemClick(int position);//

        void onaddclick(int position);

    }

    public void setOnItemClick(ShowNotesAdapter.OnitemClickListener listener) {
        mListener = listener;
    }

    LayoutInflater layoutInflater;
    List<SimpleNotes> users;


    public ShowNotesAdapter(Context ctx, List<SimpleNotes> users) {
        this.layoutInflater = LayoutInflater.from(ctx);
        this.users = users;
        context = ctx;
        Log.d(TAG, users + "");

    }

    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_notes, parent, false);//here we define what view is our adapter showing here we are showing row_all_compaings view which you can see in res->layout
        return new View_Holder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, int position) {
        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        String urls = null;
        holder.title.setText(users.get(position).getNote());//here we are defining our data what we have to show it is coming from tha api
        holder.img.setImageResource(R.drawable.ic_circle_svgrepo_com);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class View_Holder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView img;


        public View_Holder(@NonNull View itemView, final ShowNotesAdapter.OnitemClickListener listener) {
            super(itemView);
            //here we are initializing our components that were in the roww_all_views
            title = (TextView) itemView.findViewById(R.id.tvWord);
            img = itemView.findViewById(R.id.imageView3);

        }
    }

}


