<%-- 
    Document   : topsong
    Created on : 08/02/2015, 2:37:17 PM
    Author     : tienl_000
--%>

<%@page import="java.io.File"%>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="parser" class="com.tienlx.myplaylist.xml.TopSongsParser" scope="request"/>
<c:set var="list" value="${parser}"/>

<c:if test="${not empty list}">

    <%
        parser.setPath(application.getRealPath("/") + "WEB-INF/report/");
        String filename = parser.getFilename();
        String pathname = parser.getPath() + filename + ".xml";
        parser.marshallXML(pathname);
    %>
    <c:import var="xslt" url="WEB-INF/topsongs.xsl" charEncoding="UTF-8"/>
    <c:import var="xml" url="WEB-INF/report/${parser.filename}.xml" charEncoding="UTF-8"/>

    <x:transform xml="${xml}" xslt="${xslt}"/>
    <h3>Báo cáo theo ngày</h3>
    <form action="ConvertPDF" target="_blank" >
        <select name="date">
            <c:forEach var="file" items="${parser.fileList}">
                <option value="${file}">${file.substring(6)}-${file.substring(4,6)}-${file.substring(0,4)}</option>
            </c:forEach>
        </select>
        <input type="submit" value="Lập báo cáo"/>
    </form>
</c:if>


