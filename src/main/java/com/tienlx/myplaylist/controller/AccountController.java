/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienlx.myplaylist.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tienlx.myplaylist.dao.AccountDAO;
import com.tienlx.myplaylist.entity.Account;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author tienl_000
 */
@WebServlet(name = "AccountController", urlPatterns = {"/AccountController"})
public class AccountController extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        Gson gson = new Gson();
        Account userAcc;

        Account savedAcc = (Account) session.getAttribute("USER");
        AccountDAO dao;

        if (action.equals("Login")) {//TESTED OK
            if (savedAcc != null) {
                out.print(gson.toJson(savedAcc));
                out.flush();
                return;
            }

            String email = request.getParameter("email");
            String password = request.getParameter("password");
            //String previousPage = userMainPage;

            dao = new AccountDAO();
            userAcc = dao.login(email, password);
            if (userAcc != null) {
                session.setAttribute("USER", userAcc);
                out.print(gson.toJson(userAcc));
                out.flush();

            } else {
                out.print(gson.toJson("Wrong user"));
                out.flush();
                //response.sendRedirect("/AccountController/login.jsp" + "?msg=error");
            }
        } else if (action.equals("Register")) {//UNTESTED 


            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String username = request.getParameter("username");
            //String previousPage = userMainPage;
            dao = new AccountDAO();
            Account existed = dao.get(email);

            if (existed != null) {
                out.print(gson.toJson("Duplicate"));
                out.flush();

            } else {
                Account newAcc = new Account(email, username, password, 1);
                if (dao.save(newAcc)) {
                    session.setAttribute("USER", newAcc);
                    out.print(gson.toJson(newAcc));
                } else {
                    out.print(gson.toJson("Error"));
                }
                out.flush();
                //response.sendRedirect("/AccountController/login.jsp" + "?msg=error");
            }
        } else if (action.equals("Logout")) {//UNTESTED 
            session.removeAttribute("USER");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
