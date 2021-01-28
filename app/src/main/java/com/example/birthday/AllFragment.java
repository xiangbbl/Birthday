package com.example.birthday;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    //private RecyclerViewAdapter mAdapter;
    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseHelper db;
    ArrayList<Items> arr = new ArrayList<>();
    ArrayList<data> arr_data = new ArrayList<>();

    public AllFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllFragment newInstance(String param1, String param2) {
        AllFragment fragment = new AllFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        arr.clear();
        loadData();
        //adapter.updateList(arr);
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //arr.clear();
        db = new DatabaseHelper(getContext());
        View view = inflater.inflate(R.layout.fragment_all, container, false);
        recyclerView = view.findViewById(R.id.RecView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new RecyclerViewAdapter(arr);
        recyclerView.setAdapter(adapter);
        //recyclerView.addItemDecoration();

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d(TAG, "item click for All fragment on position" + position);
                retData(position);
            }
        });
        //loadData();
        Log.d(TAG, "CreateView for All started. ");
        return view;
    }
    public void loadData(){
        Cursor cursor = db.loadData();
        while(cursor.moveToNext()){
            arr.add(new Items(R.drawable.ic_baseline_person,
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("birthday"))));
        }
    }

    public void retData(int position){
        Cursor cursor = db.retData(arr.get(position).getText1(),arr.get(position).getText2());
        cursor.moveToNext();
        String name = cursor.getString(cursor.getColumnIndex("name"));
        String Nickname = cursor.getString(cursor.getColumnIndex("nickname"));
        String date = cursor.getString(cursor.getColumnIndex("birthday"));
        String Addition = cursor.getString(cursor.getColumnIndex("add_info"));

        Intent intent = new Intent(getContext(), ViewInforPage.class);
        intent.putExtra("name", name);
        intent.putExtra("nickname", Nickname);
        intent.putExtra("date", date);
        intent.putExtra("addition", Addition);
        intent.putExtra("id", 1);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}