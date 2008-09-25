<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="https://ajax4jsf.dev.java.net/ajax" prefix="a4j"%>
<%@ taglib uri="http://richfaces.ajax4jsf.org/rich" prefix="rich"%>

<%--
    Copyright (C) 2008 Busico, Funes, Zilocchi

    Authors:
        Busico, Marcelo (marcelobusico@gmail.com)
        Funes, Franco (funesfranco@gmail.com)
        Zilocchi, Emiliano (ezilocchi@gmail.com)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
 
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <link href="css/appStyles.css" rel="stylesheet" type="text/css">
    </head>
    <body>
        <f:view>            
             <a4j:form>                 
                 <h:panelGrid columns="2" columnClasses="columnMsgs,columnLogin" styleClass="loginConteiner" id="header">
                     <h:panelGroup>
                        <rich:messages globalOnly="true"></rich:messages> 
                        <h:outputFormat value="#{msgs.resultLabel}">
                            <f:param value="#{searchBean.matches}"></f:param>
                        </h:outputFormat>
                     </h:panelGroup>
                     
                    <h:panelGroup>                                                                                                                 
                        <a4j:commandLink value="#{msgs.manage}" action="login"></a4j:commandLink>
                    </h:panelGroup>
                </h:panelGrid>
            </a4j:form>
            <rich:separator height="2" ></rich:separator>
            <a4j:form>  
                <h:panelGrid width="100%" style="text-align: center;">                        
                    <h:graphicImage value="/images/sabuesonix-logo.jpg"></h:graphicImage>
                    <h:panelGroup>                            
                        <h:inputText value="#{searchBean.searchParam}"></h:inputText>
                        <h:commandButton value="#{msgs.search}"
                                           actionListener="#{searchBean.search}">
                              <a4j:support event="onclick" reRender="gridResult,header"></a4j:support>                       
                        </h:commandButton>
                    </h:panelGroup>
                </h:panelGrid>                    
                <rich:separator height="16" ></rich:separator>                                                
                <rich:datascroller for="docList" page="#{searchBean.pageNumber}" id="ds1" reRender="ds2"></rich:datascroller>
                <h:panelGrid width="100%" styleClass="loginConteiner" id="gridResult">
                    <rich:dataList id="docList" value="#{searchBean.documents}" var="document" rows="25" rowClasses="oddRow, evenRow">
                        <h:panelGrid styleClass="list"  width="100%">
                            <h:panelGroup>
                                <a4j:htmlCommandLink actionListener="#{searchBean.downloadFile}" target="_new">
                                    <h:outputText value="#{document.title}" style="font-weight: bold;"></h:outputText>
                                </a4j:htmlCommandLink>                            
                                <h:outputText value="  (#{document.extension})" style="font-weight: bold;"></h:outputText>
                            </h:panelGroup>
                            <h:outputText value="#{document.resume}"></h:outputText>
                        </h:panelGrid>
                    </rich:dataList>
                    <rich:datascroller for="docList" page="#{searchBean.pageNumber}" id="ds2" reRender="ds1"></rich:datascroller>
                </h:panelGrid>
                <rich:panel>
                    <h:panelGrid width="100%" style="text-align: center;">                        
                        <a4j:commandLink value="#{msgs.about}" action="about" style="text-align: center;"></a4j:commandLink>                                            
                    </h:panelGrid>
                </rich:panel>
            </a4j:form>
        </f:view>
    </body>
</html>

