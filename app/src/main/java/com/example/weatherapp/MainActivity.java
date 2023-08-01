package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    ImageView imageView;
    TextView country, city, temp, latitude, longitude, humadity, sunrise, sunset, pressure, wind, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editTextTextPersonName);
        button = findViewById(R.id.button);
        imageView = findViewById(R.id.imageButton2);
        country = findViewById(R.id.country);
        city = findViewById(R.id.city);
        temp = findViewById(R.id.textView4);
        latitude = findViewById(R.id.Latitude);
        longitude = findViewById(R.id.Longitude);
        humadity = findViewById(R.id.Humadity);
        sunrise = findViewById(R.id.Sunrise);
        sunset = findViewById(R.id.Sunset);
        pressure = findViewById(R.id.Pressure);
        wind = findViewById(R.id.Wind);
        date = findViewById(R.id.date);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findWeather();
            }
        });

    }

    public void findWeather(){
        String city_url = editText.getText().toString();
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+city_url+"&appid=your_api_key&units=metric";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //calling api
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    //find country
                    JSONObject object1 = jsonObject.getJSONObject("sys");
                    String country_find = object1.getString("country");
                    country.setText(country_find);

                    //find city
                    String city_find = jsonObject.getString("name");
                    city.setText(city_find);

                    //find date and time
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat std = new SimpleDateFormat("dd/MM/yyyy \nHH:mm:ss");
                    String date_find = std.format(calendar.getTime());
                    date.setText(date_find);

                    //find temp
                    JSONObject object2 = jsonObject.getJSONObject("main");
                    String temp_find = object2.getString("temp");
                    temp.setText(temp_find + "° C");
//                    double temp_find_cel = Integer.parseInt(temp_find) - 273.15;
//                    temp.setText(temp_find_cel + "°F");

                    //find image
                    JSONArray jsonArray = jsonObject.getJSONArray("weather");
                    JSONObject object3 = jsonArray.getJSONObject(0);
                    String img = object3.getString("icon");

                    Picasso.get().load("https://openweathermap.org/img/wn/"+img+"@2x.png").into(imageView);

                    //find latitude
                    JSONObject object4 = jsonObject.getJSONObject("coord");
                    double latitude_find = object4.getDouble("lat");
                    latitude.setText(latitude_find + "° N");

                    //find longitude
                    double longitude_find = object4.getDouble("lon");
                    longitude.setText(longitude_find + "° E");

                    //find humadity
                    JSONObject object5 = jsonObject.getJSONObject("main");
                    int humadity_find = object5.getInt("humidity");
                    humadity.setText(humadity_find + " %");

                    //find sunrise
                    JSONObject object6 = jsonObject.getJSONObject("sys");
                    String sunrise_find = object6.getString("sunrise");
                    sunrise.setText(sunrise_find);

                    //find sunset
                    String sunset_find = object6.getString("sunset");
                    sunset.setText(sunset_find);

                    //find pressure
                    String pressure_find = object5.getString("pressure");
                    pressure.setText(pressure_find + " hPa");

                    //find windspeed
                    JSONObject object7 = jsonObject.getJSONObject("wind");
                    String wind_find = object7.getString("speed");
                    wind.setText(wind_find + " Km/h");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }
}
