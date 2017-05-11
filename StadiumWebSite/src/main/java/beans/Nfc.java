package beans;

import dao.AthleticDao;
import dao.impl.AthleticDaoImpl;
import exceptions.NotFoundException;
import java.util.ArrayList;

/**
 * Class qui permet de gérer l'attribution des différents NFC.
 * @author Maxime
 */
public class Nfc {
    
    private ArrayList<String> nfc;
    
    /**
     * Il s'agit du constructeur par défaut de la class.
     */
    public Nfc() {
        nfc = new ArrayList();
        nfc.add("2800b92754");
        nfc.add("1f00d061c8");
        nfc.add("70008273bc");
        nfc.add("7000825b24");
        nfc.add("5c005e6e2e");
    }
    
    /**
     * Renvoie un NFC qui sera attribué à l'athlète.
     * 
     * @return un Nfc différent de "5c005c9229" si celui est déjà dans la BD
     */
    public String getNfc() {
        AthleticDao athleticDao = new AthleticDaoImpl();
        try {
            athleticDao.getAthletic("5c005c9229");
            while(true) {
                int nombreAleatoire = (int)(Math.random() * nfc.size());
                try {
                    athleticDao.getAthletic(nfc.get(nombreAleatoire));
                } catch (NotFoundException e) {
                    return nfc.get(nombreAleatoire);
                }
            }           
        } catch (NotFoundException e) {
            return "5c005c9229";
        }
    }
}
