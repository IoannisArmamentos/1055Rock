package com.desertovercrowded.rock1055;

import android.os.Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class PlayingSongTitleFetcher {

    private Handler mHandler;
    private int mInterval = 20000; // 20 seconds
    private ISongTitleFetcher iSongTitleFetcher;
    private ISongTitleFetcher subscriber;

    PlayingSongTitleFetcher(ISongTitleFetcher iSongTitleFetcher) {
        mHandler = new Handler();
        this.iSongTitleFetcher = iSongTitleFetcher;
    }

    public void subscribe(ISongTitleFetcher subscriber) {
        this.subscriber = subscriber;
    }

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    private Runnable mStatusChecker = new Runnable() {

        @Override
        public void run() {
            try {
                URL url = new URL("http://1055rock.gr/player/jaz.php");
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String str = in.readLine();
                String[] parts = str.split(" - ");
                //String finalString = String.format("%s \n%s", parts[1], parts[0]); FOR ONE STRING, ONE LINE

                String title = String.format("%s", parts[1]);
                String artist = String.format("%s", parts[0]);
                iSongTitleFetcher.onSongTitleAvailable(title, artist);
                if (subscriber != null) {
                    subscriber.onSongTitleAvailable(title, artist);
                }
                in.close();
            } catch (IOException ignored) {
            } finally {
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };
}