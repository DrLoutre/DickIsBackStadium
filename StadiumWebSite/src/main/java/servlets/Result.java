/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.Match;
import dao.MatchDao;
import dao.TeamDao;
import dao.impl.MatchDaoImpl;
import dao.impl.TeamDaoImpl;
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
public class Result extends HttpServlet {
    
    HashMap<String, String> team;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        this.getServletContext().getRequestDispatcher("/WEB-INF/Result.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.getServletContext().getRequestDispatcher("/WEB-INF/Result.jsp").forward(request, response);
    }
}