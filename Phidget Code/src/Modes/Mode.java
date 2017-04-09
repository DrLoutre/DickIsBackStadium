package Modes;

/**
 * Created by bri_e on 09-04-17.
 */
public class Mode {

    private boolean  match;
    private ModeType mode;

    public Mode() {
        match = false;
        mode = ModeType.NORMAL_MODE;
    }

    public Mode(ModeType mo, boolean ma){
        match = ma;
        mode  = mo;
    }

    public void setMode(ModeType mo, boolean ma){
        match = ma;
        mode  = mo;
    }

    public void setModeType(ModeType mo) {
        mode = mo;
    }

    public void setMatchMode(boolean ma){
        match = ma;
    }

    public boolean isMatch(){
        return match;
    }

    public ModeType getModeType(){
        return mode;

    }

}
