<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : fo-transform.xsl
    Created on : 8 February 2015, 2:59 PM
    Author     : tienl_000
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"  
                xmlns:t="http://yourplaylist.tk/Schema">
    <xsl:output method="xml"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template match="/t:TopSongs">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="x" page-height="8.5in"
                    page-width="11in" margin-top="0.5in" margin-bottom="0.5in"
                                       margin-left="1in" margin-right="1in">
                    <fo:region-body margin-top="0.5in" />
                    <fo:region-before extent="1in" />
                    <fo:region-after extent="0.75in" />
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="x">
                <fo:static-content flow-name="xsl-region-before">
                    <fo:block font-size="14pt" font-family="Verdana"
                        line-height="24pt" 
                        space-after.optimum="15pt" text-align="center"
                        padding-top="3pt">
                        Báo cáo lượt nghe BXH 40 ngày <xsl:value-of select="t:DateCreated" />
                    </fo:block>
                </fo:static-content>
                
                <fo:static-content flow-name="xsl-region-after">
                    <fo:block font-size="18pt" font-family="Arial"
                        line-height="24pt" space-after.optimum="15pt" 
                        text-align="center" padding-top="3pt">
                    </fo:block>
                </fo:static-content>
                
                <fo:flow flow-name="xsl-region-body">
                    <fo:block>
                        <fo:table border-collapse="separate" table-layout="fixed" font-family="Arial">
                            <fo:table-column column-width="2cm"/>
                            <fo:table-column column-width="7cm"/>
                            <fo:table-column column-width="5cm"/>
                            <fo:table-column column-width="4cm"/>
                            <fo:table-column column-width="4cm"/>
                            
                            <fo:table-body>
                                <fo:table-row>
                                    <fo:table-cell border-color="blue"
                                                   border-width="0.5pt"
                                                   border-style="solid">
                                        <fo:block text-align="center">Thứ tự</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border-color="blue"
                                                   border-width="0.5pt"
                                                   border-style="solid">
                                        <fo:block text-align="center">Tên bài hát</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border-color="blue"
                                                   border-width="0.5pt"
                                                   border-style="solid">
                                        <fo:block text-align="center">Ca sĩ</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border-color="blue"
                                                   border-width="0.5pt"
                                                   border-style="solid">
                                        <fo:block text-align="center">Thể loại</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border-color="blue"
                                                   border-width="0.5pt"
                                                   border-style="solid">
                                        <fo:block text-align="center">Lượt nghe</fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                
                                <xsl:for-each select="t:List/t:Song">
                                    <fo:table-row>
                                        <fo:table-cell border-color="blue"
                                                       border-width="0.5pt"
                                                       border-style="solid">
                                            <fo:block text-align="center">
                                                <xsl:number level="single" count="t:Song"/>
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell border-color="blue"
                                                       border-width="0.5pt"
                                                       border-style="solid">
                                            <fo:block text-align="left">
                                                <xsl:value-of select="t:Title" />
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell border-color="blue"
                                                       border-width="0.5pt"
                                                       border-style="solid">
                                            <fo:block text-align="left">
                                                <xsl:value-of select="t:Artist" />
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell border-color="blue"
                                                       border-width="0.5pt"
                                                       border-style="solid">
                                            <fo:block text-align="left">
                                                <xsl:value-of select="t:Category" />
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell border-color="blue"
                                                       border-width="0.5pt"
                                                       border-style="solid">
                                            <fo:block text-align="left">
                                                <xsl:value-of select="t:PlayCount" />
                                            </fo:block>
                                        </fo:table-cell>
                                    </fo:table-row>
                                </xsl:for-each>
                                
                            </fo:table-body>
                        </fo:table>
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

</xsl:stylesheet>
