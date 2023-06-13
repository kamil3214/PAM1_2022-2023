package com.example.weatherforecasting1313;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView nameView,updated_atView, statusView, temperatureView, pressureView, humidityView, windView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageview);
        nameView = itemView.findViewById(R.id.name);
        updated_atView = itemView.findViewById(R.id.updated_at);
        statusView = itemView.findViewById(R.id.status);
        temperatureView = itemView.findViewById(R.id.temperature);
        pressureView = itemView.findViewById(R.id.pressure);
        humidityView = itemView.findViewById(R.id.humidity);
        windView = itemView.findViewById(R.id.wind);
    }
}