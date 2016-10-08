/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienlx.myplaylist.controller;

import com.tienlx.myplaylist.crawler.CrawlerV2;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tienl_000
 */
@WebServlet(name = "CrawlerController", urlPatterns = {"/CrawlerController"})
public class CrawlerController extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = request.getParameter("url");
        String full = request.getParameter("full");
        CrawlerV2 crawler = new CrawlerV2();
//        String absoluteDiskPath = this.getClass().getResource("/").getPath();
        String absoluteDiskPath = getServletContext().getRealPath("/") + File.separator;
        Logger.getLogger(CrawlerV2.class.getName()).log(Level.INFO, "absoluteDiskPath: " + absoluteDiskPath);
        System.out.printf("absoluteDiskPath: " + absoluteDiskPath);
        crawler.setBasePath(absoluteDiskPath);
        String base = "http://mp3.zing.vn";
        try {
            if (url == null) {
                if (full == null) {
                    crawler.fullProcess(base);
                } else {
                    crawler.fullProcess(full);
                }
            } else {
                crawler.processPage(url);
            }
        } catch (IOException ex) {
            Logger.getLogger(CrawlerV2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
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
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
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
