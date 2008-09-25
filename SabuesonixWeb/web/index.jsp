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
                 <h:panelGrid columns="2" columnClasses="columnMsgs,columnLogin" styleClass="loginConteiner">
                     <rich:messages globalOnly="true"></rich:messages> 
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
                                         action="search"
                                           actionListener="#{searchBean.search}">
                              <a4j:support event="onclick" reRender="gridResult"></a4j:support>                       
                        </h:commandButton>
                    </h:panelGroup>
                </h:panelGrid>                  
                <rich:separator height="16"></rich:separator>                
                <rich:panel>
                    <h:panelGrid width="100%" style="text-align: center;">                        
                        <a4j:commandLink value="#{msgs.about}" action="about" style="text-align: center;"></a4j:commandLink>                                            
                    </h:panelGrid>
                </rich:panel>
            </a4j:form>            
        </f:view>        
    </body>
</html>