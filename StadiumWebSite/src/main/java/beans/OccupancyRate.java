package beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OccupancyRate {

    private double free;
    private double occupied;

    public OccupancyRate(double free, double occupied) {
        this.free = free;
        this.occupied = occupied;
    }

    public double getFree() {
        return free;
    }

    public void setFree(double free) {
        this.free = free;
    }

    public double getOccupied() {
        return occupied;
    }

    public void setOccupied(double occupied) {
        this.occupied = occupied;
    }
}
