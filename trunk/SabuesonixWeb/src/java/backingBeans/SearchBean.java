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

import com.sun.faces.config.beans.ApplicationBean;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.naming.InitialContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import sabuesonix.ejbs.SearcherRemote;
import sabuesonix.entities.Document;

/**
 * SearchBean Class.
 * @author 
 * Busico, Marcelo (marcelobusico@gmail.com)
 * Funes, Franco (funesfranco@gmail.com)
 * Zilocchi, Emiliano (ezilocchi@gmail.com)
 * @version September/2008
 */
public class SearchBean {

    private String searchParam;
    private DataModel documents;
    private SearcherRemote searcher;
    private int pageNumber;
    private int matches;

    public SearchBean() {
        try {
            InitialContext ic = new InitialContext();
            searcher = (SearcherRemote) ic.lookup(SearcherRemote.class.getName());
        } catch (Exception ex) {
            searcher = null;
            Logger.getLogger(ApplicationBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getSearchParam() {
        return searchParam;
    }

    public void setSearchParam(String searchParam) {
        this.searchParam = searchParam;
    }

    public DataModel getDocuments() {
        return documents;
    }

    public void setDocuments(DataModel documents) {
        this.documents = documents;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getMatches() {
        return matches;
    }

    public void setMatches(int matches) {
        this.matches = matches;
    }

    public void search(ActionEvent event) {
        List<Document> res = searcher.search(searchParam);
        if (res != null) {
            this.documents = new ListDataModel(res);            
            this.matches = res.size();
        } else {
            documents = new ListDataModel();
            this.matches = 0;
        }
        this.pageNumber = 1;        
    }
    
    public void downloadFile(ActionEvent event){
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (!ctx.getResponseComplete()) {
            ServletOutputStream out = null;
            try {
                Document d = (Document) this.documents.getRowData();
                FileInputStream fis = new FileInputStream(d.getFile());
                String fileName = d.getFile().getName();
                String contentType = "";

                if (d.getExtension().equals(Document.TYPE_TXT)) {
                    contentType = "text/plain";
                } else if (d.getExtension().equals(Document.TYPE_HTML)) {
                    contentType = "text/html";
                } else if (d.getExtension().equals(Document.TYPE_PDF)) {
                    contentType = "application/pdf";
                } else {
                    contentType = "application/octet-stream";
                }

                HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
                response.setContentType(contentType);
                response.setHeader("cache-control", "no-cache");
                response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
                out = response.getOutputStream();
                byte[] datos = new byte[(int) d.getFile().length()];
                fis.read(datos);
                out.write(datos);
                out.flush();
                ctx.responseComplete();
            } catch (IOException ex) {
                Logger.getLogger(SearchBean.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(SearchBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }
}
