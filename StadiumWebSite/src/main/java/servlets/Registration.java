/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.Nfc;
import dao.AthleticDao;
import dao.impl.AthleticDaoImpl;
import verifications.VerifName;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class qui permet d'afficher la JSP relative à l'inscription d'un athlète.
 * 
 * @author Maxime
 */
public class Registration extends HttpServlet {
     
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.getServletContext().getRequestDispatcher("/WEB-INF/Registration.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        VerifName verif = new VerifName(request.getParameter("Prenom"),request.getParameter("Nom"),request.getParameter("Age"),request.getParameter("Sexe"),request.getParameter("MDP"));
        if (verif.tryTest()) {        
            AthleticDao athleticDao = new AthleticDaoImpl();
            int old = Integer.parseInt(request.getParameter("Age"));
            Nfc nfc = new Nfc();
            request.setAttribute("correct", true);
            request.setAttribute("value", verif.getValue());
            athleticDao.addAthletic(nfc.getNfc(), request.getParameter("Prenom"), request.getParameter("Nom"), old, request.getParameter("Sexe"), request.getParameter("MDP"));
        } else {
            request.setAttribute("value", verif.getValue());
            request.setAttribute("correct", false);
        }
        this.getServletContext().getRequestDispatcher("/WEB-INF/Registration.jsp").forward(request, response);
    }
}
