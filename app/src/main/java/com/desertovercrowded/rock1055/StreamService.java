package com.desertovercrowded.rock1055;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.IOException;

import static com.desertovercrowded.rock1055.Application.CHANNEL_ID;

public class StreamService extends Service implements ISongTitleFetcher {
    static MediaPlayer player;
    Notification notification = null;

    public StreamService() {
    }

    public static void finish() {
        stopPlaying();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PlayingSongTitleFetcher playingSongTitleFetcher;
        /* // For notification channel TODO
        if (flags == -1) {
            try {
                stopPlaying();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

        initializeMediaPlayer(); // Initialize MediaPlayer
        playingSongTitleFetcher = new PlayingSongTitleFetcher(this);
        playingSongTitleFetcher.startRepeatingTask();
        startPlaying(); // Start Streaming
        return START_NOT_STICKY; // Better START_STICKY they say for mediaplayer. But it is problematic.
    }

    public static void initializeMediaPlayer() {
        player = new MediaPlayer();
        player.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build());

        try {
            player.setDataSource("http://radio.1055rock.gr:30000/1055/");

        } catch (IllegalArgumentException | IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void startPlaying() {
        try {
            player.prepareAsync();
            player.setOnPreparedListener(mp -> player.start());
        } catch (Exception ignored) {
        }
    }

    public static void stopPlaying() {
        try {
            player.stop();
            player.reset();
            player.release();
            player = null;
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            stopPlaying();
            stopForeground(true);
        }
    }

    @Override
    public void onSongTitleAvailable(String title, String artist) {
        MediaSessionCompat mediaSession;

        try {
            Intent notificationIntent = new Intent(this, MainActivity.class);
           // notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                pendingIntent = PendingIntent.getActivity(this,
                        0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            } else {
                pendingIntent = PendingIntent.getActivity(this,
                        0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            }

/*      // For button clicking
        Intent activityPlay = new Intent(this, Contact.class);
        PendingIntent contentPlay = PendingIntent.getActivity(this,
                0, activityPlay, 0);
        Intent activityStop = new Intent(this, MainActivity.class);
        PendingIntent contentStop = PendingIntent.getActivity(this,
                0, activityStop, 0);
        Intent activityS = new Intent(this, StreamService.class);
        PendingIntent contentS = PendingIntent.getActivity(this,
                0, activityStop, 0);*/

/*          There is a problem with .setStyle and mediaSession, notification Channel won't apper on some phones.
            notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(artist)
                    .setSmallIcon(R.drawable.ic_radio_black_24dp)
                    .setLargeIcon(artwork)
                    *//*.addAction(R.drawable.ic_pause_circle_filled_black_24dp, "Previous", contentS)
                    .addAction(R.drawable.ic_play_circle_outline_black_24dp, "Play", contentPlay)
                    *//*
                    //.addAction(R.drawable.ic_exit_to_app_black_24dp, "Exit", null)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                            //.setShowActionsInCompactView(1,2)
                            .setMediaSession(mediaSession.getSessionToken()))
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .build();*/

            mediaSession = new MediaSessionCompat(this, "tag");
            Bitmap artwork = BitmapFactory.decodeResource(getResources(), R.drawable.ic_rock_black);
            notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(artist)
                    .setSmallIcon(R.drawable.ic_radio_black_24dp)
                    .setLargeIcon(artwork)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build();

            startForeground(1, notification);
        } catch (Exception e) {
            Log.e("TAG", "onSongTitleAvailable: ", e);
        }
    }
}