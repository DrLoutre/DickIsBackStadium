package beans.custom;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LapCustom {

    private String rfid;
    private int nbrLaps;
    private String temps;

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public int getNbrLaps() {
        return nbrLaps;
    }

    public void setNbrLaps(int nbrLaps) {
        this.nbrLaps = nbrLaps;
    }

    public String getTemps() {
        return temps;
    }

    public void setTemps(String temps) {
        this.temps = temps;
    }
}
