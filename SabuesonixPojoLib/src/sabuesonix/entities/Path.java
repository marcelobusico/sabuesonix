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
package sabuesonix.entities;

import sabuesonix.entities.persistence.PersistentEntity;

/**
 * Path Class.
 * @author 
 * Busico, Marcelo (marcelobusico@gmail.com)
 * Funes, Franco (funesfranco@gmail.com)
 * Zilocchi, Emiliano (ezilocchi@gmail.com)
 * @version September/2008
 */
public class Path implements PersistentEntity {

    private int oid;
    private String path;

    public Path() {
    }

    public Path(String path) {
        this.path = path;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int id) {
        this.oid = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return path;
    }
}
