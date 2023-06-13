package com.example.weatherforecasting1313;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

class DatabaseHelper {
    private SharedPreferences itemsDatabase;
    private Gson gson;
 public DatabaseHelper(Context context) {
        itemsDatabase = context.getSharedPreferences( "taskDatabase",Context.MODE_PRIVATE);
        gson = new Gson();
         }
 public void saveItems(ArrayList<Item> items){
        SharedPreferences.Editor editor = itemsDatabase.edit();
        editor.putString("items",gson.toJson(items));
        editor.apply();
    }
 public ArrayList<Item> getItems(){
    String itemsString = itemsDatabase.getString("items", null);
    Type itemListType = new TypeToken<ArrayList<Item>>(){}.getType();
    ArrayList<Item> items = gson.fromJson(itemsString,itemListType);
if (items!=null) return items;
 else return new ArrayList<>();
}
}
