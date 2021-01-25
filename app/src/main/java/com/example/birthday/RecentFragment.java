package com.example.birthday;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    DatabaseHelper db;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Items> arr = new ArrayList<>();

    public RecentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecentFragment newInstance(String param1, String param2) {
        RecentFragment fragment = new RecentFragment();
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
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //arr.clear();
        db = new DatabaseHelper(getContext());
        View view = inflater.inflate(R.layout.fragment_recent, container, false);
        recyclerView = view.findViewById(R.id.RecView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerViewAdapter(arr);
        recyclerView.setAdapter(mAdapter);

        loadData();
        //arr.add(new Items(R.drawable.ic_baseline_person, "Name1", "11.30"));
        //arr.add(new Items(R.drawable.ic_baseline_person, "Name2", "12/31"));
        Log.d(TAG, "CreateView for Recent started. ");
        return view;
    }
    public void loadData(){
        Cursor cursor = db.loadRecData();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat mFormat = new SimpleDateFormat("MM/dd");
        String CurrTime = mFormat.format(c.getTime());
        Log.d(TAG, "Current Time: " + CurrTime);
        Date db_time;
        Date cur_time;
        long diff;

        while(cursor.moveToNext()){
            try {
                db_time= mFormat.parse(cursor.getString(cursor.getColumnIndex("birthday")).split("/")[0] + "/"
                        + (cursor.getString(cursor.getColumnIndex("birthday"))).split("/")[1]);
                cur_time = mFormat.parse(CurrTime);
                diff = getDiff(db_time, cur_time);
                //System.out.println("Curr time: " + cur_time + " db_time: " + db_time + " time different " + diff);
                if (diff == 0) {
                    arr.add(new Items(R.drawable.ic_baseline_person,
                            cursor.getString(cursor.getColumnIndex("name")),
                            "Today"));
                }
                else if(diff < 7 && diff > 0){
                    arr.add(new Items(R.drawable.ic_baseline_person,
                            cursor.getString(cursor.getColumnIndex("name")),
                            cursor.getString(cursor.getColumnIndex("birthday")).split("/")[0] + "/"
                                    + (cursor.getString(cursor.getColumnIndex("birthday"))).split("/")[1]));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    public long getDiff(Date cur_time, Date db_time){
        long TimeDiff = cur_time.getTime() - db_time.getTime();
        long seconds = TimeDiff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        return days;
    }
}