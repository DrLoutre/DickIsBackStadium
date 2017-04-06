package Models.In;

import Events.TurnEvent;
import Models.BlackBox.BlackBox;
import com.phidgets.PhidgetException;
import com.phidgets.RFIDPhidget;
import com.phidgets.event.*;

/**
 * Created by bri_e on 28-02-17.
 */
public class LapCalculator {

    private static int PHIDGET_SERIAL = 335178;
    private static long MINIMUM_PASSING_TIME_IN_MILLI = 2;

    private RFIDPhidget rfidPhidget;
    private Runners runners;
    private AttachListener attachListener;
    private DetachListener detachListener;
    private ErrorListener errorListener;
    private TagGainListener tagGainListener;
    private TagLossListener tagLossListener;
    private OutputChangeListener outputChangeListener;
    private long timeScanning;
    private BlackBox blackBox;

    public LapCalculator(BlackBox bbx)throws PhidgetException{
        runners = new Runners();
        rfidPhidget = new RFIDPhidget();
        blackBox = bbx;
        setListeners();
        rfidPhidget.addAttachListener(attachListener);
        rfidPhidget.addDetachListener(detachListener);
        rfidPhidget.addErrorListener(errorListener);
        rfidPhidget.addTagGainListener(tagGainListener);
        rfidPhidget.addTagLossListener(tagLossListener);
        rfidPhidget.addOutputChangeListener(outputChangeListener);
        rfidPhidget.open(PHIDGET_SERIAL);
        System.out.println("Waiting Attachment...");
        rfidPhidget.waitForAttachment();
        System.out.println("Attached !");
    }

    private void onScan(String id){
        runners.scanned(id);
        long lastLapTime = runners.getIdPerfs(id).get(runners.getIdLapsNumber(id)-1);
        long lapTime = runners.getIdPerfs(id).getLast();
        System.out.println("Runner id : " + id + " " +
                "\nRunning Time : " + (lapTime-lastLapTime)/1000 +
                "\nNumber of laps : " + runners.getIdLapsNumber(id));
    }

    private void setListeners() throws PhidgetException {

        attachListener = new AttachListener() {
            public void attached(AttachEvent ae)
            {
                try
                {
                    ((RFIDPhidget)ae.getSource()).setAntennaOn(true);
                    ((RFIDPhidget)ae.getSource()).setLEDOn(true);
                }
                catch (PhidgetException ex) { }
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
        tagGainListener = new TagGainListener()
        {
            public void tagGained(TagGainEvent oe)
            {
                System.out.println("Tag Gained: " +oe.getValue() + " (Proto:"+ oe.getProtocol()+")");
                timeScanning = System.currentTimeMillis();
            }
        };
        tagLossListener = new TagLossListener()
        {
            public void tagLost(TagLossEvent oe)
            {
                long clock = System.currentTimeMillis() ;
                System.out.println(oe);
                if(Math.abs(timeScanning - clock) > MINIMUM_PASSING_TIME_IN_MILLI) {
                    onScan(oe.getValue());
                    try {
                        blackBox.processElement(new TurnEvent(clock));
                    } catch (PhidgetException e) {
                        System.out.println("Error while processing event");
                    }
                }
                timeScanning = Long.parseLong("0.00");
            }
        };
        outputChangeListener = new OutputChangeListener()
        {
            public void outputChanged(OutputChangeEvent oe)
            {
                System.out.println(oe);
            }
        };
    }
}
