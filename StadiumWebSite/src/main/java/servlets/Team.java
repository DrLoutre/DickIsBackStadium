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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import verifications.VerifMatch;

/**
 * Class qui permet d'afficher la JSP relative à la création d'un match.
 * 
 * @author Maxime
 */
public class Team extends HttpServlet {
     
    boolean nfc;
    boolean time;
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
                VerifMatch verif;
                try {
                    verif = new VerifMatch(request.getParameter("Date"), request.getParameter("Heure"));
                    if(verif.tryTest()) {
                        time = true;
                        try {
                            int indexteam1 = teamDao.addTeam((String) request.getParameter("Team1"));
                            int indexteam2 = teamDao.addTeam((String) request.getParameter("Team2"));
                            playInDao.addEntry((String) request.getParameter("nfc"), indexteam1);
                            String date = request.getParameter("Date") + " " + request.getParameter("Heure") + ":00";
                            matchDao.addMatch(indexteam1, indexteam2, 0, 0, date, false);
                        } catch(IntegrityException | NotFoundException e) {} 
                    } else {
                        time = false;
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(Team.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                time = true;
                try {
                    playInDao.addEntry((String) request.getParameter("nfc"), Integer.parseInt(request.getParameter("match")));
                } catch(NotFoundException | IntegrityException e) {
                    nfc = false;
                }
            }
        }
        request.setAttribute("correct", nfc);
        request.setAttribute("time", time);
        initVar(request, response);
        this.getServletContext().getRequestDispatcher("/WEB-INF/Team.jsp").forward(request, response);
    }
    
    /**
     * Permet d'initialiser des variables afin d'afficher l'ensemble des matchs qui ne sont pas finis. 
     */
    private void initVar(HttpServletRequest request, HttpServletResponse response) {
        MatchDao resultDao = new MatchDaoImpl();
        TeamDao teamDao = new TeamDaoImpl();
        team = new HashMap<>();
        try {
            request.setAttribute("matchs", resultDao.getNotEndedMatch());
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
