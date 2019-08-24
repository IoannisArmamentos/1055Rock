package com.desertovercrowded.rock1055;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Application extends android.app.Application {
    public static final String CHANNEL_ID = "1055ServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

        DataSingleton.getInstance().LoadDataFromJson(this);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "1055 Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            //channel1.setDescription("This is Channel 1");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
