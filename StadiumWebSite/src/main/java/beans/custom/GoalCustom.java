package beans.custom;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GoalCustom {

    private boolean isDroit;
    private int nbrGoals;

    public boolean isDroit() {
        return isDroit;
    }

    public void setDroit(boolean droit) {
        isDroit = droit;
    }

    public int getNbrGoals() {
        return nbrGoals;
    }

    public void setNbrGoals(int nbrGoals) {
        this.nbrGoals = nbrGoals;
    }
}
