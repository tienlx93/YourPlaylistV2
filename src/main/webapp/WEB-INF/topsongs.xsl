<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : topsongs.xsl
    Created on : 8 February 2015, 2:40 PM
    Author     : tienl_000
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:t="http://yourplaylist.tk/Schema">
    <xsl:output method="html" encoding="UTF-8"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template match="/t:TopSongs">
        <h2>Bảng xếp hạng ngày
            <xsl:value-of select="t:DateCreated"/>
        </h2>
        <table border="1">
            <thead>
                <tr>
                    <td>Thứ tự</td>
                    <td>Tên bài hát</td>
                    <td>Ca sĩ</td>
                    <td>Thể loại</td>
                    <td>Lượt nghe</td>
                </tr>
            </thead>
            <tbody>
                <xsl:apply-templates select="t:List"/>
                
            </tbody>
        </table>
    </xsl:template>

    <xsl:template match="t:List">
        <xsl:apply-templates select="t:Song">
        </xsl:apply-templates>
    </xsl:template>
    
    <xsl:template match="t:Song">
        <tr>
            <td><xsl:number level="single" count="t:Song"/></td>
            <td><xsl:value-of select="t:Title"/></td>
            <td><xsl:value-of select="t:Artist"/></td>
            <td><xsl:value-of select="t:Category"/></td>
            <td><xsl:value-of select="t:PlayCount"/></td>
        </tr>
    </xsl:template>
</xsl:stylesheet>
