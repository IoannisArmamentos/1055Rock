package com.desertovercrowded.rock1055;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

import androidx.core.content.ContextCompat;

public class PlayPauseButtonListener implements View.OnClickListener {

    private boolean streams;

    @Override
    public void onClick(View view) {
        Intent serviceIntent = new Intent(view.getContext(), StreamService.class);
        if (!streams) {
            ContextCompat.startForegroundService(view.getContext(), serviceIntent);
            ((ImageButton) view).setImageResource(R.drawable.ic_pause_circle_outline_black_48dp);
            streams = true;
        } else {
            view.getContext().stopService(serviceIntent);
            ((ImageButton) view).setImageResource(R.drawable.ic_play_circle_outline_black_48dp);
            streams = false;
        }
    }
}