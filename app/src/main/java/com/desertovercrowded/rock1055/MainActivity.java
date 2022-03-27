package com.desertovercrowded.rock1055;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import static com.desertovercrowded.rock1055.Extensions.deleteNotificationChannel;
import static com.desertovercrowded.rock1055.Extensions.displayNoInternetAlert;
import static com.desertovercrowded.rock1055.Extensions.isOnline;
import static com.desertovercrowded.rock1055.Extensions.rate;
import static com.desertovercrowded.rock1055.Extensions.share;
import static com.desertovercrowded.rock1055.Extensions.site;
import static com.desertovercrowded.rock1055.Extensions.sms;
import static com.desertovercrowded.rock1055.Extensions.viber;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ISongTitleFetcher {

    private long backPressedTime;
    private Toast backToast;
    private PlayingSongTitleFetcher playingSongTitleFetcher;

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

        // If button is clicked, start or stop Foreground Service for streaming. May god's love be with you
        initializePlayPauseButton();

        // For policies, otherwise it won't run
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // If no Internet Connection
        if (!isOnline(this)) {
            displayNoInternetAlert(this);
        }

        // Repeating Task for fetching song name from txt file from server
        playingSongTitleFetcher = new PlayingSongTitleFetcher(this);
        playingSongTitleFetcher.startRepeatingTask();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent intent = new Intent();
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                intent.setClass(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_program:
                intent.setClass(this, Program.class);
                startActivity(intent);
                break;
            case R.id.nav_producers:
                intent.setClass(this, Producers.class);
                startActivity(intent);
                break;
            case R.id.nav_contact:
                intent.setClass(this, Contact.class);
                startActivity(intent);
                break;
            case R.id.nav_site:
                site(this);
                break;
            case R.id.nav_sms:
                sms(this);
                break;
            case R.id.nav_viber:
                viber(this);
                break;
            case R.id.nav_info:
                intent.setClass(this, AboutMe.class);
                startActivity(intent);
                break;
            case R.id.nav_rate:
                rate(this);
                break;
            case R.id.nav_share:
                share(this);
                break;
            case R.id.nav_exit:
                finishAffinity();
                break;
            default:
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void initializePlayPauseButton() {
        ImageButton ibTogglePlay = findViewById(R.id.ibToogleState);
        ibTogglePlay.setOnClickListener(new PlayPauseButtonListener());
    }

    @Override
    public void onSongTitleAvailable(String title, String artist) {
        TextView textViewTitle = findViewById(R.id.songtitle);
        TextView textViewArtist = findViewById(R.id.songartist);
        if (title != null) {
            textViewTitle.setText(title);
            textViewArtist.setText(artist);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            finish();
        } else {
            backToast = Toast.makeText(getBaseContext(), "Πατήστε πίσω ξανά για έξοδο", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        playingSongTitleFetcher.stopRepeatingTask();
        // Kill stream service
        Intent serviceIntent = new Intent(this, StreamService.class);
        stopService(serviceIntent);
    }
}