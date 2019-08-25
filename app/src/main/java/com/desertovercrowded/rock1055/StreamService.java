package com.desertovercrowded.rock1055;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;

import java.io.IOException;

import static com.desertovercrowded.rock1055.Application.CHANNEL_ID;

public class StreamService extends Service {
    static MediaPlayer player;
    public int serviceAvailable;
    public static boolean isPlaying = false;
    private MediaSessionCompat mediaSession;

    public StreamService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initializeNotificationChannel(); // NotificationChannel, MUST PUT PLAY AND STOP BUTTONS
        initializeMediaPlayer(); // Initialize MediaPlayer
        startPlaying(); // Start Streaming
        return START_NOT_STICKY; // Better START_STICKY they say
    }

    public void initializeNotificationChannel() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Bitmap artwork = BitmapFactory.decodeResource(getResources(), R.drawable.ic_rock_black);

        mediaSession = new MediaSessionCompat(this, "tag");

        /*//ΓΙΑ ΚΛΙΚ ΣΤΑ ΚΩΛΟΚΟΥΜΠΑ
                Intent activityPlay = new Intent(this, Contact.class);
        PendingIntent contentPlay = PendingIntent.getActivity(this,
                0, activityPlay, 0);
        Intent activityStop = new Intent(this, MainActivity.class);
        PendingIntent contentStop = PendingIntent.getActivity(this,
                0, activityStop, 0);
        Intent activityS = new Intent(this, StreamService.class);
        PendingIntent contentS = PendingIntent.getActivity(this,
                0, activityStop, 0);*/

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("1055 Rock")
                //.setContentText(text)
                //.setContentTitle(title)
                .setSmallIcon(R.drawable.ic_radio_black_24dp)
                .setLargeIcon(artwork)
                /*.addAction(R.drawable.ic_pause_circle_filled_black_24dp, "Previous", contentS)
                .addAction(R.drawable.ic_play_circle_outline_black_24dp, "Play", contentPlay)
                .addAction(R.drawable.ic_exit_to_app_black_24dp, "Exit", null)*/
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        //.setShowActionsInCompactView(1,2)
                        .setMediaSession(mediaSession.getSessionToken()))
                //.setSubText("Sub Text")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        startForeground(1, notification);
    }

    public static void initializeMediaPlayer() {
        player = new MediaPlayer();
        player.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build());
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            //player.setDataSource("http://radio.onweb.gr:8078/");
            player.setDataSource("http://radio.1055rock.gr:30000/1055/");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void startPlaying() {
        try {
            player.prepareAsync();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    player.start();
                }
            });
        } catch (Exception ignored) {
        }
    }

    public void stopPlaying() throws IOException {
        player.stop();
        player.reset();
        player.release();
        player = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            try {
                stopPlaying();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}