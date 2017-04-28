/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package verifications;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Maxime
 */
public final class VerifMatch {
    
    Date date;
    
    public VerifMatch(String date, String heure) throws ParseException {
        this.date = parseDate(date + " " + heure);
    }
    
    public Boolean tryTest() {
        Date nowDate = new Date();
        return date.after(nowDate);
    }
    
    public Date parseDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd hh:mm");
        return sdf.parse(date);
    } 
}
