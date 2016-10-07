/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienlx.myplaylist.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tienlx.myplaylist.dao.AccountDAO;
import com.tienlx.myplaylist.dao.PlaylistDAO;
import com.tienlx.myplaylist.dao.SongDAO;
import com.tienlx.myplaylist.entity.Account;
import com.tienlx.myplaylist.entity.Playlist;
import com.tienlx.myplaylist.entity.Song;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.tienlx.myplaylist.xml.AlbumParser;
import com.tienlx.myplaylist.xml.type.AlbumType;

/**
 *
 * @author tienl_000
 */
@WebServlet(name = "PlaylistController", urlPatterns = {"/PlaylistController"})
public class PlaylistController extends HttpServlet {

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
        HttpSession session = request.getSession();
        Gson gson = new Gson();
        PlaylistDAO dao = new PlaylistDAO();
        SongDAO songDao = new SongDAO();

        String action = request.getParameter("action");
        Account currentUser = (Account) session.getAttribute("USER");

        if (action.equals("save")) { //TESTED OK (validate done)
            AlbumType album;
            String name = request.getParameter("name");
            String accountEmail = request.getParameter("email");
            String username = request.getParameter("username");
            String[] songIds = request.getParameterValues("songList");
            String id = dao.getNewId();

            Playlist albumEntity = new Playlist(id, name, 0, accountEmail, username);
            album = new AlbumType(id, name, accountEmail);

            List<Song> songs = album.getSongList();
            for (String songId : songIds) {
                if (songId != null) {
                    Song song1 = songDao.get(songId);
                    songs.add(song1);
                }
            }

            String path = request.getServletContext().getRealPath("/");

            boolean result = dao.save(albumEntity);
            if (result) {
                AlbumParser parser = new AlbumParser();
                parser.parse(path + "playlist/" + id + ".xml", album);

                out.print(gson.toJson(album));
                out.flush();
            } else {
                out.print(gson.toJson("Error"));
                out.flush();
                //response.sendRedirect("/AccountController/login.jsp" + "?msg=error");
            }
        } else if (action.equals("delete")) { //UNTESTED
            String id = request.getParameter("id");
            String accountEmail = request.getParameter("email");

            if (currentUser == null || !currentUser.getEmail().equals(accountEmail)) {
                out.print(gson.toJson("Not Login"));
                out.flush();
            } else {
                Playlist p = dao.get(id);
                if (p.getAccountEmail().equals(accountEmail)) {
                    dao.remove(p);
                    out.print(gson.toJson("Success"));
                    out.flush();
                } else {
                    out.print(gson.toJson("Not Login"));
                    out.flush();
                }
            }
        } else if (action.equals("getLastPlaylist")) { //TESTED OK
            if (currentUser != null) {
                String email = currentUser.getEmail();
                final String lastId = dao.getLastIdByEmail(email);
                HashMap<String, String> result = new HashMap<String, String>();
                result.put("lastId", lastId);
                out.print(gson.toJson(result));
                out.flush();
            } else {
                out.print(gson.toJson("Not Login"));
                out.flush();
            }
        } else if (action.equals("getTopPlaylist")) { //TESTED
            String strLimit = request.getParameter("limit");
            int limit = 10;
            if (strLimit != null) {
                try {
                    limit = Integer.parseInt(strLimit);
                } catch (Exception e) {
                }
            }
            List<Playlist> topPlaylist = dao.getTopPlaylist(limit);
            out.print(gson.toJson(topPlaylist));
            out.flush();
        } else if (action.equals("getUserPlaylist")) { //TESTED
            if (currentUser != null) {
                String email = currentUser.getEmail();
                List<Playlist> topPlaylist = dao.getPlaylistByEmail(email);
                out.print(gson.toJson(topPlaylist));
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
