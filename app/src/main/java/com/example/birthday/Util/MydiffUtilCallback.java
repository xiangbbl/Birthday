package com.example.birthday.Util;

import androidx.recyclerview.widget.DiffUtil;

import com.example.birthday.Items;

import java.util.ArrayList;
import java.util.List;

public class MydiffUtilCallback extends DiffUtil.Callback {

    private ArrayList<Items> OldList;
    private ArrayList<Items> NewList;
    public MydiffUtilCallback(ArrayList<Items> OldList, ArrayList<Items> NewList){
        this.OldList = OldList;
        this.NewList = NewList;
    }

    @Override
    public int getOldListSize() {
        return OldList.size();
    }

    @Override
    public int getNewListSize() {
        return NewList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItemPosition == newItemPosition;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return OldList.get(oldItemPosition) == NewList.get(newItemPosition);
    }
}
