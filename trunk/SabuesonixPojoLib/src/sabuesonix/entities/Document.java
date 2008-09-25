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

import java.io.File;
import sabuesonix.entities.persistence.PersistentEntity;

/**
 * Entity Class. It represents a Document File.
 * @author 
 * Busico, Marcelo (marcelobusico@gmail.com)
 * Funes, Franco (funesfranco@gmail.com)
 * Zilocchi, Emiliano (ezilocchi@gmail.com)
 * @version September/2008
 */
public class Document implements PersistentEntity, Comparable<Document> {

    private int oid;
    private File file;
    private String title;
    private String resume;
    private String extension;
    private long timeLastModified = 0;
    private long timeIndexed = 0;
    private long size = 0;
    public static final String TYPE_TXT = "Plain Text File";
    public static final String TYPE_HTML = "Hypertext Document";
    public static final String TYPE_PDF = "PDF Document";

    public Document() {
    }

    public Document(File file) {
        this.file = file;
        timeLastModified = file.lastModified();
        size = file.length();
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public long getTimeLastModified() {
        return timeLastModified;
    }

    public void setTimeLastModified(long timeLastModified) {
        this.timeLastModified = timeLastModified;
    }

    public long getTimeIndexed() {
        return timeIndexed;
    }

    public void setTimeIndexed(long timeIndexed) {
        this.timeIndexed = timeIndexed;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Document other = (Document) obj;
        if (this.file != other.file && (this.file == null || !this.file.equals(other.file))) {
            return false;
        }
        if (this.timeLastModified != other.timeLastModified) {
            return false;
        }
        if (this.size != other.size) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.file != null ? this.file.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return file.getPath();
    }

    public int compareTo(Document other) {
        return file.getName().compareTo(other.getFile().getName());
    }
}
