package Models;

import com.phidgets.AdvancedServoPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.*;

/**
 * Created by bri_e on 21-02-17.
 */
public class Roof {

    private static int SERVO_SERIAL = 305832;
    private static int SERVO_INDEX  = 0;
    private static double VAL_ROOF_CLOSED = 15.00;
    private static double VAL_ROOF_OPEN = 220.00;


    private boolean open;
    private AdvancedServoPhidget servo;
    private AttachListener attachListener;
    private DetachListener detachListener;
    private ErrorListener errorListener;
    private ServoPositionChangeListener servoPositionChangeListener;


    public Roof() throws PhidgetException{
        servo = new AdvancedServoPhidget();
        setListeners();
        servo.addAttachListener(attachListener);
        servo.addDetachListener(detachListener);
        servo.addErrorListener(errorListener);
        servo.addServoPositionChangeListener(servoPositionChangeListener);
        servo.open(SERVO_SERIAL);
        System.out.println("Waiting Attachment");
        servo.waitForAttachment();
        servo.setEngaged(SERVO_INDEX,true);
        servo.setPosition(SERVO_INDEX, VAL_ROOF_CLOSED);
        open = false;
    }

    public boolean isOpen(){
        return open;
    }

    public void actionateRoofMotor()throws PhidgetException{
        if (open) {
            servo.setPosition(SERVO_INDEX, VAL_ROOF_CLOSED);
            open = false;
        } else {
            servo.setPosition(SERVO_INDEX, VAL_ROOF_OPEN);
            open = true;
        }
    }

    private void setListeners() {

        attachListener = new AttachListener() {

            public void attached(AttachEvent ae) {
                System.out.println("attachment of " + ae);
            }
        };

        detachListener = new DetachListener() {
            public void detached(DetachEvent ae) {
                System.out.println("detachment of " + ae);
            }
        };

        errorListener = new ErrorListener() {
            public void error(ErrorEvent ee) {
                System.out.println("error event for " + ee);
            }
        };

        servoPositionChangeListener = new ServoPositionChangeListener()
        {
            public void servoPositionChanged(ServoPositionChangeEvent oe)
            {
                System.out.println(oe);
            }
        };
    }



}
