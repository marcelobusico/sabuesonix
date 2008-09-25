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
package sabuesonix.indexer;

import java.util.logging.Level;
import java.util.logging.Logger;
import sabuesonix.entities.persistence.ManagementDB;
import sabuesonix.entities.persistence.PoolManager;

/**
 * Indexer class.
 * Busico, Marcelo (marcelobusico@gmail.com)
 * Funes, Franco (funesfranco@gmail.com)
 * Zilocchi, Emiliano (ezilocchi@gmail.com)
 * @version September/2008
 */
public class IndexerStatics {

    private static IndexerStatics instance = null;
    private PoolManager pm = PoolManager.getInstance();

    private IndexerStatics() {
    }

    public static IndexerStatics getInstance() {
        if (instance == null) {
            instance = new IndexerStatics();
        }
        return instance;
    }

    /**
     * Get the total time of the indexer run indexing files.
     * @return Time in Hours.
     */
    public float getTotalTimeIndexed() {
        float res = 0f;
        try {
            pm.connect();
            ManagementDB managementDB = new ManagementDB(pm.getSqlConnection());
            res = managementDB.getTotalTimeIndexed();
        } catch (Exception ex) {
            Logger.getLogger(IndexerStatics.class.getName()).log(Level.WARNING, null, ex);
        }
        return res;
    }

    /**
     * Get the total documents indexed in the DB.
     * @return Count of documents.
     */
    public long getDocumentCount() {
        long res = 0;
        try {
            pm.connect();
            ManagementDB managementDB = new ManagementDB(pm.getSqlConnection());
            res = managementDB.getDocumentCount();
        } catch (Exception ex) {
            Logger.getLogger(IndexerStatics.class.getName()).log(Level.WARNING, null, ex);
        }
        return res;
    }

    /**
     * Get the total size of document indexed.
     * @return Size in Megabytes.
     */
    public float getTotalSizeIndexed() {
        float res = 0f;
        try {
            pm.connect();
            ManagementDB managementDB = new ManagementDB(pm.getSqlConnection());
            res = managementDB.getTotalSizeIndexed();
        } catch (Exception ex) {
            Logger.getLogger(IndexerStatics.class.getName()).log(Level.WARNING, null, ex);
        }
        return res;
    }

    /**
     * Get the total documents indexed in the DB.
     * @return Count of documents.
     */
    public long getWordCount() {
        long res = 0;
        try {
            pm.connect();
            ManagementDB managementDB = new ManagementDB(pm.getSqlConnection());
            res = managementDB.getWordCount();
        } catch (Exception ex) {
            Logger.getLogger(IndexerStatics.class.getName()).log(Level.WARNING, null, ex);
        }
        return res;
    }

    /**
     * Get the total documents indexed in the DB grouped by type.
     * @return Count of documents by type.
     */
    public String getDocumentTypeCount() {
        String res = "";
        try {
            pm.connect();
            ManagementDB managementDB = new ManagementDB(pm.getSqlConnection());
            res = managementDB.getDocumentTypeCount();
        } catch (Exception ex) {
            Logger.getLogger(IndexerStatics.class.getName()).log(Level.WARNING, null, ex);
        }
        return res;
    }

    /**
     * Get the average speed of the indexer
     * @return Velocity of indexer, in MiB/Hour.
     */
    public float getAverageSpeed() {
        float res = 0f;
        try {
            pm.connect();
            ManagementDB managementDB = new ManagementDB(pm.getSqlConnection());
            res = managementDB.getAverageSpeed();
        } catch (Exception ex) {
            Logger.getLogger(IndexerStatics.class.getName()).log(Level.WARNING, null, ex);
        }
        return res;
    }
}
