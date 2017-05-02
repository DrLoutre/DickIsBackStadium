package dao.impl;

import beans.Athletic;
import com.querydsl.sql.dml.SQLUpdateClause;
import core.Assert;
import static core.Random.createRandomAlphaNumericString;
import dao.AthleticDao;
import dao.Dao;
import exceptions.FailureException;
import exceptions.IntegrityException;
import exceptions.NotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import stade.data.AthleticData;
import stade.data.QAthletic;

public class AthleticDaoImpl extends Dao implements AthleticDao {
    
    private static final QAthletic ATHLETIC = QAthletic.athletic;

    @Override
    public void addAthletic(String NFC, String firstName, String lastName, 
            int age, String sex, String password) throws IntegrityException {
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        Assert.notNull(firstName);
        Assert.isTrue(firstName.length() > 0);
        Assert.notNull(lastName);
        Assert.isTrue(lastName.length() > 0);
        Assert.isTrue(age >= 0);
        Assert.notNull(password);
        Assert.isTrue(password.length() > 0);
        Assert.notNull(sex);
        Assert.isTrue(sex.length() == 1);
        
        if (athleticExists(NFC)) throw new IntegrityException("An athletic "
                + "already exists in the database with the NFC id : " + NFC);
        
        String salt = createRandomAlphaNumericString(30);
        
        AthleticData data = toData(NFC, firstName, lastName, age, sex, password,
                salt);
        long rows = queryFactory.insert(ATHLETIC).populate(data).execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
    }
    
    @Override
    public Athletic getAthletic(String NFC) throws NotFoundException {
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        
        AthleticData data = queryFactory.selectFrom(ATHLETIC)
                .where(ATHLETIC.nfc.eq(NFC)).fetchFirst();
        closeConnection();
        
        if(data == null) throw new NotFoundException("Athletic " + NFC 
                + " has not been found in the database");
        
        return toAthletic(data);
    }
    
    @Override
    public boolean athleticExists(String NFC) {
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        
        AthleticData data = queryFactory.selectFrom(ATHLETIC)
                .where(ATHLETIC.nfc.eq(NFC)).fetchFirst();
        closeConnection();
        
        return (data != null);
    }

    @Override
    public String getFirstName(String NFC) throws NotFoundException {
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        
        if(!athleticExists(NFC)) throw new NotFoundException("Athletic " + NFC 
                + " has not been found in the database");
        
        AthleticData data = queryFactory.selectFrom(ATHLETIC)
                .where(ATHLETIC.nfc.eq(NFC)).fetchFirst();
        closeConnection();
        
        return data.getFirstName();
    }

    @Override
    public String getLastName(String NFC) throws NotFoundException {
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        
        if(!athleticExists(NFC)) throw new NotFoundException("Athletic " + NFC 
                + " has not been found in the database");
        
        AthleticData data = queryFactory.selectFrom(ATHLETIC)
                .where(ATHLETIC.nfc.eq(NFC)).fetchFirst();
        closeConnection();
        
        return data.getLastName();
    }

    @Override
    public String getHashedPassword(String NFC) throws NotFoundException {
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        
        if(!athleticExists(NFC)) throw new NotFoundException("Athletic " + NFC 
                + " has not been found in the database");
        
        AthleticData data = queryFactory.selectFrom(ATHLETIC)
                .where(ATHLETIC.nfc.eq(NFC)).fetchFirst();
        closeConnection();
        
        return data.getPassword();
    }

    @Override
    public void deleteAthletic(String NFC) throws NotFoundException {
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);

        if(!athleticExists(NFC)) throw new NotFoundException("Athletic " + NFC
                + " has not been found in the database");

        long rows = queryFactory.delete(ATHLETIC)
                .where(ATHLETIC.nfc.eq(NFC)).execute();
        closeConnection();

