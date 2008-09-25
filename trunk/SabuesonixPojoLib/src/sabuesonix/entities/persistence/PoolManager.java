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
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * Pool Manager for DB.
 * @author 
 * Busico, Marcelo (marcelobusico@gmail.com)
 * Funes, Franco (funesfranco@gmail.com)
 * Zilocchi, Emiliano (ezilocchi@gmail.com)
 * @version September/2008
 */
public class PoolManager {

    private static PoolManager instance = null;
    private Connection sqlConnection;
    private String resourceName = "jdbc/sabuesonix"; // "[java:comp/env/]jdbc/<dataSourceName>";
    private DataSource ds;
    private InitialContext context;

    private PoolManager() {
    }

    /**
     * Returns the unique instance of PoolManager.
     */
    public static PoolManager getInstance() {
        if (instance == null) {
            instance = new PoolManager();
        }
        return instance;
    }

    private void setContext() throws Exception {
        if (ds == null) {
            context = new InitialContext();
            ds = (DataSource) context.lookup(resourceName);
            if (ds == null) {
                throw new Exception("jdbc resource not found");
            }
        }
    }

    /**
     * Returns the DB connection.
     */
    public Connection getSqlConnection() {
        return sqlConnection;
    }

    /**
     * Connects with the DB.
     */
    public void connect() throws Exception {
        if (sqlConnection == null) {
            setContext();
            sqlConnection = ds.getConnection();
        }
    }

    /**
     * Disconnects from DB.
     */
    public void disconnect() throws Exception {
        if (sqlConnection != null) {
            sqlConnection.close();
        }
        sqlConnection = null;
        ds = null;
        if (context != null) {
            context.close();
        }
        context = null;
    }

    public void setSqlConnection(Connection sqlConnection) {
        this.sqlConnection = sqlConnection;
    }
}
