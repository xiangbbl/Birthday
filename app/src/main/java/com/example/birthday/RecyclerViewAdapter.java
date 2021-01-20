package com.example.birthday;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecViewHolder> {
    private ArrayList<Items> arrayList;
    public static class RecViewHolder extends RecyclerView.ViewHolder{
        public ImageView image;
        public TextView mText1;
        public TextView mText2;
        public RecViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView);
            mText1 = itemView.findViewById(R.id.textView3);
            mText2 = itemView.findViewById(R.id.textView4);
        }
    }

    public RecyclerViewAdapter(ArrayList<Items> arrList){
        arrayList = arrList;
    }

    @NonNull
    @Override
    public RecViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_item, parent, false);
        RecViewHolder rvh = new RecViewHolder(v);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecViewHolder holder, int position) {
        Items currItem = arrayList.get(position);
        holder.image.setImageResource(currItem.getImage());
        holder.mText1.setText(currItem.getText1());
        holder.mText2.setText(currItem.getText2());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
