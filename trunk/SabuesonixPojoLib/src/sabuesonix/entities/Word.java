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

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import sabuesonix.entities.persistence.PersistentEntity;

/**
 * Entity class. It represents a Word.
 * @author 
 * Busico, Marcelo (marcelobusico@gmail.com)
 * Funes, Franco (funesfranco@gmail.com)
 * Zilocchi, Emiliano (ezilocchi@gmail.com)
 * @version September/2008
 */
public class Word implements PersistentEntity, Comparable {

    private int oid;
    private String name;
    private List<PostRow> postRows;
    private int nr;
    private int maxTf;

    public Word() {
        postRows = new LinkedList<PostRow>();
    }

    public Word(String name, Document doc) throws Exception {
        String nameOK = correctAcuteAccent(name);
        verifyName(nameOK);
        this.name = nameOK;
        postRows = new LinkedList<PostRow>();
        PostRow pr = new PostRow();
        pr.setTf(1);
        pr.setWord(this);
        pr.setDocument(doc);
        postRows.add(pr);
        maxTf = 1;
        nr = 1;
    }

    public String correctAcuteAccent(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        int index = name.indexOf("´");
        if (index == -1 || index == (name.length() - 1)) {
            return name;
        }

        String letter = name.substring(index + 1, index + 2);
        String acuteLetter = null;
        if (letter.equals("a")) {
            acuteLetter = "á";
        }
        if (letter.equals("e")) {
            acuteLetter = "é";
        }
        if (letter.equals("i")) {
            acuteLetter = "í";
        }
        if (letter.equals("o")) {
            acuteLetter = "ó";
        }
        if (letter.equals("u")) {
            acuteLetter = "ú";
        }

        if (acuteLetter == null) {
            return name;
        }

        String newName = name.substring(0, index) + acuteLetter +
                name.substring(index + 2);

        return newName;
    }

    private void verifyName(String name) throws Exception {
        if (name == null || name.isEmpty()) {
            throw new Exception("The word is invalid.");
        }
        try {
            Pattern p = Pattern.compile("[a-zA-Z0-9áéíóúÁÉÍÓÚÑñ]+");
            Matcher m = p.matcher(name);
            if (!m.matches()) {
                throw new Exception("The word is invalid.");
            }
        } catch (PatternSyntaxException e) {
            System.out.println("Pattern invalid.");
        }
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PostRow> getPostRows() {
        return postRows;
    }

    public void setPostRows(List<PostRow> postRows) {
        this.postRows = postRows;
    }

    public int getMaxTf() {
        return maxTf;
    }

    public void setMaxTf(int maxTf) {
        this.maxTf = maxTf;
    }

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public void addOccurrenceInDocument(Document doc, boolean incrementTf) {
        int newTf = 0;
        if (postRows == null) {
            throw new IllegalStateException("The postrows variable is null.");
        }
        for (PostRow postRow : postRows) {
            if (postRow.getDocument().equals(doc)) {
                if (incrementTf) {
                    newTf = postRow.incrementTf();
                } else {
                    newTf = postRow.getTf();
                }
                break;
            }
        }
        if (newTf > maxTf) {
            maxTf = newTf;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Word other = (Word) obj;
        if (this.name == null || !this.name.equalsIgnoreCase(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    public int compareTo(Object obj) {
        if (obj == null) {
            return -1;
        }
        if (getClass() != obj.getClass()) {
            return -1;
        }
        final Word other = (Word) obj;
        return name.compareTo(other.getName());
    }

    @Override
    public String toString() {
        return name + "MaxTF: " + maxTf + "nr: " + nr;
    }
}
