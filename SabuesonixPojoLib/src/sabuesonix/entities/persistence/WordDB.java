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
public class WordDB {

    private Connection con;

    public WordDB(Connection con) {
        this.con = con;
    }

    public void save(Word w) throws SQLException {
        boolean nuevo = false;
        if (w.getOid() == 0) {
            nuevo = true;
        }
        PreparedStatement st = con.prepareStatement(
                "SELECT fn_saveWord(?, ?, ?, ?)");
        st.setInt(1, w.getOid());
        st.setString(2, w.getName());
        st.setInt(3, w.getNr());
        st.setInt(4, w.getMaxTf());
        ResultSet rs = st.executeQuery();
        if (nuevo) {
            rs.next();
            w.setOid(rs.getInt(1));
        }
        rs.close();
        if (!st.isClosed()) {
            try {
                st.close();
            } catch (Exception ex) {
                //Nothing
            }
        }
    }

    /**
     * Reads de Word from database.
     * @param readPostRows Indicate if load or not the
     * postrows references from db.
     * @param oid Id of word to read.
     * @return Word read.
     * @throws java.sql.SQLException If some error occurs.
     */
    public Word read(boolean readPostRows, int oid) throws SQLException {
        Word w = new Word();
        PreparedStatement st = con.prepareStatement(
                "SELECT name, nr, maxTf " +
                "FROM word " +
                "WHERE id = ?");
        st.setInt(1, oid);
        ResultSet res = st.executeQuery();
        res.next();
        w.setOid(oid);
        w.setName(res.getString(1));
        w.setNr(res.getInt(2));
        w.setMaxTf(res.getInt(3));
        res.close();
        if (!st.isClosed()) {
            try {
                st.close();
            } catch (Exception ex) {
                //Nothing
            }
        }

        if (!readPostRows) {
            w.setPostRows(null);
            return w;
        }

        st = con.prepareStatement(
                "SELECT id, idDocument, tf " +
                "FROM postrow " +
                "WHERE idWord = ?");
        st.setInt(1, oid);
        res = st.executeQuery();
        DocumentDB ddb = new DocumentDB(con);
        w.setPostRows(new LinkedList<PostRow>());
        while (res.next()) {
            PostRow pr = new PostRow();
            pr.setOid(res.getInt(1));
            Document doc = ddb.read(res.getInt(2));
            pr.setDocument(doc);
            pr.setTf(res.getInt(3));
            pr.setWord(w);
            w.getPostRows().add(pr);
        }
        res.close();
        if (!st.isClosed()) {
            try {
                st.close();
            } catch (Exception ex) {
                //Nothing
            }
        }
        return w;
    }

    public List<Word> readAll() throws SQLException {
        List<Word> words = new LinkedList<Word>();
        PreparedStatement st = con.prepareStatement(
                "SELECT id, name, nr, maxTf " +
                "FROM word");
        ResultSet res = st.executeQuery();
        while (res.next()) {
            Word w = new Word();
            w.setOid(res.getInt(1));
            w.setName(res.getString(2));
            w.setNr(res.getInt(3));
            w.setMaxTf(res.getInt(4));
            words.add(w);
        }
        res.close();
        if (!st.isClosed()) {
            try {
                st.close();
            } catch (Exception ex) {
                //Nothing
            }
        }
        return words;
    }

    public void delete(Word w) throws SQLException {
        PreparedStatement st = con.prepareStatement(
                "DELETE FROM word " +
                " WHERE id = ?");
        st.setInt(1, w.getOid());
        st.execute();
        if (!st.isClosed()) {
            try {
                st.close();
            } catch (Exception ex) {
                //Nothing
            }
        }
    }

    /**
     * Verify if the word is in some document, the calculate the maximum number 
     * of occurrences of the word in a document and decrement the Nr in one unit.
     * @param word Word to recalculate the maxTf and new Nr.
     */
    public void updateMaxTfAndNr(Word word) throws SQLException {
        //Verify if the word is in use.
        PreparedStatement st = con.prepareStatement(
                "SELECT COUNT(*) " +
                "FROM postrow " +
                "WHERE idWord = ?");
        st.setInt(1, word.getOid());
        ResultSet res = st.executeQuery();
        res.next();
        int count = res.getInt(1);
        res.close();
        if (!st.isClosed()) {
            try {
                st.close();
            } catch (Exception ex) {
                //Nothing
            }
        }
        if (count == 0) {
            //Delete the word
            delete(word);
            return;
        }

        //Get the maxTf of the word
        st = con.prepareStatement(
                "SELECT MAX(tf) " +
                "FROM postrow " +
                "WHERE idWord = ?");
        st.setInt(1, word.getOid());
        res = st.executeQuery();
        res.next();
        int maxTf = res.getInt(1);
        res.close();
        if (!st.isClosed()) {
            try {
                st.close();
            } catch (Exception ex) {
                //Nothing
            }
        }

        //Update the maxTf and nr of the word
        st = con.prepareStatement(
                "UPDATE word " +
                "SET nr = (nr - 1), maxTf = ? " +
                "WHERE id = ?");
        st.setInt(1, maxTf);
        st.setInt(2, word.getOid());
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
