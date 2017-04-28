/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package verifications;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 * @author Maxime
 */
public class VerifName {
    
    Map<String, String> value = new HashMap<>();
    String firstName;
    String lastName;
    String old;
    String sex;
    String mdp;
    
    public VerifName(String firstName, String lastName, String old, String sex, String mdp) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.old = old;
        this.sex = sex;
        this.mdp = mdp;
    }
    
    public boolean tryTest() {
        boolean result = true;
        if (verifSize(firstName) && verifName(firstName)) {
            value.put("Prenom", firstName);
        } else {
            result = false;
        }
        if (verifSize(lastName) && verifName(lastName)) {
            value.put("Nom", lastName);
        } else {
            result = false;
        }
        if (verifSize(mdp) && mdp.length() >= 5) {
            value.put("MDP", mdp);
        } else {
            result = false;
        }
        if (patternInt(old)) {
            value.put("Age", old);
        } else {
            result = false;
        }
        return result;
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
    
    public Map<String, String> getValue(){
        return value;
    }
}
