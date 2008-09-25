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
        
    </head>
    <body>
        <f:view>
                                            
                <h:panelGroup style="width:100%;height:100%;">
                    <rich:modalPanel rendered="true" showWhenRendered="true" moveable="false" resizeable="false">                        
                    <f:facet name="header">
                        <h:outputText value="Login"></h:outputText>
                    </f:facet>
                    <h:form>
                        <h:panelGroup>
                            <h:panelGrid columns="2">
                                    <h:outputText value="#{msgs.login}: "></h:outputText>
                                    <h:inputText id="inputLogin" value="#{managementBean.login}"></h:inputText>                
                                    <h:outputText value="#{msgs.pass}: "></h:outputText>
                                    <h:inputSecret id="inputPass" value="#{managementBean.pass}"></h:inputSecret>                
                                    <h:commandButton value="#{msgs.ok}" action="#{managementBean.login}"></h:commandButton>
                                    <h:commandButton value="#{msgs.cancel}" action="return"></h:commandButton>
                            </h:panelGrid>
                            <a4j:outputPanel ajaxRendered="true"> 
                                <rich:messages showSummary="true" style="color:red;"></rich:messages> 
                            </a4j:outputPanel>
                        </h:panelGroup>
                    </h:form>
                </rich:modalPanel>
                </h:panelGroup>
            
        </f:view>
    </body>
</html>
