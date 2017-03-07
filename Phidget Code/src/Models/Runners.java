package Models;

import sun.awt.image.ImageWatched;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by bri_e on 28-02-17.
 */
public class Runners {

    private LinkedList<String>              idList;
    private LinkedList<Integer>             idNumber;
    private LinkedList<LinkedList<Long>>    time;


    public Runners(){
        idList      = new LinkedList<String>();
        idNumber    = new LinkedList<Integer>();
        time        = new LinkedList<LinkedList<Long>>();
    }

    public void scanned(String id){
        if (idList.contains(id)){
            int index = idList.indexOf(id);
            int newVal = idNumber.get(index);
            LinkedList<Long> perfs = time.get(index);
            perfs.addLast(System.currentTimeMillis());
            newVal++;
            idNumber.set(index, newVal);
            time.set(index,perfs);
        } else {
            LinkedList<Long> tempLinked = new LinkedList<Long>();
            tempLinked.addLast(System.currentTimeMillis());
            idList.addLast(id);
            idNumber.addLast(0);
            time.addLast(tempLinked);
        }
    }

    public void reset(String id){
        if (idList.contains(id)){
            int index = idList.indexOf(id);
            int newVal = idNumber.get(index);
            idNumber.set(index, newVal++);
        } else {
            System.out.println("Nothing to be reset");
        }
    }

    public int getIdLapsNumber(String id){
        if (idList.contains(id)){
            int index = idList.indexOf(id);
            return idNumber.get(index);
        } else {
            return -1;
        }
    }

    public LinkedList<Long> getIdPerfs(String id){
        if (idList.contains(id)){
            int index = idList.indexOf(id);
            return time.get(index);
        } else {
            return null;
        }
    }

}
