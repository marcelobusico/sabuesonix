/*
 * Copyright (C) 2008 Busico, Funes, Zilocchi
 *
 * Authors:
 *    Busico, Marcelo (marcelobusico@gmail.com)
 *    Funes, Franco (funesfranco@gmail.com)
 *    Zilocchi, Emiliano (ezilocchi@gmail.com)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package backingBeans;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.naming.InitialContext;
import sabuesonix.ejbs.IndexerRemote;
import sabuesonix.entities.Path;

/**
 * ManagementBean Class.
 * @author 
 * Busico, Marcelo (marcelobusico@gmail.com)
 * Funes, Franco (funesfranco@gmail.com)
 * Zilocchi, Emiliano (ezilocchi@gmail.com)
 * @version September/2008
 */
public class ManagementBean {

    private IndexerRemote indexer;
    private String indexerStatus;
    private String login;
    private String pass;
    private DataModel paths;
    private List<Path> list;
    private String path;

    public ManagementBean() {
        try {
            InitialContext ic = new InitialContext();
            indexer = (IndexerRemote) ic.lookup(IndexerRemote.class.getName());
            list = indexer.listPaths();
        } catch (Exception ex) {
            indexer = null;
            Logger.getLogger(ManagementBean.class.getName()).log(Level.SEVERE,
                    "Couldn't connect to indexer bean.", ex);
        }
        if (list == null) {
            list = new ArrayList<Path>();
        }
        paths = new ListDataModel(list);
    }

    public String getIndexerStatus() {
        return indexerStatus;
    }

    public void setIndexerStatus(String indexerStatus) {
        this.indexerStatus = indexerStatus;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public DataModel getPaths() {
        return paths;
    }

    public void setPaths(DataModel paths) {
        this.paths = paths;
    }

    public void newPath(ActionEvent event) {
        Path p = new Path(path);
        list.add(p);
        indexer.addPath(p);
    }

    public void delPath(ActionEvent event) {
        Path p = (Path) this.paths.getRowData();
        list.remove(p);
        indexer.removePath(p);
    }

    public void startIndexEngine(ActionEvent event) {
        if (indexer == null) {
            return;
        }
        if (indexer.isStopped()) {
            indexer.start();
        }
        this.indexerStatus = indexer.getIndexerState();
    }

    public void stopIndexEngine(ActionEvent event) {
        if (indexer == null) {
            return;
        }
        if (indexer.isRunning()) {
            indexer.stop();
        }
        this.indexerStatus = indexer.getIndexerState();
    }

    public void refresh(ActionEvent event) {
        if (indexer == null) {
            this.indexerStatus = "Unknown";
        }
        this.indexerStatus = indexer.getIndexerState();
    }

    public String login() {
        if (this.login == null || this.pass == null) {
            FacesContext.getCurrentInstance().addMessage(login, new FacesMessage(FacesMessage.SEVERITY_ERROR, "login o pass incorrecto", null));
            return "failure";
        }
        if (this.login.equalsIgnoreCase("admin") && this.pass.equalsIgnoreCase("admin")) {
            return "success";
        } else {
            FacesContext.getCurrentInstance().addMessage(login, new FacesMessage(FacesMessage.SEVERITY_ERROR, "login o pass incorrecto", null));
            return "failure";
        }
    }

    public float getAverageSpeed() {
        return this.indexer.getAverageSpeed();
    }

    public long getDocumentCount() {
        return this.indexer.getDocumentCount();
    }

    public float getTotalSizeIndexed() {
        return this.indexer.getTotalSizeIndexed();
    }

    public float getTotalTimeIndexed() {
        return this.indexer.getTotalTimeIndexed();
    }

    public long getWordCount() {
        return this.indexer.getWordCount();
    }

    public String getDocumentTypeCount() {
        return this.indexer.getDocumentTypeCount();
    }
}
