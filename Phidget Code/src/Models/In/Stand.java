package Models.In;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.event.InputChangeEvent;
import com.phidgets.event.InputChangeListener;

/**
 * Created by bri_e on 08-03-17.
 */
public class Stand {

    private InterfaceKitPhidget interfaceKitPhidget;
    private String standName;
    private boolean[] seats;
    private int numberOfSeats;


    public Stand(InterfaceKitPhidget ifk, String name, int nbrSeats){
        standName = name;
        interfaceKitPhidget = ifk;
        numberOfSeats = nbrSeats;
        seats = new boolean[numberOfSeats];
        interfaceKitPhidget.addInputChangeListener(new InputChangeListener() {
            @Override
            public void inputChanged(InputChangeEvent inputChangeEvent) {
                int seatOfConcern = inputChangeEvent.getIndex();
                if (seats[seatOfConcern]) {
                    seats[seatOfConcern] = false;
                    System.out.println("Siège n°" + seatOfConcern + " libéré dans la tribune " + standName);
                } else {
                    seats[seatOfConcern] = true;
                    System.out.println("Siège n°" + seatOfConcern + " occupé dans la tribune " + standName);
                }
            }
        });
    }





}
