package beans.custom;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SeatsCustom {

    private int standID;
    private boolean[] occupArray;

    public int getStandID() {
        return standID;
    }

    public void setStandID(int standID) {
        this.standID = standID;
    }

    public boolean[] getOccupArray() {
        return occupArray;
    }

    public void setOccupArray(boolean[] occupArray) {
        this.occupArray = occupArray;
    }
}
