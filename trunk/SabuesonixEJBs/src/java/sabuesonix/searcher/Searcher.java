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
package sabuesonix.searcher;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import sabuesonix.entities.Document;
import sabuesonix.entities.PostRow;
import sabuesonix.entities.Vocabulary;
import sabuesonix.entities.Word;
import sabuesonix.entities.persistence.PoolManager;
import sabuesonix.entities.persistence.WordDB;

/**
 * Searcher class.
 * Busico, Marcelo (marcelobusico@gmail.com)
 * Funes, Franco (funesfranco@gmail.com)
 * Zilocchi, Emiliano (ezilocchi@gmail.com)
 * @version September/2008
 */
public class Searcher {

    public Searcher() {
    }

    public List<Document> search(String phrase) throws SQLException {
        String[] keywords = phrase.split("\\s");
        Vocabulary voc = Vocabulary.getInstance();
        List<Word> words = new LinkedList<Word>();
        for (int i = 0; i < keywords.length; i++) {
            String keyword = keywords[i];
            Word w = voc.find(keyword);
            if (w != null) {
                words.add(w);
            } else {
                return null;
            }
        }
        if (words.isEmpty()) {
            return null;
        }
        List<Document> docs = processWords(words);
        return docs;
    }

    /**
     * Process the words list and return documents found
     * ordered by relevance.
     * @param words Words of the documents.
     * @return List of Documents ordered by relevance.
     */
    private List<Document> processWords(List<Word> words) throws SQLException {
        List<Document> docs = new LinkedList<Document>();
        words = orderByNr(words);
        //Get the SQL Connection
        WordDB wdb = new WordDB(PoolManager.getInstance().getSqlConnection());
        List<Word> wordsWithPostRows = new LinkedList<Word>();

        //Get documents that contains all words searched.
        boolean firstWord = true;
        for (Word word : words) {
            Word w = wdb.read(true, word.getOid());
            wordsWithPostRows.add(w);
            if (firstWord) {
                List<PostRow> postRows = orderByTf(word.getPostRows());
                for (PostRow pr : postRows) {
                    docs.add(pr.getDocument());
                }
            } else {
                //Mark documents for delete from list.
                for (Document document : docs) {
                    List<Document> docAux = new LinkedList<Document>();
                    for (PostRow pr : word.getPostRows()) {
                        docAux.add(pr.getDocument());
                    }
                    if (!docAux.contains(document)) {
                        document.setOid(-1);
                    }
                }
                //Delete marked documents from list.
                List<Document> docAux = new LinkedList<Document>();
                for (Document document : docs) {
                    if (document.getOid() != -1) {
                        docAux.add(document);
                    }
                }
                docs = docAux;
                if (docs.isEmpty()) {
                    break;
                }
            }
            firstWord = false;
        }

        //Get the other documents by relevance.
        for (Word word : wordsWithPostRows) {
            if (word.getNr() != 0) {
                List<PostRow> postRows = orderByTf(word.getPostRows());
                for (PostRow pr : postRows) {
                    if (!docs.contains(pr.getDocument())) {
                        docs.add(pr.getDocument());
                    }
                }
            }
        }
        return docs;
    }

    private List<Word> orderByNr(List<Word> words) {
        Word[] orderedKeywords = new Word[words.size()];
        int counter = 0;
        for (Word word : words) {
            orderedKeywords[counter] = word;
            counter++;
        }
        orderedKeywords = insertionMethod(orderedKeywords);
        //Convert vector to list ordered
        List<Word> listOrdered = new LinkedList<Word>();
        for (Word word : orderedKeywords) {
            listOrdered.add(word);
        }
        return listOrdered;
    }

    private List<PostRow> orderByTf(List<PostRow> postRows) {
        PostRow[] orderedKeywords = new PostRow[postRows.size()];
        int counter = 0;
        for (PostRow pr : postRows) {
            orderedKeywords[counter] = pr;
            counter++;
        }
        orderedKeywords = insertionMethod(orderedKeywords);
        //Convert vector to list ordered
        List<PostRow> listOrdered = new LinkedList<PostRow>();
        for (PostRow pr : orderedKeywords) {
            listOrdered.add(pr);
        }
        return listOrdered;
    }

    /**
     * Order the word array by Nr.
     * @param vector Array with words.
     * @return Array with ordered words.
     */
    private Word[] insertionMethod(Word[] vector) {
        // Direct Insertion Method
        // Order:  n^2
        Word y;
        int i, k;
        for (k = 1; k < vector.length; k++) {
            y = vector[k];
            for (i = k - 1; i >= 0 && y.getNr() < vector[i].getNr(); i--) {
                vector[i + 1] = vector[i];
            }
            vector[i + 1] = y;
        }
        return vector;
    }

    /**
     * Order the post row array by Tf.
     * @param vector Array with post rows.
     * @return Array with ordered post rows.
     */
    private PostRow[] insertionMethod(PostRow[] vector) {
        // Direct Insertion Method
        // Order:  n^2
        PostRow y;
        int i, k;
        for (k = 1; k < vector.length; k++) {
            y = vector[k];
            for (i = k - 1; i >= 0 && y.getTf() > vector[i].getTf(); i--) {
                vector[i + 1] = vector[i];
            }
            vector[i + 1] = y;
        }
        return vector;
    }
}