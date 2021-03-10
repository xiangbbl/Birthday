package com.example.birthday;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
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
import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.birthday.App.CHANNEL_1_ID;

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
    ArrayList<Items> arr_temp = new ArrayList<>();
    private String mDate;
    private NotificationManagerCompat notificationManagerCompat;

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
        notificationManagerCompat = NotificationManagerCompat.from(getContext());
    }
    @Override
    public void onResume() {
        super.onResume();
        arr.clear();
        arr_temp.clear();
        loadData();
        mAdapter.notifyDataSetChanged();
        //notificationManagerCompat = NotificationManagerCompat.from(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = new DatabaseHelper(getContext());
        View view = inflater.inflate(R.layout.fragment_recent, container, false);
        recyclerView = view.findViewById(R.id.RecView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerViewAdapter(arr);
        recyclerView.setAdapter(mAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        mAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d(TAG, "item click for Recent fragment on position" + position);
                retData(position);
            }
        });
        Log.d(TAG, "CreateView for Recent started. ");
        return view;
    }
    public void loadData(){
        Cursor cursor = db.loadRecData();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat mFormat = new SimpleDateFormat("MM/dd");
        String CurrTime = mFormat.format(c.getTime());
        //Log.d(TAG, "Current Time: " + CurrTime);
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
                    arr_temp.add(new Items(R.drawable.ic_baseline_person,
                            cursor.getString(cursor.getColumnIndex("name")),
                            cursor.getString(cursor.getColumnIndex("birthday"))));
                }
                else if(diff < 7 && diff > 0){
                    arr.add(new Items(R.drawable.ic_baseline_person,
                            cursor.getString(cursor.getColumnIndex("name")),
                            cursor.getString(cursor.getColumnIndex("birthday")).split("/")[0] + "/"
                                    + (cursor.getString(cursor.getColumnIndex("birthday"))).split("/")[1]));
                    arr_temp.add(new Items(R.drawable.ic_baseline_person,
                            cursor.getString(cursor.getColumnIndex("name")),
                            cursor.getString(cursor.getColumnIndex("birthday"))));
                    //notification();
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
    public void retData(int position){
        Cursor cursor = db.retData(arr_temp.get(position).getText1(), arr_temp.get(position).getText2());
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
        intent.putExtra("id", 0);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /*public void notification(){
        Notification notification = new NotificationCompat.Builder(getContext(), CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_baseline_person)
                .setContentTitle("Birthday")
                .setContentText("Someone's birthday is coming soon!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManagerCompat.notify((int) System.currentTimeMillis(), notification);
    }*/
}