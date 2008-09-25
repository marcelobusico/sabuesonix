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
package sabuesonix.ejbs;

import java.util.List;
import javax.ejb.Stateless;
import sabuesonix.entities.Path;
import sabuesonix.indexer.Indexer;
import sabuesonix.indexer.IndexerStatics;

/**
 * IndexerBean class.
 * Busico, Marcelo (marcelobusico@gmail.com)
 * Funes, Franco (funesfranco@gmail.com)
 * Zilocchi, Emiliano (ezilocchi@gmail.com)
 * @version September/2008
 */
@Stateless
public class IndexerBean implements IndexerRemote {

    /**
     * Show the state of indexer (running, stopped, paused).
     * @return String with the state of indexer.
     */
    public String getIndexerState() {
        return Indexer.getInstance().getState() +
                (Indexer.getInstance().getFileNameProcessing() != null
                ? "<br/>Processing file \"" +
                Indexer.getInstance().getFileNameProcessing() + "\"" : "");
    }

    /**
     * Verify if the indexer is running.
     * @return true if the indexer is running, false otherwise.
     */
    public boolean isRunning() {
        return Indexer.getInstance().isRunning();
    }

    /**
     * Verify if the indexer is stopped.
     * @return true if the indexer is stopped, false otherwise.
     */
    public boolean isStopped() {
        return Indexer.getInstance().isStopped();
    }

    /**
     * Start the search jobs of the indexer if it is stoped.
     */
    public void start() {
        Indexer.getInstance().start();
    }

    /**
     * Stop the indexer if it is started.
     */
    public void stop() {
        Indexer.getInstance().stop();
    }

    /**
     * Get the list of paths where the indexer should be indexing.
     * @return List with Path objects.
     */
    public List<Path> listPaths() {
        return Indexer.getInstance().listPaths();
    }

    /**
     * Add the path to the exploration list.
     * @param path Path to add.
     */
    public void addPath(Path path) {
        Indexer.getInstance().addPath(path);
    }

    /**
     * Remove the path from the exploration list.
     * @param path Path to remove.
     */
    public void removePath(Path path) {
        Indexer.getInstance().removePath(path);
    }

    /**
     * Get the total time of the indexer run indexing files.
     * @return Time in Hours.
     */
    public float getTotalTimeIndexed() {
        return IndexerStatics.getInstance().getTotalTimeIndexed();
    }

    /**
     * Get the total documents indexed in the DB.
     * @return Count of documents.
     */
    public long getDocumentCount() {
        return IndexerStatics.getInstance().getDocumentCount();
    }

    /**
     * Get the total size of document indexed.
     * @return Size in Megabytes.
     */
    public float getTotalSizeIndexed() {
        return IndexerStatics.getInstance().getTotalSizeIndexed();
    }

    /**
     * Get the total documents indexed in the DB.
     * @return Count of documents.
     */
    public long getWordCount() {
        return IndexerStatics.getInstance().getWordCount();
    }

    /**
     * Get the total documents indexed in the DB grouped by type.
     * @return Count of documents by type.
     */
    public String getDocumentTypeCount() {
        return IndexerStatics.getInstance().getDocumentTypeCount();
    }

    /**
     * Get the average speed of the indexer
     * @return Velocity of indexer, in MiB/Hour.
     */
    public float getAverageSpeed() {
        return IndexerStatics.getInstance().getAverageSpeed();
    }
}
