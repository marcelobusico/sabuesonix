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
            <a4j:region>
                <h:form>
                    <a4j:poll interval="5000" reRender="idxStatus" 
                              actionListener="#{managementBean.refresh}"/>
                </h:form>
            </a4j:region>
            
            <a4j:form>                 
                <h:panelGrid columns="2" columnClasses="columnMsgs,columnLogin" styleClass="loginConteiner">
                    <rich:messages globalOnly="true"></rich:messages> 
                    <h:panelGroup>
                        <h:commandLink value="#{msgs.return}" action="return"></h:commandLink>
                    </h:panelGroup>
                </h:panelGrid>
            </a4j:form>
            <rich:separator height="2" ></rich:separator>
            <a4j:form>
                <rich:tabPanel switchType="client">
                    <rich:tab label="#{msgs.paths}">
                        <h:panelGrid columns="3">
                            <h:outputText value="#{msgs.path}"></h:outputText>
                            <h:inputText value="#{managementBean.path}"></h:inputText>
                            <a4j:commandButton value="#{msgs.new} #{msgs.path}"
                                               actionListener="#{managementBean.newPath}"
                                               reRender="pathsTable"></a4j:commandButton>
                        </h:panelGrid>
                        <rich:dataTable value="#{managementBean.paths}" 
                                        var="path" 
                                        id="pathsTable"
                                        rowClasses="oddRow, evenRow"
                                        columnClasses="columnPaths,columnOptions"
                                        width="50%">                            
                            <rich:column>
                                <h:outputText value="#{path}"></h:outputText>
                            </rich:column>
                            <rich:column>
                                <a4j:commandButton image="/images/eliminarSmallBtn.png"
                                                   actionListener="#{managementBean.delPath}"
                                                   reRender="pathsTable"></a4j:commandButton>
                            </rich:column>
                        </rich:dataTable>
                    </rich:tab>
                    <rich:tab label="#{msgs.state}" id="tabState">                        
                        <h:panelGrid rowClasses="oddRow,evenRow" columns="2" id="idxStatus" 
                                     style="font-size: 12px;">
                            <h:outputText value="#{msgs.indexerStatus}"></h:outputText>
                            <h:outputText value="#{managementBean.indexerStatus}" escape="false"></h:outputText>
                            <h:outputText value="#{msgs.averageSpeed}"></h:outputText>
                            <h:outputText value="#{managementBean.averageSpeed} MBytes/h" ></h:outputText>
                            <h:outputText value="#{msgs.documentCount}"></h:outputText>
                            <h:outputText value="#{managementBean.documentCount}"></h:outputText>
                            <h:outputText value="#{msgs.totalSizeIndexed}"></h:outputText>
                            <h:outputText value="#{managementBean.totalSizeIndexed} MBytes"></h:outputText>
                            <h:outputText value="#{msgs.totalTimeIndexed}"></h:outputText>
                            <h:outputText value="#{managementBean.totalTimeIndexed} #{msgs.hour}"></h:outputText>
                            <h:outputText value="#{msgs.wordCount}"></h:outputText>
                            <h:outputText value="#{managementBean.wordCount}"></h:outputText>
                            <h:outputText value="#{msgs.documentTypeCount}"></h:outputText>
                            <h:outputText value="#{managementBean.documentTypeCount}" escape="false"></h:outputText>
                        </h:panelGrid>                        
                        <a4j:commandButton value="#{msgs.startIndex}" actionListener="#{managementBean.startIndexEngine}" reRender="roboticDog"></a4j:commandButton>
                        <a4j:commandButton value="#{msgs.stopIndex}" actionListener="#{managementBean.stopIndexEngine}" reRender="roboticDog"></a4j:commandButton>
                    </rich:tab>
                </rich:tabPanel>                
            </a4j:form>
        </f:view>
    </body>
</html>
