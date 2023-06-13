package com.example.weatherforecasting1313;

public class Item {

    String name;
    String updated_at;
    String status;
    String temperature;
    String pressure;
    String humidity;
    String wind;
    int image;

    public Item(String name, String updated_at, String status, String temperature, String pressure, String humidity, String wind, int image) {
        this.name = name;
        this.updated_at = updated_at;
        this.status = status;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.wind = wind;
        this.image = image;

    }

    public String getName() {
        return name;
    }
    public String getUpdated_at() {
        return updated_at;
    }
    public String getStatus() { return status; }
    public String getTemperature() { return temperature; }

    public String getPressure() { return pressure; }

    public String getHumidity() { return humidity; }

    public String getWind() { return wind; }




    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
