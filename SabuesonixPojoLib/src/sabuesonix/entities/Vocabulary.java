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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sabuesonix.entities.persistence.PoolManager;
import sabuesonix.entities.persistence.WordDB;

/**
 * Entity Class. It represents the vocabulary of all indexed documents.
 * @author 
 * Busico, Marcelo (marcelobusico@gmail.com)
 * Funes, Franco (funesfranco@gmail.com)
 * Zilocchi, Emiliano (ezilocchi@gmail.com)
 * @version September/2008
 */
public class Vocabulary {

    private Hashtable<String, Word> words;
    private static Vocabulary instance = null;
    private static Connection con;
    

    static {
        PoolManager pm = PoolManager.getInstance();
        try {
            pm.connect();
        } catch (Exception ex) {
            Logger.getLogger(Vocabulary.class.getName()).log(Level.SEVERE, null, ex);
        }
        con = pm.getSqlConnection();
        if (con == null) {
            Logger.getLogger(Vocabulary.class.getName()).log(Level.SEVERE,
                    "Couldn't get connection with database.");
        }
    }

    private Vocabulary() {
    }

    public static Vocabulary getInstance() {
        if (instance == null) {
            reload();
        }
        return instance;
    }

    public Hashtable<String, Word> getWords() {
        return words;
    }

    public void setWords(Hashtable<String, Word> words) {
        this.words = words;
    }

    /**
     * Find the word identified by the word String in the vocabulary.
     * @param word String to find.
     * @return Word found in vocabulary with the postrows loaded, 
     * or null if the word isn't there.
     */
    public Word find(String word) {
        Word w = words.get(word);
        if (w == null) {
            return null;
        }
        WordDB wdb = new WordDB(con);
        try {
            w = wdb.read(true, w.getOid());
        } catch (SQLException ex) {
            Logger.getLogger(Vocabulary.class.getName()).log(Level.SEVERE, null, ex);
        }
        return w;
    }

    public void saveToDB(Connection con) throws SQLException {
        WordDB wdb = new WordDB(con);
        for (Word word : words.values()) {
            wdb.save(word);
        }
    }

    public static Vocabulary loadFromDB(Connection con) throws SQLException {
        WordDB wdb = new WordDB(con);
        Vocabulary vocabulary = new Vocabulary();
        Hashtable<String, Word> ht = new Hashtable<String, Word>();
        vocabulary.setWords(ht);
        List<Word> words = wdb.readAll();
        for (Word word : words) {
            ht.put(word.getName(), word);
        }
        return vocabulary;
    }

    public static void reload() {
        try {
            instance = Vocabulary.loadFromDB(con);
        } catch (SQLException ex) {
            Logger.getLogger(Vocabulary.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
