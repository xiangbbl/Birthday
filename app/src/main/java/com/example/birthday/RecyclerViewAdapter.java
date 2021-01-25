package com.example.birthday;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birthday.Util.MydiffUtilCallback;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.addAll;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecViewHolder> {
    private ArrayList<Items> arrayList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    public static class RecViewHolder extends RecyclerView.ViewHolder{
        public ImageView image;
        public TextView mText1;
        public TextView mText2;
        public RecViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView);
            mText1 = itemView.findViewById(R.id.textView3);
            mText2 = itemView.findViewById(R.id.textView4);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public RecyclerViewAdapter(ArrayList<Items> arrList){
        arrayList = arrList;
    }

    @NonNull
    @Override
    public RecViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_item, parent, false);
        RecViewHolder rvh = new RecViewHolder(v, onItemClickListener);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecViewHolder holder, int position) {
        Items currItem = arrayList.get(position);
        holder.image.setImageResource(currItem.getImage());
        holder.mText1.setText(currItem.getText1());
        holder.mText2.setText(currItem.getText2());
        if(currItem.getText2().equals("Today")) {
            holder.mText2.setTextColor(Color.parseColor("#FFFF5100"));
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void insert(ArrayList<Items> insertList){
        MydiffUtilCallback mydiffUtilCallback = new MydiffUtilCallback(arrayList, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(mydiffUtilCallback);
        arrayList.addAll(insertList);
        diffResult.dispatchUpdatesTo(this);
    }
    public void update(ArrayList<Items> insertList){
        MydiffUtilCallback mydiffUtilCallback = new MydiffUtilCallback(arrayList, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(mydiffUtilCallback);
        arrayList.clear();
        arrayList.addAll(insertList);
        diffResult.dispatchUpdatesTo(this);
    }
    public void updateList(ArrayList<Items> insertList) {
        insertList.clear();
        addAll(insertList);
    }
}
