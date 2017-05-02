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
 * Permet de vérifier que les informations rentrées au moment de l'inscription sont correctes.
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
    
    /**
     * Constructeur par défaut de la classe.
     * 
     * @param firstName : il s'agit du prénom de l'athlète.
     * @param lastName : il s'agit du Nom de l'athlète.
     * @param old : il s'agit de l'âge de l'athlète.
     * @param sex : il s'agit du sexe de l'athlète dans le cas d'un homme "h" et dans l'autre cas "f".
     * @param mdp : il s'agit du mot de passe de l'athlète.
     */
    public VerifName(String firstName, String lastName, String old, String sex, String mdp) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.old = old;
        this.sex = sex;
        this.mdp = mdp;
    }
    
    /**
     * Permet de vérifier que la date et l'heure rentrée se déroulent bien après la date et l'heure 
     * actuelle.
     * 
     * @return : Si les informations sont correctes, la valeur true est retournée sinon on retourne false.
     */
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
    
    /**
     * Permet de vérifier que la valeur rentrée vérifier bien le pattern suivant : 
     * "(?i)(?:(?![×Þß÷þø])[a-zÀ-ÿ\\-\\' ])*"
     * 
     * @param name : String qu'il va falloir tester.
     * @return : true si le String respecte bien le pattern, false sinon.
     */
    private boolean verifName(String name) {
        String test = "(?i)(?:(?![×Þß÷þø])[a-zÀ-ÿ\\-\\' ])*";
        boolean matches = Pattern.matches(test, name);
        return matches;
    }
    
    /**
     * Permet de vérifier que la valeur rentrée vérifie bien le pattern suivant : 
     * [0-9]*
     * 
     * @param value : String qu'il va falloir tester.
     * @return true si le String respecte bien le pattern, false sinon.
     */
    private boolean patternInt(String value) {
        boolean verif = Pattern.matches("[0-9]*", value);
        return verif;
    }
    
    /**
     * Permet de vérifier que le String passée en paramètre est bien inférieur ou égal à une longueur de 30.
     * 
     * @param value : String qu'il va falloir tester.
     * @return true si le String est bien inférieur ou égal à un longeur de 30, false sinon.
     */
    private boolean verifSize(String value) {
        return value.length() <= 30;
    }
    
    /**
     * Permet de retourner la Hmap qui contient l'ensemble des valeurs correctes.
     * 
     * @return la Hmap qui contient l'ensemble des valeurs correctes.
     */
    public Map<String, String> getValue(){
        return value;
    }
}
