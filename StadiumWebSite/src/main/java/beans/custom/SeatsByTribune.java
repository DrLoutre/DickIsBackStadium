package beans.custom;

import beans.Seat;
import beans.Tribune;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
public class SeatsByTribune {

    private Tribune tribune;
    private ArrayList<Seat> seats;

    public Tribune getTribune() {
        return tribune;
    }

    public void setTribune(Tribune tribune) {
        this.tribune = tribune;
    }

    public ArrayList<Seat> getSeats() {
        return seats;
    }

    public void setSeats(ArrayList<Seat> seats) {
        this.seats = seats;
    }
}