        Assert.isTrue(rows == 1);
    }

    @Override
    public ArrayList<Athletic> getAllAthletic() throws NotFoundException {
        List<AthleticData> datas = queryFactory.select(ATHLETIC).from(ATHLETIC)
                .fetch();
        closeConnection();

        if (datas.isEmpty()) throw new NotFoundException("Athletics has not "
                + "been found in the database");
        
        ArrayList<Athletic> athletics = new ArrayList<>();
        for (AthleticData athletic : datas) {
            athletics.add(toAthletic(athletic));
        }
                
        return athletics;
    }

    @Override
    public void setFirstName(String NFC, String firstName) 
            throws NotFoundException {
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        Assert.notNull(firstName);
        Assert.isTrue(firstName.length() > 0);
        
        Athletic athletic = getAthletic(NFC);
        athletic.setPrenom(firstName);
        AthleticData data = toData(athletic);
        SQLUpdateClause update = queryFactory.update(ATHLETIC);
        
        long rows = update.set(ATHLETIC, data).where(ATHLETIC.nfc.eq(NFC))
                .execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
    }

    @Override
    public void setLastName(String NFC, String lastName) 
            throws NotFoundException {
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        Assert.notNull(lastName);
        Assert.isTrue(lastName.length() > 0);
        
        Athletic athletic = getAthletic(NFC);
        athletic.setNom(lastName);
        AthleticData data = toData(athletic);
        SQLUpdateClause update = queryFactory.update(ATHLETIC);
        
        long rows = update.set(ATHLETIC, data).where(ATHLETIC.nfc.eq(NFC))
                .execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
    }

    @Override
    public void setPassword(String NFC, String password) 
            throws NotFoundException {
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        Assert.notNull(password);
        Assert.isTrue(password.length() > 0);
        
        AthleticData data = queryFactory.selectFrom(ATHLETIC)
                .where(ATHLETIC.nfc.eq(NFC)).fetchFirst();
        closeConnection();
        
        if(data == null) throw new NotFoundException("Athletic " + NFC 
                + " has not been found in the database");
        
        String hashedPassword = hashText(password+data.getSalt());
        data.setPassword(hashedPassword);
        
        SQLUpdateClause update = queryFactory.update(ATHLETIC);
        
        long rows = update.set(ATHLETIC, data).where(ATHLETIC.nfc.eq(NFC))
                .execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
    }

    @Override
    public void setAge(String NFC, int age) throws NotFoundException {
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        Assert.isTrue(age <= 0);
        
        Athletic athletic = getAthletic(NFC);
        athletic.setAge(age);
        AthleticData data = toData(athletic);
        SQLUpdateClause update = queryFactory.update(ATHLETIC);
        
        long rows = update.set(ATHLETIC, data).where(ATHLETIC.nfc.eq(NFC))
                .execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
    }

    @Override
    public void setSex(String NFC, String sex) throws NotFoundException {
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        Assert.notNull(sex);
        Assert.isTrue(sex.length() == 1);
        
        Athletic athletic = getAthletic(NFC);
        athletic.setSex(sex);
        AthleticData data = toData(athletic);
        SQLUpdateClause update = queryFactory.update(ATHLETIC);
        
        long rows = update.set(ATHLETIC, data).where(ATHLETIC.nfc.eq(NFC))
                .execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
    }

    @Override
    public boolean connect(String NFC, String password) {
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        Assert.notNull(password);
        Assert.isTrue(password.length() == 1);
        
        AthleticData data = queryFactory.selectFrom(ATHLETIC)
                .where(ATHLETIC.nfc.eq(NFC)).fetchFirst();
        closeConnection();
        
        if(data == null) return false;
        
        String hashedPassword = hashText(password+data.getSalt());
        return password.equals(hashedPassword);
    }
    
    private AthleticData toData(Athletic athletic){
        return toData(athletic.getNFC(), athletic.getPrenom(), 
                athletic.getNom(), athletic.getAge(), athletic.getSex(), 
                athletic.getMDP(), createRandomAlphaNumericString(30));
    }
    
    private AthleticData toData(String NFC, String firstName, String lastName, 
            int age, String sex, String password, String salt){
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        Assert.notNull(firstName);
        Assert.isTrue(firstName.length() > 0);
        Assert.notNull(lastName);
        Assert.isTrue(lastName.length() > 0);
        Assert.isTrue(age >= 0);
        Assert.notNull(password);
        Assert.isTrue(password.length() > 0);
        Assert.notNull(sex);
        Assert.isTrue(sex.length() == 1);
        Assert.notNull(salt);
        Assert.isTrue(salt.length() > 0);
        
        String hashedPassword = hashText(password+salt);
        
        AthleticData data = new AthleticData();
        data.setNfc(NFC);
        data.setFirstName(firstName);
        data.setLastName(lastName);
        data.setAge(age);
        data.setPassword(hashedPassword);
        data.setSex(sex);
        data.setSalt(salt);
        
        return data;
    }
    
    private Athletic toAthletic(AthleticData data){
        Athletic returnValue = new Athletic();
        returnValue.setAge(data.getAge());
        returnValue.setMDP(data.getPassword());
        returnValue.setNFC(data.getNfc());
        returnValue.setNom(data.getLastName());
        returnValue.setPrenom(data.getFirstName());
        returnValue.setSex(data.getSex());
        
        return returnValue;
    }
    
    private final int NB_HASH = 50;
    
    /**
     * @param data the data that have to be converted
     * @return the string from the given byte array
     */
    private String convertByteToHex(byte data[]){
        StringBuilder hexData = new StringBuilder();
        for (int byteIndex = 0; byteIndex < data.length; byteIndex++)
            hexData.append(
                    Integer.toString((data[byteIndex] & 0xff) + 0x100, 16)
                            .substring(1));
        
        return hexData.toString();
    }
    
    /**
     * @pre textToHash is not null
     * @param textToHash the text that will be hashed
     * @return the hashed text (hashed several times)
     */
    private String hashText(String textToHash)
    {
        for(int i=0;i<NB_HASH;i++){
            MessageDigest sha512;
            try {
                sha512 = MessageDigest.getInstance("SHA-512");
                sha512.update(textToHash.getBytes());

                textToHash = convertByteToHex(sha512.digest());
            } catch (NoSuchAlgorithmException e) {
                throw new FailureException("This algorithm doesn't exist");
            }
        }
        return textToHash;
    }
}
