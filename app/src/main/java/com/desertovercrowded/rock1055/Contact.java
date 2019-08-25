package com.desertovercrowded.rock1055;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

        final Button bFacebook = findViewById(R.id.button_Facebook);
        final Button bInstagram = findViewById(R.id.button_Instagram);
        final Button bTwitter = findViewById(R.id.button_Twitter);
        final Button bEmail = findViewById(R.id.button_Email);

        bFacebook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.facebook.com/1055rock/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        bInstagram.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.instagram.com/1055_rock/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        bTwitter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uri uri = Uri.parse("https://twitter.com/1055rock");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        bEmail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:grammateia@1055rock.gr"));
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.about_email_intent));
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(Contact.this, getString(R.string.about_not_found_email), Toast.LENGTH_SHORT).show();
                }
                startActivity(intent);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}