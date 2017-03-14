package Models.In;

public enum Weather {

    RAIN("Pluvieux"),
    CLOUD("Nuageux"),
    SUN("Dégagé"),
    SNOW("Chutes de neige");

    private String weather;

    Weather(String weather) {
        this.weather = weather;
    }

    public String getWeather() {
        return this.weather;
    }
}
