package com.example.birthday;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import java.text.ParseException;
import java.util.Calendar;

public class App extends Application {
    public static final String CHANNEL_1_ID = "channel1";
    DatabaseHelper db;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
        RescheduleAlarm();
    }

    private void createNotificationChannels(){

        Intent intent = new Intent();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is channel 1");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }

    public void RescheduleAlarm(){
        db = new DatabaseHelper(getApplicationContext());
        Cursor cursor = db.loadData();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(System.currentTimeMillis());
        int len, Year, Month, Day, cYear, sDay;
        String name;
        if(cursor.moveToFirst()) {
            //cursor.moveToNext();
            name = cursor.getString(cursor.getColumnIndex("name"));
            len = name.length();
            Year = Integer.parseInt(cursor.getString(cursor.getColumnIndex("birthday")).split("/")[2]);
            Month = Integer.parseInt(cursor.getString(cursor.getColumnIndex("birthday")).split("/")[0]) - 1;
            Day = Integer.parseInt(cursor.getString(cursor.getColumnIndex("birthday")).split("/")[1]);

            c1.set(Year, Month, Day, 00, 00, 0);
            c1.set(Calendar.MILLISECOND, 0);
            int id = (int) c1.getTimeInMillis() + len + name.charAt(0) + name.charAt(name.length() - 1);
            Intent intent = new Intent(getApplicationContext(), Broadcast.class);
            //Intent intent = new Intent("com.my.package.MY_UNIQUE_ACTION");

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, intent, 0);
            boolean alarmUp = (PendingIntent.getBroadcast(getApplicationContext(), id,
                    new Intent("com.my.package.MY_UNIQUE_ACTION"),
                    PendingIntent.FLAG_NO_CREATE) != null);

            if (alarmUp) {
                Log.d("myTag", "Alarm is already active");
            }
            else {
                Log.d("myTag", "Alarm is not active!!!!!!!!!!!");
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                cYear = c.get(Calendar.YEAR);
                cursor = db.loadData();
                while(cursor.moveToNext()) {
                    name = cursor.getString(cursor.getColumnIndex("name"));
                    len = name.length();
                    Year = Integer.parseInt(cursor.getString(cursor.getColumnIndex("birthday")).split("/")[2]);
                    Month = Integer.parseInt(cursor.getString(cursor.getColumnIndex("birthday")).split("/")[0]) - 1;
                    Day = Integer.parseInt(cursor.getString(cursor.getColumnIndex("birthday")).split("/")[1]);
                    sDay = Day - 6;
                    c1.set(Year, Month, Day, 00, 00, 0);
                    c1.set(Calendar.MILLISECOND, 0);

                    id = (int) c1.getTimeInMillis() + len + name.charAt(0) + name.charAt(name.length() - 1);
                    System.out.println("id: " + c1.getTimeInMillis() + " | " + len + " | " + name.charAt(0) + " | " + name.charAt(name.length() - 1));
                    intent.putExtra("id", id);
                    pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, intent, 0);
                    c.set(cYear, Month, sDay, 07, 00, 0);

                    if (c.getTimeInMillis() > System.currentTimeMillis()){
                        System.out.println("Alarm should go off soon");
                    }
                    else {
                        System.out.println("Alarm is already passed!!!!!!!!!!!!!!!!!!!!!!!");
                        c.set(cYear+1, Month, sDay, 07, 00, 0);
                    }
                    alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                }
            }
        }
    }
}
