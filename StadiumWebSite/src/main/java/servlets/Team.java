/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.Match;
import dao.AthleticDao;
import dao.MatchDao;
import dao.PlaysInDao;
import dao.TeamDao;
import dao.impl.AthleticDaoImpl;
import dao.impl.MatchDaoImpl;
import dao.impl.PlaysInDaoImpl;
import dao.impl.TeamDaoImpl;
import exceptions.IntegrityException;
import exceptions.NotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Maxime
 */
public class Team extends HttpServlet {
     
    int indexmatch;
    int indexteam1;
    int indexteam2;
    boolean nfc;
    HashMap<String, String> team;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        initVar(request, response);
        this.getServletContext().getRequestDispatcher("/WEB-INF/Team.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MatchDao matchDao = new MatchDaoImpl();
        TeamDao teamDao = new TeamDaoImpl();
        AthleticDao athleticDao = new AthleticDaoImpl();
        PlaysInDao playInDao = new PlaysInDaoImpl();
        try {
            athleticDao.getAthletic((String)request.getParameter("nfc"));
            nfc = true;
        } catch (NotFoundException e) {
            nfc = false;
        }
        if (nfc) { 
            if(request.getParameter("Rejoindre") == null) {
                try {
                    indexteam1 = teamDao.getAllTeam().size();
                    indexteam2 = teamDao.getAllTeam().size() + 1;
                } catch (NotFoundException e) {
                    indexteam1 = 0;
                    indexteam2 = 1;
                }
                try {
                    indexmatch = matchDao.getAllMatch().size();
                } catch (NotFoundException e) {
                    indexmatch = 0;
                }
                try {
                    teamDao.addTeam(indexteam1, (String) request.getParameter("Team1"));
                    teamDao.addTeam(indexteam2, (String) request.getParameter("Team2"));
                    playInDao.addEntry((String) request.getParameter("nfc"), indexteam1);
                    matchDao.addMatch(indexmatch, indexteam1, indexteam2);
                } catch(IntegrityException | NotFoundException e) {}
            } else {
                try {
                    playInDao.addEntry((String) request.getParameter("nfc"), Integer.parseInt(request.getParameter("match")));
                } catch(NotFoundException e) {}
            }
        }
        initVar(request, response);
        this.getServletContext().getRequestDispatcher("/WEB-INF/Team.jsp").forward(request, response);
    }
    
    private void initVar(HttpServletRequest request, HttpServletResponse response) {
        MatchDao resultDao = new MatchDaoImpl();
        TeamDao teamDao = new TeamDaoImpl();
        team = new HashMap<>();
        try {
            request.setAttribute("matchs", resultDao.getAllMatch());
            for (Match match : resultDao.getAllMatch()) {
                if(!team.containsKey(Integer.toString(match.getTeamID1())))
                    team.put(Integer.toString(match.getTeamID1()), teamDao.getName(match.getTeamID1()));
                if(!team.containsKey(Integer.toString(match.getTeamID2())))
                    team.put(Integer.toString(match.getTeamID2()), teamDao.getName(match.getTeamID2()));
            }
            request.setAttribute("team", team);
        } catch(NotFoundException e) {
            request.setAttribute("matchs", new ArrayList<>());
            request.setAttribute("team", new HashMap<>());
        }
    }
}
