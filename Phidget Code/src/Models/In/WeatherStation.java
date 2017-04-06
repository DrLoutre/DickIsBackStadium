package Models.In;

import Events.EventType;
import Events.HeatEvent;
import Models.BlackBox.BlackBox;
import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.SensorChangeEvent;
import com.phidgets.event.SensorChangeListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class WeatherStation {

    private static double MILI_INTERVAL = 1000;

    private Weather weather;
    private Date sunset;
    private Date sunrise;
    private static String apiKey = "eff123853edf134ccd86bf1455b57487";
    private double heat;
    private int sensorIndex;
    private InterfaceKitPhidget interfaceKitPhidget;
    private BlackBox blackBox;

    public WeatherStation(InterfaceKitPhidget ifk, int index, BlackBox bbx) throws PhidgetException {
        interfaceKitPhidget = ifk;
        blackBox = bbx;
        sensorIndex = index;
        heat = refreshHeat();
        try {
            refreshSunPhases();
            refreshWeather();
        } catch(JSONException e) {
            System.out.println("Error while listening Weather API");
        }
        interfaceKitPhidget.addSensorChangeListener(new SensorChangeListener() {
            @Override
            public void sensorChanged(SensorChangeEvent sensorChangeEvent) {
                if (sensorChangeEvent.getIndex() == sensorIndex) {
                    double newval = 0;
                    try {
                        newval = refreshHeat();
                    } catch (PhidgetException e) {
                        System.out.println("Error while listening Phidget : " + e);
                    }
                    //System.out.println("new val = " + newval + " vs old val = " + heat);
                    if (Math.abs(newval - heat) > 0.5) {
                        if ((blackBox.noEvent(EventType.HEAT_EVENT))
                                || ((System.currentTimeMillis() - blackBox.getLast(EventType.HEAT_EVENT).removeLast().getTime()) > MILI_INTERVAL)) {
                            heat = newval;
                            try {
                                blackBox.processElement(new HeatEvent(System.currentTimeMillis()));
                            } catch(PhidgetException e) {
                                System.out.println("Error while processing event");
                            }
                        }
                    }
                }
            }
        });
    }

    public double getHeat() {
        return heat;
    }

    public double refreshHeat() throws PhidgetException {
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

    public boolean isDay(){
        try {
            refreshSunPhases();
        } catch(JSONException e) {
            System.out.println("Error while getting Sun Phases");
        }
        Date now = new Date();
        Date sunSet  = this.getSunset();
        Date sunRise = this.getSunrise();
        if (now.getDay() != sunset.getDay()) {
            System.out.println("Error, current day is different from sunset");
        }
        if (now.compareTo(sunRise) > 0) {
            //We're after sunrise.
            if (now.compareTo(sunSet) > 0){
                //We're after sunset
                return false;
            } else {
                //We're before sunset
                return true;
            }
        } else {
            //We're before sunrise
            return false;
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
