/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienlx.myplaylist.controller;

import com.google.gson.Gson;
import com.tienlx.myplaylist.dao.ArtistDAO;
import com.tienlx.myplaylist.dao.SongDAO;
import com.tienlx.myplaylist.entity.Artist;
import com.tienlx.myplaylist.entity.Song;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.tienlx.myplaylist.util.AccentRemover;

/**
 *
 * @author tienl_000
 */
public class Search extends HttpServlet {

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

        Gson gson = new Gson();
        SongDAO songDao = new SongDAO();

        String method = request.getParameter("method");
        String query = request.getParameter("query");
        query = AccentRemover.removeAccent(query);
        List result;
        if (method.equals("artist")) {
            ArtistDAO aDao = new ArtistDAO();
            Artist artist = aDao.getByName(query);
            out.print(gson.toJson(artist));
            out.flush();
        } else {
            if (method.equals("quick")) { //TESTED OK
                result = songDao.search(query, 6);
            } else if (method.equals("limit")) {
                String limit = request.getParameter("limit");
                int number = Integer.parseInt(limit);
                result = songDao.search(query, number);
            } else {//TESTED OK
                result = songDao.search(query, 0);
            }
            if (result != null) {

                out.print(gson.toJson(result));
                out.flush();

            } else {
                out.print("[]");
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
