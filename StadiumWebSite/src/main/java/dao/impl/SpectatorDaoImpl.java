package dao.impl;

import beans.Spectator;
import core.Assert;
import dao.Dao;
import dao.MatchDao;
import dao.SpectatorDao;
import dao.TribuneDao;
import exceptions.IntegrityException;
import exceptions.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import stade.data.QSpectator;
import stade.data.SpectatorData;

public class SpectatorDaoImpl extends Dao implements SpectatorDao{

    private final TribuneDao tribuneDao;
    private final MatchDao matchDao;
    
    private static final QSpectator SPECTATOR = QSpectator.spectator;
    
    public SpectatorDaoImpl(){
        super();
        tribuneDao = new TribuneDaoImpl();
        matchDao = new MatchDaoImpl();
    }
    
    @Override
    public boolean spectatorExists(int ID) {
        Assert.isTrue(ID >= 0);
        
        SpectatorData data = queryFactory.selectFrom(SPECTATOR)
                .where(SPECTATOR.idSpec.eq(ID))
                .fetchFirst();
        closeConnection();
        
        return(data != null);
    }

    @Override
    public int addSpetator(String lastName, String firstName, 
            String tribuneNFC, int IDMatch) throws NotFoundException {
        Assert.notNull(lastName);
        Assert.isTrue(lastName.length() > 0);
        Assert.notNull(firstName);
        Assert.isTrue(firstName.length() > 0);
        Assert.notNull(tribuneNFC);
        Assert.isTrue(tribuneNFC.length() > 0);
        Assert.isTrue(IDMatch >= 0);
        
        if(!tribuneDao.tribuneExists(tribuneNFC)) throw 
                new NotFoundException("The tribune " + tribuneNFC 
                        + " does not exists in the database");
        if(!matchDao.matchExists(IDMatch)) throw new NotFoundException("The "
                + "match " + IDMatch + " does not exists in the database");
        
        SpectatorData data = toData(lastName, firstName, tribuneNFC, IDMatch);
        int ID = queryFactory.insert(SPECTATOR).populate(data)
                .executeWithKey(SPECTATOR.idSpec);
        closeConnection();
        
        return ID;
    }
    
    @Override
    public Spectator getSpectator(int ID) throws NotFoundException {
        Assert.isTrue(ID >= 0);
        
        SpectatorData data = queryFactory.selectFrom(SPECTATOR)
                .where(SPECTATOR.idSpec.eq(ID))
                .fetchFirst();
        closeConnection();
        
        if(data==null) throw new NotFoundException("The spectator " 
                + ID + " does not exists in the database");
        
        return toSpectator(data);
    }

    @Override
    public void deleteSpectator(int ID) throws NotFoundException {
        Assert.isTrue(ID >= 0);

        if(!spectatorExists(ID)) throw new NotFoundException("Spectator " + ID
                + " has not been found in the database");

        long rows = queryFactory.delete(SPECTATOR)
                .where(SPECTATOR.idSpec.eq(ID)).execute();
        closeConnection();

        Assert.isTrue(rows == 1);
    }

    @Override
    public String getTribune(int ID) throws NotFoundException {
        Assert.isTrue(ID >= 0);
        
        SpectatorData data = queryFactory.selectFrom(SPECTATOR)
                .where(SPECTATOR.idSpec.eq(ID))
                .fetchFirst();
        closeConnection();
        
        if(data == null) throw new NotFoundException("The spectator " 
                + ID + " does not exists in the database");
        
        return data.getTribuneNFC();
    }

    @Override
    public Pair<String, String> getName(int ID) throws NotFoundException {
        Assert.isTrue(ID >= 0);
        
        SpectatorData data = queryFactory.selectFrom(SPECTATOR)
                .where(SPECTATOR.idSpec.eq(ID))
                .fetchFirst();
        closeConnection();
        
        if(data == null) throw new NotFoundException("The spectator " 
                + ID + " does not exists in the database");
        
        return new Pair(data.getFirstName(),data.getLastName());
    }

    @Override
    public int getMatch(int ID) throws NotFoundException {
        Assert.isTrue(ID >= 0);
        
        SpectatorData data = queryFactory.selectFrom(SPECTATOR)
                .where(SPECTATOR.idSpec.eq(ID))
                .fetchFirst();
        closeConnection();
        
        if(data == null) throw new NotFoundException("The spectator " 
                + ID + " does not exists in the database");
        
        return data.getIdMatch();
    }

