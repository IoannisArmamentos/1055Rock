package com.desertovercrowded.rock1055;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class Contact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final ImageButton bFacebook = findViewById(R.id.button_Facebook);
        final ImageButton bInstagram = findViewById(R.id.button_Instagram);
        final ImageButton bTwitter = findViewById(R.id.button_Twitter);
        final ImageButton bEmail = findViewById(R.id.button_Email);

        bFacebook.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://www.facebook.com/1055rock/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        bInstagram.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://www.instagram.com/1055_rock/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        bTwitter.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://twitter.com/1055rock");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        bEmail.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:1055rockstudio@gmail.com"));
            try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(Contact.this, getString(R.string.about_not_found_email), Toast.LENGTH_SHORT).show();
            }
            startActivity(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}