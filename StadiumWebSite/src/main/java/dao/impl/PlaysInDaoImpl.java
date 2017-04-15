package dao.impl;

import core.Assert;
import dao.AthleticDao;
import dao.Dao;
import dao.PlaysInDao;
import dao.TeamDao;
import exceptions.IntegrityException;
import exceptions.NotFoundException;
import java.util.LinkedList;
import java.util.List;
import stade.data.PlaysInData;
import stade.data.QPlaysIn;

public class PlaysInDaoImpl extends Dao implements PlaysInDao{

    private final AthleticDao athleticDao;
    private final TeamDao teamDao;
    
    private static final QPlaysIn PLAYS_IN = QPlaysIn.playsIn;
    
    public PlaysInDaoImpl(){
        super();
        athleticDao = new AthleticDaoImpl();
        teamDao = new TeamDaoImpl();
    }
    
    @Override
    public boolean isPlaying(String NFC, int idTeam) {
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        Assert.isTrue(idTeam >= 0);
        
        PlaysInData data = queryFactory.selectFrom(PLAYS_IN)
                .where(PLAYS_IN.nfc.eq(NFC)
                        .and(PLAYS_IN.idTeam.eq(idTeam)))
                .fetchFirst();
        closeConnection();
        
        return (data != null);
    }

    @Override
    public void addEntry(String NFC, int idTeam) 
            throws IntegrityException,NotFoundException {
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        Assert.isTrue(idTeam >= 0);
        
        if(isPlaying(NFC, idTeam)) throw new IntegrityException("The athletics " 
                + NFC + " is already referenced with the team " + idTeam 
                + " in the data base");
        if(!athleticDao.athleticExists(NFC)) throw new NotFoundException("The "
                + "athletics " + NFC + " does not exist in the database");
        if(!teamDao.teamExists(idTeam)) throw new NotFoundException("The team " 
                + idTeam + " does not exist in the database");
        
        PlaysInData data = toData(NFC,idTeam);
        long rows = queryFactory.insert(PLAYS_IN).populate(data).execute();
        closeConnection();
        
        Assert.isTrue(rows == 1);
    }

    @Override
    public int getTeam(String NFC) throws NotFoundException {
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        
        if(!athleticDao.athleticExists(NFC)) throw new NotFoundException("The "
                + "athletics " + NFC + " does not exist in the database");
        
        PlaysInData data = queryFactory.selectFrom(PLAYS_IN)
                .where(PLAYS_IN.nfc.eq(NFC)).fetchFirst();
        closeConnection();
        
        if(data == null) throw new NotFoundException("The athletic " + NFC 
                + " is not referenced in a team");
        else return data.getIdTeam();
    }

    @Override
    public List<String> getPlayers(int idTeam) throws NotFoundException {
        Assert.isTrue(idTeam >= 0);
        
        if(!teamDao.teamExists(idTeam)) throw new NotFoundException("The team " 
                + idTeam + " does not exist in the database");
        
        List<PlaysInData> datas = queryFactory.selectFrom(PLAYS_IN)
                .where(PLAYS_IN.idTeam.eq(idTeam)).fetch();
        closeConnection();
        if(datas == null || datas.isEmpty()) throw new NotFoundException("There"
                + " is no athletics referenced with this team in the database");
        else {
            List<String> NFCList = new LinkedList();
            for(PlaysInData data : datas){
                NFCList.add(data.getNfc());
            }
            return NFCList;
        }
    }
    
    PlaysInData toData(String NFC, int idTeam){
        Assert.notNull(NFC);
        Assert.isTrue(NFC.length() > 0);
        Assert.isTrue(idTeam >= 0);
        
        PlaysInData data = new PlaysInData();
        data.setIdTeam(idTeam);
        data.setNfc(NFC);
        return data;
    }
}
