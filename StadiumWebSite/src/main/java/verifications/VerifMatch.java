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
 * Class qui permet de vérifier si la date pour le match est correcte,
 * c'est-à-dire qu'elle n'est pas encore passé.
 *  
 * @author Maxime
 */
public final class VerifMatch {
    
    Date date;
    
    /**
     * Constructeur de la class.
     * 
     * @param date : date rentrée pour le match.
     * @param heure : heure rentrée pour le match.
     * @throws ParseException : erreur qui se déclenche si la date ne peut pas être parser.
     */
    public VerifMatch(String date, String heure) throws ParseException {
        this.date = parseDate(date + " " + heure);
    }
    
    /**
     * Permet de vérifier que la date et l'heure rentrée se déroulent bien après la date et l'heure 
     * actuelle.
     * 
     * @return : si la date et l'heure se déroulent après, false sinon.
     */
    public Boolean tryTest() {
        Date nowDate = new Date();
        return date.after(nowDate);
    }
    
    /**
     * Permet de parser le string en un format Date.
     * 
     * @param date : Il s'agit de la date sous forme d'un string qui a été rentré par l'utilisateur.
     * @return : la date rentrée par l'utilisateur sous forme d'un type Date
     * @throws ParseException : erreur qui se déclenche si la date ne peut pas être parser.
     */
    private Date parseDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd hh:mm");
        return sdf.parse(date);
    } 
}
