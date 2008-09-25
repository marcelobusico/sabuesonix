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
import javax.ejb.Remote;
import sabuesonix.entities.Path;

/**
 * IndexerRemote class.
 * Busico, Marcelo (marcelobusico@gmail.com)
 * Funes, Franco (funesfranco@gmail.com)
 * Zilocchi, Emiliano (ezilocchi@gmail.com)
 * @version September/2008
 */
@Remote
public interface IndexerRemote {

    /**
     * Show the state of indexer (running, stopped, paused).
     * @return String with the state of indexer.
     */
    String getIndexerState();

    /**
     * Verify if the indexer is running.
     * @return true if the indexer is running, false otherwise.
     */
    boolean isRunning();

    /**
     * Verify if the indexer is stopped.
     * @return true if the indexer is stopped, false otherwise.
     */
    boolean isStopped();

    /**
     * Start the search jobs of the indexer if it is stoped.
     */
    void start();

    /**
     * Stop the indexer if it is started.
     */
    void stop();

    /**
     * Get the list of paths where the indexer should be indexing.
     * @return List with Path objects.
     */
    List<Path> listPaths();

    /**
     * Add the path to the exploration list.
     * @param path Path to add.
     */
    void addPath(Path path);

    /**
     * Remove the path from the exploration list.
     * @param path Path to remove.
     */
    void removePath(Path path);

    /**
     * Get the total time of the indexer run indexing files.
     * @return Time in Hours.
     */
    float getTotalTimeIndexed();

    /**
     * Get the total documents indexed in the DB.
     * @return Count of documents.
     */
    long getDocumentCount();

    /**
     * Get the total size of document indexed.
     * @return Size in Megabytes.
     */
    float getTotalSizeIndexed();

    /**
     * Get the total documents indexed in the DB.
     * @return Count of documents.
     */
    long getWordCount();

    /**
     * Get the total documents indexed in the DB grouped by type.
     * @return Count of documents by type.
     */
    String getDocumentTypeCount();

    /**
     * Get the average speed of the indexer
     * @return Velocity of indexer, in MiB/Hour.
     */
    float getAverageSpeed();
}
