package com.desertovercrowded.rock1055.Adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.desertovercrowded.rock1055.Producer;
import com.desertovercrowded.rock1055.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProducerAdapter extends RecyclerView.Adapter<ProducerAdapter.MyViewHolder> {

    private final Activity activity;
    ArrayList<Producer> producers = new ArrayList<>();

    public ProducerAdapter(Activity activity, ArrayList<Producer> producers) {
        this.activity = activity;
        this.producers = producers;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_producer_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {

        myViewHolder.tvProducerName.setText(this.producers.get(position).getName());
        myViewHolder.tvProducerDescription.setText(this.producers.get(position).getDescription());
        myViewHolder.tvProducerShow.setText(this.producers.get(position).getShow());
        myViewHolder.tvProducerShowTime.setText(this.producers.get(position).getShowTime());

        String imageUriString = this.producers.get(position).getImage();

        if (imageUriString != null && !imageUriString.isEmpty()) {
            Uri uri = Uri.parse(imageUriString);
            myViewHolder.ivProducerImage.setScaleType(ImageView.ScaleType.FIT_END);
            Picasso.get().load(uri).into(myViewHolder.ivProducerImage);
        }
        final Producer temp = this.producers.get(position);
        myViewHolder.cvProducer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("YOLO", "YOLO");
                ShowMoar(temp);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.producers.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cvProducer;
        TextView tvProducerName, tvProducerDescription, tvProducerShow, tvProducerShowTime;
        ImageView ivProducerImage;

        public MyViewHolder(View view) {
            super(view);
            this.setIsRecyclable(true);
            cvProducer = view.findViewById(R.id.cvProducer);
            tvProducerName = view.findViewById(R.id.tvProducerName);
            tvProducerDescription = view.findViewById(R.id.tvProducerDescription);
            ivProducerImage = view.findViewById(R.id.ivProducerImage);
            tvProducerShow = view.findViewById(R.id.tvProducerShow);
            tvProducerShowTime = view.findViewById(R.id.tvProducerShowTime);
        }
    }

    private void ShowMoar(Producer producer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(producer.getDescription())
                .setTitle(producer.getName())
                .setCancelable(true)
                .setNeutralButton(R.string.exit,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }
                );
        AlertDialog alert = builder.create();
        alert.show();
    }
}