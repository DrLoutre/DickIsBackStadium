/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.Nfc;
import dao.AthleticDao;
import dao.impl.AthleticDaoImpl;
import verifications.Verif;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Maxime
 */
public class Registration extends HttpServlet {
     
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("correct", false);
        this.getServletContext().getRequestDispatcher("/WEB-INF/Registration.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Verif verif = new Verif(request.getParameter("Nom"),request.getParameter("Prenom"),request.getParameter("Age"),request.getParameter("MDP"),request.getParameter("Sexe"));
        if (verif.tryTest()) {        
            request.setAttribute("correct", true);
            AthleticDao athleticdao = new AthleticDaoImpl();
            int old = Integer.parseInt(request.getParameter("Age"));
            Nfc nfc = new Nfc();
            System.out.println(request.getParameter("Sexe"));
            athleticdao.addAthletic(nfc.getNfc(), request.getParameter("Prenom"), request.getParameter("Nom"), old, request.getParameter("Sexe"), request.getParameter("MDP"));
        }
        else {
            request.setAttribute("correct", false);
        }
        this.getServletContext().getRequestDispatcher("/WEB-INF/Registration.jsp").forward(request, response);
    }
}
