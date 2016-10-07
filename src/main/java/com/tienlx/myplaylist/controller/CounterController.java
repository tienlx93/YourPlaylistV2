/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienlx.myplaylist.controller;

import com.google.gson.Gson;
import com.tienlx.myplaylist.dao.PlaylistDAO;
import com.tienlx.myplaylist.dao.SongDAO;
import com.tienlx.myplaylist.entity.Song;
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
@WebServlet(name = "CounterController", urlPatterns = {"/CounterController"})
public class CounterController extends HttpServlet {

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
        String id = request.getParameter("id");
        HttpSession session = request.getSession();
        Gson gson = new Gson();

        if (action.equals("Song")) {
            SongDAO dao = new SongDAO();
            if (dao.updatePlayCount(id)) {
                out.print(gson.toJson("Success"));
                out.flush();
            } else {
                out.print(gson.toJson("Not found"));
                out.flush();
            }


        } else if (action.equals("Playlist")) {
            PlaylistDAO dao = new PlaylistDAO();
            if (dao.updatePlayCount(id)) {
                out.print(gson.toJson("Success"));
                out.flush();
            } else {
                out.print(gson.toJson("Not found"));
                out.flush();
            }
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