    @Override
    public ArrayList<Spectator> getAllSpectator() throws NotFoundException {
        List<SpectatorData> datas = queryFactory.select(SPECTATOR)
                .from(SPECTATOR).fetch();
        closeConnection();

        if (datas.isEmpty()) throw new NotFoundException("Spectators has not "
                + "been found in the database");
        
        ArrayList<Spectator> spectators = new ArrayList<>();
        for (SpectatorData spectator : datas) {
            spectators.add(toSpectator(spectator));
        }
                
        return spectators;
    }

    @Override
    public ArrayList<Spectator> getAllSpectator(String tribuneNFC) 
            throws NotFoundException {
        Assert.notNull(tribuneNFC);
        
        ArrayList<Spectator> spectators = getAllSpectator();
        
        ArrayList<Spectator> selectedSpectators = new ArrayList<>();
        for (Spectator spectator : spectators) {
            if(tribuneNFC.equals(spectator.geTribuneNFC())) 
                selectedSpectators.add(spectator);
        }
        
        if (selectedSpectators.isEmpty()) throw new NotFoundException("There "
                + "is no Spectators of Tribune " + tribuneNFC + " in the"
                + " database");
        
        return selectedSpectators;
    }

    @Override
    public ArrayList<Spectator> getAllSpectator(int matchID) 
            throws NotFoundException {
        Assert.isTrue(matchID >= 0);
        
        ArrayList<Spectator> spectators = getAllSpectator();
        
        ArrayList<Spectator> selectedSpectators = new ArrayList<>();
        for (Spectator spectator : spectators) {
            if(matchID == spectator.getIDMatch())
                selectedSpectators.add(spectator);
        }
        
        if (selectedSpectators.isEmpty()) throw new NotFoundException("There "
                + "is no Spectators of the Match " + matchID + " in the"
                + " database");
        
        return selectedSpectators;
    }

    @Override
    public ArrayList<Spectator> getAllSpectator(String tribuneNFC, int matchID) 
            throws NotFoundException {
        Assert.notNull(tribuneNFC);
        Assert.isTrue(matchID >= 0);
        
        ArrayList<Spectator> spectators = getAllSpectator();
        
        ArrayList<Spectator> selectedSpectators = new ArrayList<>();
        for (Spectator spectator : spectators) {
            if(tribuneNFC.equals(spectator.geTribuneNFC()) 
                    && matchID == spectator.getIDMatch()) 
                selectedSpectators.add(spectator);
        }
        
        if (selectedSpectators.isEmpty()) throw new NotFoundException("There "
                + "is no Spectators of the Match " + matchID + " in the "
                + "Tribune " + tribuneNFC + " in the database");
        
        return selectedSpectators;
    }
    
    SpectatorData toData(int ID, String lastName, String firstName, 
            String tribuneNFC, int IDMatch){
        Assert.isTrue(ID >= 0);
        Assert.notNull(lastName);
        Assert.isTrue(lastName.length() > 0);
        Assert.notNull(firstName);
        Assert.isTrue(firstName.length() > 0);
        Assert.notNull(tribuneNFC);
        Assert.isTrue(tribuneNFC.length() > 0);
        Assert.isTrue(IDMatch >= 0);
        
        SpectatorData data = new SpectatorData();
        data.setFirstName(firstName);
        data.setIdMatch(IDMatch);
        data.setIdSpec(ID);
        data.setLastName(lastName);
        data.setTribuneNFC(tribuneNFC);
        return data;
    }
    
    SpectatorData toData(String lastName, String firstName, 
            String tribuneNFC, int IDMatch){
        Assert.notNull(lastName);
        Assert.isTrue(lastName.length() > 0);
        Assert.notNull(firstName);
        Assert.isTrue(firstName.length() > 0);
        Assert.notNull(tribuneNFC);
        Assert.isTrue(tribuneNFC.length() > 0);
        Assert.isTrue(IDMatch >= 0);
        
        SpectatorData data = new SpectatorData();
        data.setFirstName(firstName);
        data.setIdMatch(IDMatch);
        data.setLastName(lastName);
        data.setTribuneNFC(tribuneNFC);
        return data;
    }
    
    private Spectator toSpectator(SpectatorData data){
        Spectator returnValue = new Spectator();
        returnValue.setFirstName(data.getFirstName());
        returnValue.setID(data.getIdSpec());
        returnValue.setIDMatch(data.getIdMatch());
        returnValue.setLastName(data.getLastName());
        returnValue.setTribuneNFC(data.getTribuneNFC());
        
        return returnValue;
    }
}
