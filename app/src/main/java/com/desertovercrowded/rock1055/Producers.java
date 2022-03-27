package com.desertovercrowded.rock1055;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.desertovercrowded.rock1055.adapter.ProducerAdapter;

public class Producers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producers);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ProducerList producers = DataSingleton.getProducerList();

        RecyclerView recyclerView = findViewById(R.id.rvProducers);

        ProducerAdapter adapter = new ProducerAdapter(this, producers.getProducers());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
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