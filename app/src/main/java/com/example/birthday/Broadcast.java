package com.example.birthday;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

import static android.content.ContentValues.TAG;
import static android.content.Context.ALARM_SERVICE;
import static com.example.birthday.App.CHANNEL_1_ID;

public class Broadcast extends BroadcastReceiver {
    String name;
    int id;
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.hasExtra("id")){
            Bundle b = intent.getExtras();
            if(b != null) {
                name = intent.getExtras().getString("name");
                id = b.getInt("id");
            }
        }
        //System.out.println("name: " + name + " | id: " + id);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setContentTitle("Birthday")
                    .setContentText(name + "'s birthday is coming soon!")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .build();
            // set up new alarm for next year
            //System.out.println("give notification");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify((int) System.currentTimeMillis(), notification);

        newAlarm(context);
    }

    public void newAlarm(Context context){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        Intent intent = new Intent(context, Broadcast.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);
        if (c.getTimeInMillis() > System.currentTimeMillis()){
            System.out.println("New Alarm should go off soon");
        }
        else {
            //System.out.println("Alarm is already passed");
            //System.out.println("!!!!!!!!!! " + Calendar.getInstance().get(Calendar.MONTH));
            c.set(Calendar.getInstance().get(Calendar.YEAR)+1, Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 07, 00, 0);
            System.out.println(c.getTime() + "???????????????");
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

}
