package Models;

/**
 * Created by bri_e on 08-03-17.
 */
public class Field {

    private boolean isWatering;
    private boolean isHeating;

    public Field(){
        isWatering = false;
        isHeating = false;
    }

    public void setWatering(boolean state){
        isWatering = state;
        if (isWatering) isHeating = false;
    }

    public boolean isWatering(){
        return isWatering;
    }

    public void setHeating(boolean state){
        isHeating = state;
        if (isHeating) isWatering = false;
    }

    public boolean isHeating(){
        return isHeating;
    }
}
