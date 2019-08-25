package com.desertovercrowded.rock1055;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import static com.desertovercrowded.rock1055.Application.CHANNEL_ID;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public boolean streams;
    private ImageButton ibTooglePlay;
    private int mInterval = 20000; // 20 seconds
    private Handler mHandler;
    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Navigation Drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // For policies, otherwise it won't run
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // If no Internet Connection
        if (!isOnline()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.isonline)
                    .setTitle(R.string.unabletoconnect)
                    .setCancelable(false)
                    .setPositiveButton(R.string.settings,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                    finish();
                                    startActivity(intent);
                                }
                            }
                    )
                    .setNegativeButton(R.string.exit,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            }
                    );
            AlertDialog alert = builder.create();
            alert.show();

            return;
        }

        // Foreground Service for streaming. May god's love be with you
        initializeUIElements();

        // Handler for song title from text file, repeats every 20sec
        mHandler = new Handler();
        startRepeatingTask();
    }

    Runnable mStatusChecker = new Runnable() {

        @Override
        public void run() {
            try {
                URL url = new URL("http://www.1055rock.gr/player/jaz.txt");
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String str = in.readLine();
                String[] parts = str.split("\\|");

                String finalString = String.format("%s - %s", parts[3], parts[4]);

                TextView textView = (TextView) findViewById(R.id.songplaying);

                textView.setText(finalString);

                in.close();
            } catch (IOException ignored) {
            } finally {
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    public void startRepeatingTask() {
        mStatusChecker.run();
    }

    public void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            onDestroy();
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent intent = new Intent();
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            intent.setClass(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_program) {
            intent.setClass(this, Programold.class);
            startActivity(intent);
        } else if (id == R.id.nav_producers) {
            intent.setClass(this, Producers.class);
            startActivity(intent);
        } else if (id == R.id.nav_contact) {
            intent.setClass(this, Contact.class);
            startActivity(intent);
        } else if (id == R.id.nav_info) {
            intent.setClass(this, About.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            share();
        } else if (id == R.id.nav_rate) {
            rate();
        } else if (id == R.id.nav_exit) {
            onDestroy();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Checks if user has Internet access
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    //ImageButtons
    public void initializeUIElements() {
        ibTooglePlay = (ImageButton) findViewById(R.id.ibToogleState);

        // Play Button
        ibTooglePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!streams) {
                    Intent serviceIntent = new Intent(v.getContext(), StreamService.class);
                    ContextCompat.startForegroundService(v.getContext(), serviceIntent);
                    ibTooglePlay.setImageResource(R.drawable.ic_pause_circle_outline_black_48dp);
                    streams=true;
                }
                else
                {
                    Intent serviceIntent = new Intent(v.getContext(), StreamService.class);
                    stopService(serviceIntent);
                    ibTooglePlay.setImageResource(R.drawable.ic_play_circle_outline_black_48dp);
                    streams=false;
                }
            }
        });
    }

    //When the app closes, delete NotificationChannel
    public void deleteNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.deleteNotificationChannel(CHANNEL_ID);
        }
    }

    //Share the app
    private void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share));
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    //Rate the app
    private void rate() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(
                "https://play.google.com/store/apps/details?id=com.desertovercrowded.rock1055"));
        intent.setPackage("com.android.vending");
        Toast.makeText(getApplicationContext(), " :) ", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
        deleteNotificationChannel();
        if (streams) {
            Intent serviceIntent = new Intent(this, StreamService.class);
            stopService(serviceIntent);
            streams = false;
        }
        finish();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}