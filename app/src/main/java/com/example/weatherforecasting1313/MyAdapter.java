package com.example.weatherforecasting1313;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context context;
    ArrayList<Item> items;

    private SharedPreferences itemsDatabase;

    private Gson gson;

    public MyAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;


    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {

        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull  MyViewHolder holder, int position) {
        holder.nameView.setText(items.get(position).getName());
        holder.updated_atView.setText(items.get(position).getUpdated_at());
        holder.statusView.setText(items.get(position).getStatus());
        holder.temperatureView.setText(items.get(position).getTemperature());
        holder.pressureView.setText(items.get(position).getPressure());
        holder.humidityView.setText(items.get(position).getHumidity());
        holder.windView.setText(items.get(position).getWind());



        holder.imageView.setImageResource(items.get(position).getImage());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.remove(position);
                notifyDataSetChanged();
                itemsDatabase = context.getSharedPreferences( "taskDatabase",Context.MODE_PRIVATE);
                gson = new Gson();;
                SharedPreferences.Editor editor = itemsDatabase.edit();
                editor.putString("items",gson.toJson(items));
                editor.apply();



            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}