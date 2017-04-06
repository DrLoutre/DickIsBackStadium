package Models.In;

import Events.StandEvent;
import Models.BlackBox.BlackBox;
import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.InputChangeEvent;
import com.phidgets.event.InputChangeListener;

/**
 * Created by bri_e on 08-03-17.
 */
public class Stand {

    private InterfaceKitPhidget interfaceKitPhidget;
    private BlackBox blackBox;
    private String standName;
    private boolean[] seats;
    private int numberOfSeats;


    public Stand(InterfaceKitPhidget ifk, String name, int nbrSeats,BlackBox bbx){
        standName = name;
        interfaceKitPhidget = ifk;
        blackBox = bbx;
        numberOfSeats = nbrSeats;
        seats = new boolean[numberOfSeats];
        interfaceKitPhidget.addInputChangeListener(new InputChangeListener() {
            @Override
            public void inputChanged(InputChangeEvent inputChangeEvent) {
                if (inputChangeEvent.getState()) {
                    int seatOfConcern = inputChangeEvent.getIndex();
                    if (seats[seatOfConcern]) {
                        seats[seatOfConcern] = false;
                        System.out.println("Siège n°" + seatOfConcern + " libéré dans la tribune " + standName);
                    } else {
                        seats[seatOfConcern] = true;
                        System.out.println("Siège n°" + seatOfConcern + " occupé dans la tribune " + standName);
                    }
                    try {
                        blackBox.processElement(new StandEvent(System.currentTimeMillis()));
                    } catch(PhidgetException e) {
                        System.out.println("Error while processing event");
                    }
                }
            }
        });
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public String getStandName() {
        return standName;
    }

    public boolean[] getSeats() {
        return seats;
    }
}
