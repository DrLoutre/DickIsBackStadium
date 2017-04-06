/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package verifications;

import java.util.regex.Pattern;

/**
 *
 * @author Maxime
 */
public class Verif {
    
    String firstName;
    String lastName;
    String old;
    String sex;
    String mdp;
    
    public Verif(String firstName, String lastName, String old, String sex, String mdp) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.old = old;
        this.sex = sex;
        this.mdp = mdp;
    }
    
    public boolean tryTest() {
        return verifSize(firstName) && verifSize(lastName) && verifSize(mdp) && patternInt(old) &&  verifName(firstName) && verifName(lastName);
    }
    
    private boolean verifName(String name) {
        String test = "(?i)(?:(?![×Þß÷þø])[a-zÀ-ÿ\\-\\' ])*";
        boolean matches = Pattern.matches(test, name);
        return matches;
    }
    
    private boolean patternInt(String value) {
        boolean verif = Pattern.matches("[0-9]*", value);
        return verif;
    }
    
    private boolean verifSize(String value) {
        return value.length() <= 30;
    }
}
