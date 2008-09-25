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
package sabuesonix.indexer.states;

import sabuesonix.indexer.Indexer;

/**
 * IndexerStateRunning class.
 * Busico, Marcelo (marcelobusico@gmail.com)
 * Funes, Franco (funesfranco@gmail.com)
 * Zilocchi, Emiliano (ezilocchi@gmail.com)
 * @version September/2008
 */
public class IndexerStateRunning implements IndexerState {

    /**
     * Verify if the indexer is running.
     * @return true if the indexer is running, false otherwise.
     */
    public boolean isRunning() {
        return true;
    }

    /**
     * Verify if the indexer is stopped.
     * @return true if the indexer is stopped, false otherwise.
     */
    public boolean isStopped() {
        return false;
    }

    /**
     * Verify if the indexer is stopping now.
     * @return true if the indexer is interrupted and it is stopping, 
     * false otherwise.
     */
    public boolean isStopping() {
        return false;
    }

    /**
     * Start the search jobs of the indexer if it is stoped.
     */
    public void start() {
        throw new IllegalStateException("The indexer is running.");
    }

    /**
     * Stop the indexer if it is started or paused.
     */
    public void stop() {
        Indexer.getInstance().interrupt();
    }
}
