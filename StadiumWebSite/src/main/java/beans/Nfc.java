package beans;

import dao.AthleticDao;
import dao.impl.AthleticDaoImpl;
import exceptions.NotFoundException;
import java.util.ArrayList;

public class Nfc {
    
    private ArrayList<String> nfc;
    
    public Nfc() {
        nfc = new ArrayList();
        nfc.add("2800b92754");
        nfc.add("5c005c8aeb");
        nfc.add("1f00d061c8");
        nfc.add("70008273bc");
        nfc.add("7000825b24");
        nfc.add("5c005c9229");
        nfc.add("2800494ced");
        nfc.add("5c005e6e2e");
    }
    
    public String getNfc() {
        while(true) {
            int nombreAleatoire = (int)(Math.random() * (nfc.size() + 1));
            AthleticDao athleticDao = new AthleticDaoImpl();
            try {
                athleticDao.getAthletic(nfc.get(nombreAleatoire));
            } catch (NotFoundException e) {
                return nfc.get(nombreAleatoire);
            }
        }
    }
}
