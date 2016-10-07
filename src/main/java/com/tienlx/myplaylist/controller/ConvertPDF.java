/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienlx.myplaylist.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.xml.sax.SAXException;

/**
 *
 * @author tienl_000
 */
public class ConvertPDF extends HttpServlet {

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
        try {
            String date = request.getParameter("date");
            String path = getServletContext().getRealPath("/");
            String xslPath = path + "WEB-INF/fo-transform.xsl";
            String xmlPath = path + "WEB-INF/report/" + date + ".xml";
            String foPath = path + "WEB-INF/report/" + date + ".fo";
            String config = path + "WEB-INF/fopUserConfig.xml";
            File file = new File(foPath);
            methodTraX(xslPath, xmlPath, foPath, path);
            FileInputStream input = new FileInputStream(file);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.setContentType("application/pdf;charset=UTF-8");


            FopFactory ff = FopFactory.newInstance();
            FOUserAgent fua = ff.newFOUserAgent();
            ff.setUserConfig(config);
            ff.getFontManager().setFontBaseURL(path + "WEB-INF");
            Fop fop = ff.newFop(MimeConstants.MIME_PDF, fua, out);

            TransformerFactory tff = TransformerFactory.newInstance();
            Transformer trans = tff.newTransformer();
            File fo = new File(foPath);
            Source src = new StreamSource(fo);
            Result result = new SAXResult(fop.getDefaultHandler());
            trans.transform(src, result);

            byte[] content = out.toByteArray();
            response.setContentLength(content.length);
            response.getOutputStream().write(content);
            response.getOutputStream().flush();

        } catch (FOPException ex) {
            Logger.getLogger(ConvertPDF.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(ConvertPDF.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(ConvertPDF.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(ConvertPDF.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
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

    private void methodTraX(String xslPath, String xmlPath, String foPath, String path) {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            StreamSource xsltFile = new StreamSource(xslPath);
            Transformer trans = tf.newTransformer(xsltFile);

            StreamSource xmlFile = new StreamSource(xmlPath);
            StreamResult htmlFile = new StreamResult(new FileOutputStream(foPath));
            trans.transform(xmlFile, htmlFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConvertPDF.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(ConvertPDF.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(ConvertPDF.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
}
