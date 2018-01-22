package com.example.piotr.pogoda;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


public class City extends MainActivity {

    private TextView cityField;
    private TextView detailsField;
    private TextView currentTemperatureField;
    private TextView humidity_field;
    private TextView pressure_field;
    private TextView weatherIcon;
    private TextView updatedField;
    private static final String OPEN_WEATHER_MAP_URL =
            "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric&lang=pl";
    private static final String OPEN_WEATHER_MAP_API = "d6976893bb490b2769ceef815f3e813f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        Typeface fontWeather = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/weathericons-regular-webfont.ttf");
        cityField = findViewById(R.id.city_field);
        updatedField = findViewById(R.id.updated_field);
        detailsField = findViewById(R.id.details_field);
        currentTemperatureField = findViewById(R.id.current_temperature_field);
        humidity_field = findViewById(R.id.humidity_field);
        pressure_field = findViewById(R.id.pressure_field);
        weatherIcon = findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(fontWeather);

        placeIdTask asyncTask = new placeIdTask(new AsyncResponse() {

            @SuppressLint("SetTextI18n")
            public void processFinish(String weather_city, String weather_description,
                                      String weather_temperature, String weather_humidity,
                                      String weather_pressure, String weather_updatedOn,
                                      String weather_iconText) {

                cityField.setText(weather_city);
                updatedField.setText(weather_updatedOn);
                detailsField.setText(weather_description);
                currentTemperatureField.setText(weather_temperature);
                humidity_field.setText(getString(R.string.humidity) + weather_humidity);
                pressure_field.setText(getString(R.string.pressure) + weather_pressure);
                weatherIcon.setText(Html.fromHtml(weather_iconText));
            }
        });
        asyncTask.execute(MainActivity.getWidth(), MainActivity.getHeight());
    }

    @SuppressWarnings("unused")
    public void get_city_list(View view) {
        Intent intent = new Intent(City.this, CityList.class);
        startActivity(intent);
    }

    private String setWeatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId / 100;
        String icon = "";
        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        if (actualId == 800) {
            long currentTime = new Date().getTime();
            if (currentTime >= sunrise && currentTime < sunset) {
                linearLayout.setBackground(this.getResources().getDrawable(R.mipmap.sunny));
                icon = "&#xf00d;";
            } else {
                linearLayout.setBackground(this.getResources().getDrawable(R.mipmap.notclouds));
                icon = "&#xf02e;";
            }
        } else {
            switch (id) {
                case 2:
                    linearLayout.setBackground(this.getResources().getDrawable(R.mipmap.storm));
                    icon = "&#xf01e;";
                    break;
                case 3:
                    linearLayout.setBackground(this.getResources().getDrawable(R.mipmap.rain));
                    icon = "&#xf01c;";
                    break;
                case 7:
                    linearLayout.setBackground(this.getResources().getDrawable(R.mipmap.fog));
                    icon = "&#xf014;";
                    break;
                case 8:
                    linearLayout.setBackground(this.getResources().getDrawable(R.mipmap.clouds));
                    icon = "&#xf013;";
                    break;
                case 6:
                    linearLayout.setBackground(this.getResources().getDrawable(R.mipmap.snow));
                    icon = "&#xf01b;";
                    break;
                case 5:
                    linearLayout.setBackground(this.getResources().getDrawable(R.mipmap.heavy_rain));
                    icon = "&#xf019;";
                    break;
            }
        }
        return icon;
    }

    public interface AsyncResponse {

        void processFinish(String output1, String output2, String output3,
                           String output4, String output5, String output6,
                           String output7);
    }

    @SuppressLint("StaticFieldLeak")
    public class placeIdTask extends AsyncTask<String, Void, JSONObject> {

        public AsyncResponse delegate = null;

        public placeIdTask(AsyncResponse asyncResponse) {
            delegate = asyncResponse;
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            JSONObject jsonWeather = null;
            try {
                jsonWeather = getWeatherJSON(params[0], params[1]);
            } catch (Exception e) {
                Log.d("Error", "Cannot process JSON results", e);
            }
            return jsonWeather;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if (json != null) {
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = json.getJSONObject("main");
                    DateFormat df = DateFormat.getDateTimeInstance();

                    String city = json.getString("name").toUpperCase(Locale.US) + ", " +
                            json.getJSONObject("sys").getString("country");
                    String description = details.getString("description").toUpperCase(Locale.US);
                    @SuppressLint("DefaultLocale") String temperature = String.format("%.2f", main.getDouble("temp")) + "°";
                    String humidity = main.getString("humidity") + "%";
                    String pressure = main.getString("pressure") + " hPa";
                    String updatedOn = df.format(new Date(json.getLong("dt") * 1000));
                    String iconText = setWeatherIcon(details.getInt("id"),
                            json.getJSONObject("sys").getLong("sunrise") * 1000,
                            json.getJSONObject("sys").getLong("sunset") * 1000);

                    delegate.processFinish(city, description, temperature, humidity, pressure,
                            updatedOn, iconText);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static JSONObject getWeatherJSON(String lat, String lon) {
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_URL, lat, lon));
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            connection.addRequestProperty("x-api-key", OPEN_WEATHER_MAP_API);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuilder json = new StringBuilder(1024);
            String tmp;
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            if (data.getInt("cod") != 200) {
                return null;
            }

            return data;
        } catch (Exception e) {
            return null;
        }
    }
}
