package me.phum.pocketigl;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecycleViewAdapter";
    private ArrayList<String> UserNames = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<String> userNames, Context context) {
        UserNames = userNames;
        mContext = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Responsible for inflating view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        holder.USER.setText(UserNames.get(position));
        holder.USER.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Patrick carries right here
                Toast.makeText(mContext, UserNames.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return UserNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView USER;
        RelativeLayout parentLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            USER = itemView.findViewById(R.id.user);
            parentLayout =itemView.findViewById(R.id.parent_layout);
        }
    }
}
