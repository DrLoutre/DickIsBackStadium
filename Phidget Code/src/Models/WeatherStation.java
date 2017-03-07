package Models;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class WeatherStation {

    private Weather weather;
    private Date sunset;
    private Date sunrise;
    private static String apiKey = "eff123853edf134ccd86bf1455b57487";
    private double heat;
    private int sensorIndex;
    private InterfaceKitPhidget interfaceKitPhidget;

    public WeatherStation(InterfaceKitPhidget ifk, int index) throws PhidgetException {
        interfaceKitPhidget = ifk;
        sensorIndex = index;
        heat = retHeat();
    }

    public double refreshHeat() throws PhidgetException {
        heat = retHeat();
        return heat;
    }


    private double retHeat() throws PhidgetException {
        int sensorvalue = interfaceKitPhidget.getSensorValue(sensorIndex);
        double roomtemp = (sensorvalue * 0.22222) - 61.11;
        return roomtemp;
    }

    public void refreshSunPhases() throws JSONException {
        JSONObject sun = getWeatherInformation();

        if (sun != null) {
            sun = sun.getJSONObject("sys");

            //3600 : UTC to UTC+1
            this.sunrise = new Date(sun.getInt("sunrise") * 1000L + 3600);
            this.sunset = new Date(sun.getInt("sunset") * 1000L + 3600);
        }
    }

    public void refreshWeather() throws JSONException {
        JSONObject weatherInfos = getWeatherInformation();

        if (weatherInfos != null) {

            String weather = weatherInfos.getJSONArray("weather").getJSONObject(0).getString("main");

            System.out.println(weather.toString());

            switch (weather.toLowerCase()) {
                case "rain" :
                    this.weather = Weather.RAIN;
                    break;
                case "clouds" :
                    this.weather = Weather.CLOUD;
                    break;
                case "clear" :
                    this.weather = Weather.SUN;
                    break;
                case "snow" :
                    this.weather = Weather.SNOW;
                    break;
            }
        }
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public Date getSunset() {
        return this.sunset;
    }

    public void setSunset(Date sunset) {
        this.sunset = sunset;
    }

    public Date getSunrise() {
        return this.sunrise;
    }

    public void setSunrise(Date sunrise) {
        this.sunrise = sunrise;
    }

    private JSONObject getWeatherInformation() throws JSONException {

        JSONObject rootObject = null;

        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?id=2790472&APPID=" + apiKey);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("accept", "application/json");

            if (httpURLConnection.getResponseCode() != 200) {
                throw new RuntimeException("Http Get method failed " + httpURLConnection.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            StringBuilder buffer = new StringBuilder();
            String temp;
            while ((temp = br.readLine()) != null) {
                buffer.append(temp);
            }

            httpURLConnection.disconnect();

            rootObject = new JSONObject(buffer.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return rootObject;
    }

}
