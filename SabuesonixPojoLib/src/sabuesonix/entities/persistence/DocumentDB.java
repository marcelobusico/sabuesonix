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

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;
import sabuesonix.entities.Document;

/**
 * Persistence class.
 * @author 
 * Busico, Marcelo (marcelobusico@gmail.com)
 * Funes, Franco (funesfranco@gmail.com)
 * Zilocchi, Emiliano (ezilocchi@gmail.com)
 * @version September/2008
 */
public class DocumentDB {

    private Connection con;

    public DocumentDB(Connection con) {
        this.con = con;
    }

    public void save(Document d) throws SQLException {
        PreparedStatement st = con.prepareStatement(
                "SELECT fn_saveDocuments(?, ?, ?, ?, ?, ?, ?, ?)");
        st.setInt(1, d.getOid());
        if (d.getFile() == null) {
            st.setNull(2, Types.VARCHAR);
        } else {
            st.setString(2, d.getFile().getPath());
        }
        st.setString(3, d.getTitle());
        st.setString(4, d.getResume());
        st.setLong(5, d.getSize());
        st.setString(6, d.getExtension());
        st.setLong(7, d.getTimeIndexed());
        st.setLong(8, d.getTimeLastModified());
        ResultSet rs = st.executeQuery();
        rs.next();
        d.setOid(rs.getInt(1));
        rs.close();
        if (!st.isClosed()) {
            try {
                st.close();
            } catch (Exception ex) {
                //Nothing
            }
        }
    }

    public Document read(int oid) throws SQLException {
        Document d = new Document();
        PreparedStatement st = con.prepareStatement(
                "SELECT path, title, resume, size, extension, timeIndexed, timeLastModified " +
                "FROM document " +
                "WHERE id = ?");
        st.setInt(1, oid);
        ResultSet res = st.executeQuery();
        res.next();
        d.setOid(oid);
        File f = new File(res.getString(1));
        d.setFile(f);
        d.setTitle(res.getString(2));
        d.setResume(res.getString(3));
        d.setSize(res.getLong(4));
        d.setExtension(res.getString(5));
        d.setTimeIndexed(res.getLong(6));
        d.setTimeLastModified(res.getLong(7));
        res.close();
        if (!st.isClosed()) {
            try {
                st.close();
            } catch (Exception ex) {
                //Nothing
            }
        }
        return d;
    }

    public Document read(String path) throws SQLException {
        Document d = new Document();
        PreparedStatement st = con.prepareStatement(
                "SELECT id, path, title, resume, size, extension, timeIndexed, timeLastModified " +
                "FROM document " +
                "WHERE path like ?");
        st.setString(1, path);
        ResultSet res = st.executeQuery();
        if (!res.next()) {
            return null;
        }
        d.setOid(res.getInt(1));
        File f = new File(res.getString(2));
        d.setFile(f);
        d.setTitle(res.getString(3));
        d.setResume(res.getString(4));
        d.setSize(res.getLong(5));
        d.setExtension(res.getString(6));
        d.setTimeIndexed(res.getLong(7));
        d.setTimeLastModified(res.getLong(8));
        res.close();
        if (!st.isClosed()) {
            try {
                st.close();
            } catch (Exception ex) {
                //Nothing
            }
        }
        return d;
    }

    public List<Document> readAll() throws SQLException {
        List<Document> documents = new LinkedList<Document>();
        PreparedStatement st = con.prepareStatement(
                "SELECT id, path, title, resume, size, extension, timeIndexed, timeLastModified " +
                "FROM document");
        ResultSet res = st.executeQuery();
        while (res.next()) {
            Document d = new Document();
            d.setOid(res.getInt(1));
            File path = new File(res.getString(2));
            d.setFile(path);
            d.setTitle(res.getString(3));
            d.setResume(res.getString(4));
            d.setSize(res.getLong(5));
            d.setExtension(res.getString(6));
            d.setTimeIndexed(res.getLong(7));
            d.setTimeLastModified(res.getLong(8));
            documents.add(d);
        }
        res.close();
        if (!st.isClosed()) {
            try {
                st.close();
            } catch (Exception ex) {
                //Nothing
            }
        }
        return documents;
    }

    public void delete(Document d) throws SQLException {
        PreparedStatement st = con.prepareStatement(
                "DELETE FROM document " +
                " WHERE id = ?");
        st.setInt(1, d.getOid());
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
