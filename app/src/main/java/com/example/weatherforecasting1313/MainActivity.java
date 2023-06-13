package com.example.weatherforecasting1313;
import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.view.ViewGroup;
import android.widget.GridLayout.LayoutParams;
import android.content.Context;
import android.widget.TimePicker;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class MainActivity extends AppCompatActivity {

    //Zdefiniowanie zmiennych globalnych
    String CITY1;
    TextView addressT, updated_atT, statusT, tempT, temp_minTxt, temp_maxT, sunriseT,sunsetT, windT, pressureT, humidityT;
    EditText CITY;
    RelativeLayout myLayout;
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private ImageButton help_button;

    public DatabaseHelper databaseHelper;

    Button timeButton;
    int hour, minute;
    int year;
    int month;
    int day;

    int selected_year;
    int selected_month;
    int selected_day;
    SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    long timestamp;
    int series_selector;
    int counter;
    int status_visibility=0;
    int pressure_visibility=0;
    int wind_visibility=0;
    int keyboard_mode=0;
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
    ArrayList<String> address_array, updated_at_array, status_array, temp_array, temp_minTxt_array, temp_maxT_array, wind_array, pressure_array, humidity_array, citylist, keyboard_hours;
    int[] myIntArray = {1, 2, 3};
    //ArrayList<String> keyboard_hours;

    String  address_array_temp,updated_at_array_temp,status_array_temp,temp_array_temp,wind_array_temp,pressure_array_temp,humidity_array_temp;
    ArrayList<Item> items = new ArrayList<Item>();
    MyAdapter myAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHelper = new DatabaseHelper(this);
        items = databaseHelper.getItems();
        //Zainicjowanie początkowych wartości zmiennych
        dformat.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
        setContentView(R.layout.activity_main);


        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        myAdapter = new MyAdapter(getApplicationContext(),items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);

        timeButton = findViewById(R.id.timeButton);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);
        timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
        initDatePicker();
        selected_year = cal.get(Calendar.YEAR);
        selected_month = cal.get(Calendar.MONTH);
        selected_month = selected_month + 1;
        selected_day = cal.get(Calendar.DAY_OF_MONTH);
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());
        CITY=findViewById(R.id.city);

        address_array = new ArrayList<>();
        updated_at_array = new ArrayList<>();
        status_array= new ArrayList<>();
        temp_array= new ArrayList<>();
        temp_minTxt_array = new ArrayList<>();
        temp_maxT_array = new ArrayList<>();
        wind_array = new ArrayList<>();
        pressure_array = new ArrayList<>();
        humidity_array = new ArrayList<>();
        keyboard_hours = new ArrayList<>();
        ArrayList<String> citylist = new ArrayList<String>();









    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.overflow_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.fromkeyboard:
                if (item.isChecked()){
                    item.setChecked(false);
                    keyboard_mode=0;
                    timeButton.setEnabled(true);
                }
                else{
                    item.setChecked(true);
                    keyboard_mode=1;
                    timeButton.setEnabled(false);

                }
                break;

            case R.id.help:
            {
                    openDialog();
                }
                break;

        }


        return super.onOptionsItemSelected(item);
    }


    public void updateDatabase(){
        databaseHelper.saveItems(items);
    }
    public void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    //Funkcja konwertująca pixele na dp w celu dopasowania dodawanych elementów do wielkości ekranu
    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    //Funkcja do wybierania czasu (godzina i minuta)
    public void popTimePicker(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
            }
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    //Funkcja zwracająca dzisiejszą datę
    private String getTodaysDate()
    {
         year = cal.get(Calendar.YEAR);
         month = cal.get(Calendar.MONTH);
         month = month + 1;
         day = cal.get(Calendar.DAY_OF_MONTH);
         return makeDateString(day, month, year);
    }



    //Funkcja do wybierania daty
    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
                selected_year=year;
                selected_month=month;
                selected_day=day;
            }
        };

        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()+432000000);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-60000);


    }

    //Funkcja do konwertowania daty z formatu Integer na format String
    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    //Funkcja konwertująca numery oznaczające miesiące na nazwy
    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    //Funkcja wywołująca okno do wybierania daty
    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }

    //Funkcja wywołująca okno do wybierania daty
    public void remove_view(int i)
    {
        datePickerDialog.show();
    }

    //Funkcja dodająca


    //Obsługa przycisku: Odczytanie inputu z text view i wysłanie zapytania do API
    public void run(View view){

        if (keyboard_mode==0) {
            if (counter == 0) {
                String list = CITY.getText().toString();
                String[] list1 = list.split(",");
                List<String> list2 = Arrays.asList(list1);
                ArrayList<String> list3 = new ArrayList<String>(list2);
                //Collections.reverse(list3);
                citylist = list3;
                CITY1 = list3.get(counter);


                new weatherTask().execute(); //wysłanie zapytania do API
            }

        }

        if (keyboard_mode==1) {
            if (counter == 0) {
                keyboard_hours = new ArrayList<>();
                String list = CITY.getText().toString();
                String[] list1 = list.split(",");
                List<String> list2 = Arrays.asList(list1);
                ArrayList<String> list3 = new ArrayList<String>(list2);
                //Collections.reverse(list3);
                citylist = list3;
                for(int i=0;i<list3.size();i++) {
                    keyboard_hours.add(citylist.get(i).replaceAll("\\D+",""));
                    citylist.set(i,citylist.get(i).replaceAll("\\d+",""));
                    list3.set(i,list3.get(i).replaceAll("[0-9]",""));
                    System.out.println(citylist.get(i));
                    System.out.println(keyboard_hours.get(i));
                }
                CITY1 = list3.get(counter);

                new weatherTask_fromkeyboard().execute(); //wysłanie zapytania do API

            }
        }


    }

    //Klasa do wysłania zapytania do api i zapisania informacji w ArrayList'ach
    class weatherTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //Wysłanie zapytania do API
        protected String doInBackground(String args[]) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/forecast?q="+CITY1+"&appid=73cbebdd0322acd49bda6ede059b2b18");
            return response;
        }
        @Override
        protected void onPostExecute(String result) {


            //Uzyskanie zmiennej przedstawiającej obecną godzine minus 5 minut w formacie Unix
            long now_unixTime = (System.currentTimeMillis()- 5*60000) / 1000L;

            //Utworzenie Stringa daty w odpowiednim formacie
            String mkdate = Integer.toString(selected_year)+"-"+Integer.toString(selected_month)+"-"+Integer.toString(selected_day)+" "+Integer.toString(hour)+":"+Integer.toString(minute)+":00";

            try{
                Date date = dformat.parse(mkdate); //Konwersja wprowadzonej daty na formaty Date
                timestamp = date.getTime()/1000; //Konwersja wprowadzonej daty z formatu Date na format Unix
                //System.out.println(Long.toString(timestamp));
            }
            catch (ParseException e){

            }

            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject selected_timeframe_0 = jsonObj.getJSONArray("list").getJSONObject(0);
                String series_dt_0 = selected_timeframe_0.getString("dt");

                JSONObject selected_timeframe_last = jsonObj.getJSONArray("list").getJSONObject(39);
                String series_dt_last = selected_timeframe_last.getString("dt");

                if (timestamp>Long.valueOf(series_dt_last)){
                    String times = dformat.format(new Date(Long.valueOf(series_dt_last)*1000));
                    System.out.println(times);

                }

                if(timestamp < Long.valueOf(series_dt_0) && timestamp > now_unixTime){
                    new weatherTask_current().execute();
                    return;
                }


                for (int i=0 ; i<39; i++){
                    JSONObject selected_timeframe_1 = jsonObj.getJSONArray("list").getJSONObject(i);
                    String series_dt_1 = selected_timeframe_1.getString("dt");

                    JSONObject selected_timeframe_2 = jsonObj.getJSONArray("list").getJSONObject(i+1);
                    String series_dt_2 = selected_timeframe_2.getString("dt");

                    if ( (timestamp>=Long.valueOf(series_dt_1)) && (timestamp<Long.valueOf(series_dt_2)) )
                    {
                      series_selector = i;
                        System.out.println("series: "+Integer.toString(i));

                        System.out.println("Time: "+Long.toString(timestamp));
                        System.out.println("Time1: "+series_dt_1);
                        System.out.println("Time2: "+series_dt_2);
                        break;

                    }
                    else{
                        series_selector = 58;
                    }




                }

                //Wczytanie zestawu danych odpowiadającego wybranej godzinie
                JSONObject selected_timeframe = jsonObj.getJSONArray("list").getJSONObject(series_selector);

                //Wczytanie wybranych danych
                JSONObject main = selected_timeframe.getJSONObject("main");
                JSONObject wind = selected_timeframe.getJSONObject("wind");
                JSONObject weather = selected_timeframe.getJSONArray("weather").getJSONObject(0);
                String updatedAtText = makeDateString(selected_day, selected_month, selected_year)+" "+String.format(Locale.getDefault(), "%02d:%02d",hour, minute);
                String temp = selected_timeframe.getJSONObject("main").getString("temp");
                Double temp_celcious1 = Double.valueOf(temp) - 272.15;
                String temp_celcious =  String.format("%.2f", temp_celcious1) + "°C";
                String pressure =   main.getString("pressure")+"hPa";
                String humidity =  main.getString("humidity")+"%";
                String windSpeed = wind.getString("speed") +"m/s";
                String weatherDescription = weather.getString("description");
                String address = jsonObj.getJSONObject("city").getString("name") + ", " + jsonObj.getJSONObject("city").getString("country");


                items.add(new Item(address,updatedAtText, weatherDescription.toUpperCase(), temp_celcious, pressure, humidity, windSpeed, R.drawable.exit));
                myAdapter.notifyItemInserted(items.size()-1);
                databaseHelper.saveItems(items);

                counter++;
                if(citylist.size()>counter){
                   CITY1=citylist.get(counter);
                    new weatherTask().execute();
                }
                else{counter = 0;}

            }
            catch (JSONException e) {
                counter++;
                if(citylist.size()>counter){
                    CITY1=citylist.get(counter);
                    new weatherTask().execute();

                }
               else{counter = 0;}

            }

        }
    }

    //Klasa do wysłania zapytania do api i zapisania informacji w ArrayList'ach dla czasu z klawiatury
    class weatherTask_fromkeyboard extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //Wysłanie zapytania do API
        protected String doInBackground(String args[]) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/forecast?q="+CITY1+"&appid=73cbebdd0322acd49bda6ede059b2b18");
            return response;
        }
        @Override
        protected void onPostExecute(String result) {


            //Uzyskanie zmiennej przedstawiającej obecną godzine minus 5 minut w formacie Unix
            long now_unixTime = (System.currentTimeMillis()- 5*60000) / 1000L;

            //Utworzenie Stringa daty w odpowiednim formacie yyyy-MM-dd HH:mm:ss
            String keyboard_hour = keyboard_hours.get(counter);
            if (Integer.valueOf(keyboard_hour)>24)
            {
                keyboard_hour = "null";
            }

            System.out.println(keyboard_hour);

            String mkdate = Integer.toString(selected_year)+"-"+Integer.toString(selected_month)+"-"+Integer.toString(selected_day)+" "+keyboard_hour+":01:00";
            System.out.println(mkdate);

            try{
                if(keyboard_hour=="null")
                {
                    return;
                }
                Date date = dformat.parse(mkdate); //Konwersja wprowadzonej daty na formaty Date
                timestamp = date.getTime()/1000; //Konwersja wprowadzonej daty z formatu Date na format Unix
            }
            catch (ParseException e){

            }

            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject selected_timeframe_0 = jsonObj.getJSONArray("list").getJSONObject(0);
                String series_dt_0 = selected_timeframe_0.getString("dt");

                if(timestamp < Long.valueOf(series_dt_0) && timestamp > now_unixTime){
                    new weatherTask_current().execute();
                    return;
                }


                for (int i=0 ; i<39; i++){
                    JSONObject selected_timeframe_1 = jsonObj.getJSONArray("list").getJSONObject(i);
                    String series_dt_1 = selected_timeframe_1.getString("dt");

                    JSONObject selected_timeframe_2 = jsonObj.getJSONArray("list").getJSONObject(i+1);
                    String series_dt_2 = selected_timeframe_2.getString("dt");

                    if ( (timestamp>Long.valueOf(series_dt_1)) && (timestamp<Long.valueOf(series_dt_2)) )
                    {
                        series_selector = i;
                        System.out.println("series: "+Integer.toString(i));

                        System.out.println("Time: "+Long.toString(timestamp));
                        System.out.println("Time1: "+series_dt_1);
                        System.out.println("Time2: "+series_dt_2);
                        break;

                    }
                    else{
                        series_selector = 58;
                    }




                }

                //Wczytanie zestawu danych odpowiadającego wybranej godzinie
                JSONObject selected_timeframe = jsonObj.getJSONArray("list").getJSONObject(series_selector);

                //Wczytanie wybranych danych
                JSONObject main = selected_timeframe.getJSONObject("main");
                JSONObject wind = selected_timeframe.getJSONObject("wind");
                JSONObject weather = selected_timeframe.getJSONArray("weather").getJSONObject(0);
                String updatedAtText = makeDateString(selected_day, selected_month, selected_year)+" "+String.format(Locale.getDefault(), "%02d:%02d",Integer. parseInt(keyboard_hour), 0);
                String temp = selected_timeframe.getJSONObject("main").getString("temp");
                Double temp_celcious1 = Double.valueOf(temp) - 272.15;
                String temp_celcious =   String.format("%.2f", temp_celcious1) + "°C";
                String pressure =  main.getString("pressure")+"hPa";
                String humidity =   main.getString("humidity")+"%";
                String windSpeed =  wind.getString("speed") +"m/s";
                String weatherDescription = weather.getString("description");
                String address = jsonObj.getJSONObject("city").getString("name") + ", " + jsonObj.getJSONObject("city").getString("country");


                items.add(new Item(address,updatedAtText, weatherDescription.toUpperCase(), temp_celcious, pressure, humidity, windSpeed, R.drawable.exit));
                myAdapter.notifyItemInserted(items.size()-1);
                databaseHelper.saveItems(items);

                counter++;
                if(citylist.size()>counter){
                    CITY1=citylist.get(counter);
                    new weatherTask_fromkeyboard().execute();
                }
                else{counter = 0;}

            }
            catch (JSONException e) {
                counter++;
                if(citylist.size()>counter){
                    CITY1=citylist.get(counter);
                    new weatherTask_fromkeyboard().execute();

                }
                else{counter = 0;}



            }

        }
    }

    //Klasa do wysłania zapytania do api i zapisania informacji w ArrayList'ach dla czasu dla którego nie ma jeszcze prognozy
    class weatherTask_current extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.errorText).setVisibility(View.GONE);
            findViewById(R.id.city).setVisibility(View.VISIBLE);
            findViewById(R.id.button2).setVisibility(View.VISIBLE);
        }


        protected String doInBackground(String args[]) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q="+CITY1+"&appid=73cbebdd0322acd49bda6ede059b2b18");
            return response;
        }
        @Override
        protected void onPostExecute(String result) {


            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject sys = jsonObj.getJSONObject("sys");
                JSONObject wind = jsonObj.getJSONObject("wind");
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);
                Long updatedAt = jsonObj.getLong("dt");
                String updatedAtText = makeDateString(selected_day, selected_month, selected_year)+" "+String.format(Locale.getDefault(), "%02d:%02d",hour, minute);
                String temp = main.getString("temp");
                //Double temp_celcious = Double.valueOf(temp) - 272.15;

                Double temp_celcious1 = Double.valueOf(temp) - 272.15;
                String temp_celcious =   String.format("%.2f", temp_celcious1) + "°C";
                String pressure =   main.getString("pressure")+"hPa";
                String humidity =   main.getString("humidity")+"%";
                String windSpeed = wind.getString("speed") +"m/s";
                String weatherDescription = weather.getString("description");
                String address = jsonObj.getString("name") + ", " + sys.getString("country");

                items.add(new Item(address,updatedAtText, weatherDescription.toUpperCase(), temp_celcious, pressure, humidity, windSpeed, R.drawable.exit));
                myAdapter.notifyItemInserted(items.size()-1);
                databaseHelper.saveItems(items);

                counter++;
                if(citylist.size()>counter){
                    CITY1=citylist.get(counter);
                    new weatherTask().execute();
                }
                else{counter = 0;}

            }
            catch (JSONException e) {
                counter++;
                if(citylist.size()>counter){
                    //initialize();
                    CITY1=citylist.get(counter);
                    new weatherTask().execute();

                }
                else{counter = 0;}



            }


        }
    }


}