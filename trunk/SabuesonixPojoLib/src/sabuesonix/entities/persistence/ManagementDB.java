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

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sabuesonix.entities.Path;

/**
 * Persistence class.
 * @author 
 * Busico, Marcelo (marcelobusico@gmail.com)
 * Funes, Franco (funesfranco@gmail.com)
 * Zilocchi, Emiliano (ezilocchi@gmail.com)
 * @version September/2008
 */
public class ManagementDB {

    private Connection con;

    public ManagementDB(Connection con) {
        this.con = con;
    }

    public void savePath(Path p) throws SQLException {
        PreparedStatement st = con.prepareStatement(
                "INSERT INTO path (path) values (?)");
        st.setString(1, p.getPath());
        st.execute();
        if (!st.isClosed()) {
            try {
                st.close();
            } catch (Exception ex) {
                //Nothing
            }
        }
    }

    public List<Path> readPaths() throws SQLException {
        List<Path> retorno = new ArrayList<Path>();
        PreparedStatement st = con.prepareStatement(
                "SELECT p.id, p.path FROM path p");
        ResultSet res = st.executeQuery();
        while (res.next()) {
            Path p = new Path();
            p.setOid(res.getInt(1));
            p.setPath(res.getString(2));
            retorno.add(p);
        }
        res.close();
        if (!st.isClosed()) {
            try {
                st.close();
            } catch (Exception ex) {
                //Nothing
            }
        }
        return retorno;
    }

    public void deletePath(Path p) throws SQLException {
        PreparedStatement st = con.prepareStatement(
                "DELETE FROM path WHERE id = ?");
        st.setInt(1, p.getOid());
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
     * Get the total time of the indexer run indexing files.
     * @return Time in Hours.
     */
    public float getTotalTimeIndexed() throws SQLException {
        PreparedStatement st = con.prepareStatement("Select" +
                " sum(timeIndexed) from document");
        ResultSet rs = st.executeQuery();
        rs.next();
        long aux = rs.getLong(1);
        rs.close();
        if (!st.isClosed()) {
            try {
                st.close();
            } catch (Exception ex) {
                //Nothing
            }
        }
        return ((float) aux) / 3600000f;
    }

    /**
     * Get the total documents indexed in the DB.
     * @return Count of documents.
     */
    public long getDocumentCount() throws SQLException {
        PreparedStatement st = con.prepareStatement("Select" +
                " count(*) from document");
        ResultSet rs = st.executeQuery();
        rs.next();
        long aux = rs.getLong(1);
        rs.close();
        if (!st.isClosed()) {
            try {
                st.close();
            } catch (Exception ex) {
                //Nothing
            }
        }
        return aux;
    }

    /**
     * Get the total size of document indexed.
     * @return Size in Megabytes.
     */
    public float getTotalSizeIndexed() throws SQLException {
        PreparedStatement st = con.prepareStatement("Select" +
                " sum(size) from document");
        ResultSet rs = st.executeQuery();
        rs.next();
        long aux = rs.getLong(1);
        rs.close();
        if (!st.isClosed()) {
            try {
                st.close();
            } catch (Exception ex) {
                //Nothing
            }
        }
        return ((float) aux) / 1024f / 1024f;
    }

    /**
     * Get the total documents indexed in the DB.
     * @return Count of documents.
     */
    public long getWordCount() throws SQLException {
        PreparedStatement st = con.prepareStatement("Select" +
                " count(*) from word");
        ResultSet rs = st.executeQuery();
        rs.next();
        long aux = rs.getLong(1);
        rs.close();
        if (!st.isClosed()) {
            try {
                st.close();
            } catch (Exception ex) {
                //Nothing
            }
        }
        return aux;
    }

    /**
     * Get the total documents indexed in the DB grouped by type.
     * @return Count of documents by type.
     */
    public String getDocumentTypeCount() throws SQLException {
        PreparedStatement st = con.prepareStatement(
                "SELECT count(*), extension from document group by extension");
        ResultSet rs = st.executeQuery();
        String aux = "";
        boolean first = true;
        while (rs.next()) {
            if (!first) {
                aux += "<br/>";
            }
            int cant = rs.getInt(1);
            String type = rs.getString(2);
            aux += type + ": " + cant + " documents.";
            first = false;
        }
        rs.close();
        if (!st.isClosed()) {
            try {
                st.close();
            } catch (Exception ex) {
                //Nothing
            }
        }
        return aux;
    }

    /**
     * Get the average speed of the indexer
     * @return Velocity of indexer, in MiB/Hour.
     */
    public float getAverageSpeed() throws SQLException {
        return Float.valueOf(getTotalSizeIndexed() / getTotalTimeIndexed());
    }
}
