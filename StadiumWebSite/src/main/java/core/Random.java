package core;

public class Random {
    
    /**
     * @pre length is > 0
     * @param length the length of the future string
     * @return a string with the given length and compose of alphabetical 
     *         characters
     */
    public static String createRandomAlphaNumericString(int length) {
        Assert.isTrue(length > 1);
        
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder strBuff = new StringBuilder();
        java.util.Random rnd = new java.util.Random();
        while (strBuff.length() < length) {
            int index = (int) (rnd.nextFloat() * chars.length());
            strBuff.append(chars.charAt(index));
        }
        String str = strBuff.toString();
        
        return str;
    }
    
}
