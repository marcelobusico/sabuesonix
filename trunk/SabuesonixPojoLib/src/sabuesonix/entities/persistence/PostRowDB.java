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
package sabuesonix.entities.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import sabuesonix.entities.Document;
import sabuesonix.entities.PostRow;
import sabuesonix.entities.Word;

/**
 * Persistence class.
 * @author 
 * Busico, Marcelo (marcelobusico@gmail.com)
 * Funes, Franco (funesfranco@gmail.com)
 * Zilocchi, Emiliano (ezilocchi@gmail.com)
 * @version September/2008
 */
public class PostRowDB {

    private Connection con;

    public PostRowDB(Connection con) {
        this.con = con;
    }

    public void save(PostRow pr) throws SQLException {
        if (pr == null) {
            throw new IllegalArgumentException("Postrow null.");
        }
        if (pr.getDocument() == null) {
            throw new IllegalArgumentException("Document null.");
        }
        if (pr.getDocument().getOid() == 0) {
            throw new IllegalArgumentException("Document with oid=0:" +
                    pr.getDocument().getFile().toString());
        }
        if (pr.getWord() == null) {
            throw new IllegalArgumentException("Word null.");
        }
        if (pr.getWord().getOid() == 0) {
            throw new IllegalArgumentException("Word with oid=0:" +
                    pr.getWord().getName());
        }
        PreparedStatement st = con.prepareStatement(
                "SELECT fn_savePostRow(?, ?, ?, ?)");
        st.setInt(1, pr.getOid());
        st.setInt(2, pr.getDocument().getOid());
        st.setInt(3, pr.getWord().getOid());
        st.setInt(4, pr.getTf());
        st.execute();
        if (!st.isClosed()) {
            try {
                st.close();
            } catch (Exception ex) {
                //Nothing
            }
        }
    }

    public PostRow read(int oid, Word w) throws SQLException {
        PostRow pr = new PostRow();
        PreparedStatement st = con.prepareStatement(
                "SELECT idDocument, tf " +
                "FROM postrow " +
                "WHERE id = ?");
        st.setInt(1, oid);
        ResultSet res = st.executeQuery();
        res.next();
        pr.setOid(oid);
        DocumentDB ddb = new DocumentDB(con);
        Document doc = ddb.read(res.getInt(1));
        pr.setDocument(doc);
        pr.setTf(res.getInt(2));
        pr.setWord(w);
        res.close();
        if (!st.isClosed()) {
            try {
                st.close();
            } catch (Exception ex) {
                //Nothing
            }
        }
        return pr;
    }

    public List<PostRow> read(Document doc) throws SQLException {
        List<PostRow> listpr = new LinkedList<PostRow>();
        PreparedStatement st = con.prepareStatement(
                "SELECT id, idWord, tf " +
                "FROM postrow " +
                "WHERE idDocument = ?");
        st.setInt(1, doc.getOid());
        ResultSet res = st.executeQuery();
        while (res.next()) {
            PostRow pr = new PostRow();
            //Object id
            pr.setOid(res.getInt(1));
            //Document
            pr.setDocument(doc);
            //Word
            int idWord = res.getInt(2);
            Word w = new WordDB(con).read(false, idWord);
            pr.setWord(w);
            //Tf
            pr.setTf(res.getInt(3));
            //Add to list
            listpr.add(pr);
        }
        res.close();
        if (!st.isClosed()) {
            try {
                st.close();
            } catch (Exception ex) {
                //Nothing
            }
        }
        return listpr;
    }

    public void delete(PostRow pr) throws SQLException {
        PreparedStatement st = con.prepareStatement(
                "DELETE FROM postrow " +
                " WHERE id = ?");
        st.setInt(1, pr.getOid());
        st.execute();
        if (!st.isClosed()) {
            try {
                st.close();
            } catch (Exception ex) {
                //Nothing
            }
        }
    }
}
