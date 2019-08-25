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

    PlayingSongTitleFetcher(ISongTitleFetcher iSongTitleFetcher) {
        mHandler = new Handler();
        this.iSongTitleFetcher = iSongTitleFetcher;
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
                URL url = new URL("http://www.1055rock.gr/player/jaz.txt");
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String str = in.readLine();
                String[] parts = str.split("\\|");

                String finalString = String.format("%s - %s", parts[3], parts[4]);

                iSongTitleFetcher.onSongTitleAvailable(finalString);

                in.close();
            } catch (IOException ignored) {
            } finally {
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };
}