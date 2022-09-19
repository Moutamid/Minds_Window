package com.moutamid.mycalender;


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

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

//adapter is a class which we used to show list of data for example this adapter is used to show all the compaings in the project
public class noteAdapter extends RecyclerView.Adapter<noteAdapter.View_Holder> {
    private noteAdapter.OnitemClickListener mListener;
    Context context;
   Boolean state=false;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("notes");

    public interface OnitemClickListener {
        void onEditClick(int position);//

        void onDeleteClick(int position);

    }

    public void setOnItemClick(noteAdapter.OnitemClickListener listener) {
        mListener = listener;
    }

    LayoutInflater layoutInflater;
    List<notes> users;


    public noteAdapter(Context ctx, List<notes> users,Boolean state) {
        this.layoutInflater = LayoutInflater.from(ctx);
        this.users = users;
        context = ctx;
        this.state=state;
        Log.d("listItems",users+"");

    }

    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_event, parent, false);//here we define what view is our adapter showing here we are showing row_all_compaings view which you can see in res->layout
        return new View_Holder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, int position) {
//        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
//        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
//        holder.cardView.setBackgroundColor(randomAndroidColor);
        String urls = null;
        holder.title.setText(users.get(position).getTitle());
        holder.description.setText(users.get(position).getNote());
        //here we are defining our data what we have to show it is coming from tha api
        Query myMostViewedPostsQuery = database.orderByChild("date").equalTo(users.get(position).getDate());
        holder.img.setImageResource(R.drawable.monthico);
        if(state){
            holder.date.setVisibility(View.VISIBLE);
            SimpleDateFormat dateFormatMonth = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            holder.date.setText(dateFormatMonth.format(users.get(position).getDate()));

        }
        else{
            holder.date.setVisibility(View.GONE);
        }
        myMostViewedPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    notes not = postSnapshot.getValue(notes.class);
                    Log.d("imageUrl",not.getImgUrl()+"");
                    if(not.getImgUrl()==null){

                        break;
                    }

                    if (not.getImgUrl() != null) {
                        Glide.with(context)
                                .asBitmap()
                                .load(not.getImgUrl())
                                .apply(new RequestOptions()
                                )
                                .diskCacheStrategy(AUTOMATIC)
                                .into(holder.img);

                    }
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.imgEdit.setImageResource(R.drawable.ic_baseline_edit_24);
        holder.imgDelete.setImageResource(R.drawable.ic_baseline_delete_24);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class View_Holder extends RecyclerView.ViewHolder {
        TextView title,date,description;
        ImageView img, imgEdit, imgDelete;


        public View_Holder(@NonNull View itemView, final noteAdapter.OnitemClickListener listener) {
            super(itemView);
            //here we are initializing our components that were in the roww_all_views
            title = (TextView) itemView.findViewById(R.id.tvToDo);
            img = itemView.findViewById(R.id.imgToDo);
            imgEdit = itemView.findViewById(R.id.event_edit);
            description=itemView.findViewById(R.id.tvDescription);
            imgDelete = itemView.findViewById(R.id.event_delete);
            date=itemView.findViewById(R.id.event_date);
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


