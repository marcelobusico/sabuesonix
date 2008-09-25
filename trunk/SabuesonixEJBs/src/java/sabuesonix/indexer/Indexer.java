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

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sabuesonix.entities.Document;
import sabuesonix.entities.Path;
import sabuesonix.entities.PostRow;
import sabuesonix.entities.Vocabulary;
import sabuesonix.entities.Word;
import sabuesonix.entities.persistence.DocumentDB;
import sabuesonix.entities.persistence.ManagementDB;
import sabuesonix.entities.persistence.PoolManager;
import sabuesonix.entities.persistence.PostRowDB;
import sabuesonix.entities.persistence.WordDB;
import sabuesonix.indexer.processors.FileProcessor;
import sabuesonix.indexer.processors.HTMLProcessor;
import sabuesonix.indexer.processors.PDFProcessor;
import sabuesonix.indexer.processors.TXTProcessor;
import sabuesonix.indexer.states.IndexerState;
import sabuesonix.indexer.states.IndexerStateRunning;
import sabuesonix.indexer.states.IndexerStateStopped;
import sabuesonix.indexer.states.IndexerStateStopping;

/**
 * Indexer class.
 * Busico, Marcelo (marcelobusico@gmail.com)
 * Funes, Franco (funesfranco@gmail.com)
 * Zilocchi, Emiliano (ezilocchi@gmail.com)
 * @version September/2008
 */
public class Indexer implements Runnable {

    private static Indexer instance = null;
    private IndexerState state;
    private List<Path> paths;
    private PoolManager pm = PoolManager.getInstance();
    private boolean interrupted = false;
    private String fileNameProcessing = null;

    private Indexer() {
        state = new IndexerStateStopped();
        loadPaths();
    }

    public static Indexer getInstance() {
        if (instance == null) {
            instance = new Indexer();
        }
        return instance;
    }

    private void loadPaths() {
        try {
            pm.connect();
            ManagementDB managementDB = new ManagementDB(pm.getSqlConnection());
            paths = managementDB.readPaths();
        } catch (Exception ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE,
                    "Couldn't load paths from DB.", ex);
            paths = new LinkedList<Path>();
        }
    }

    /**
     * Add the path to the exploration list.
     * @param path Path to add.
     */
    public void addPath(Path path) {
        try {
            pm.connect();
            ManagementDB managementDB = new ManagementDB(pm.getSqlConnection());
            paths.add(path);
            managementDB.savePath(path);
        } catch (Exception ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.WARNING,
                    "Couldn't save current path in DB.", ex);
        }
    }

    /**
     * Remove the path from the exploration list.
     * @param path Path to remove.
     */
    public void removePath(Path path) {
        try {
            pm.connect();
            ManagementDB managementDB = new ManagementDB(pm.getSqlConnection());
            paths.remove(path);
            managementDB.deletePath(path);
        } catch (Exception ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.WARNING,
                    "Couldn't delete current path from DB.", ex);
        }
    }

    /**
     * Get the list of paths where the indexer should be indexing.
     * @return List with Path objects.
     */
    public List<Path> listPaths() {
        return paths;
    }

    public void start() {
        interrupted = false;
        state.start();
    }

    public void stop() {
        state.stop();
    }

    public String getFileNameProcessing() {
        return fileNameProcessing;
    }

    public boolean isInterrupted() {
        return interrupted;
    }

    public void interrupt() {
        state = new IndexerStateStopping();
        interrupted = true;
    }

    /**
     * Verify if the indexer is running.
     * @return true if the indexer is running, false otherwise.
     */
    public boolean isRunning() {
        return state.isRunning();
    }

    /**
     * Verify if the indexer is stopped.
     * @return true if the indexer is stopped, false otherwise.
     */
    public boolean isStopped() {
        return state.isStopped();
    }

    /**
     * Verify if the indexer is stopping now.
     * @return true if the indexer is interrupted and it is stopping, 
     * false otherwise.
     */
    public boolean isStopping() {
        return state.isStopping();
    }

    public String getState() {
        if (state.isRunning()) {
            return "Indexer engine is running now.";
        }
        if (state.isStopped()) {
            return "Indexer engine was stopped.";
        }
        if (state.isStopping()) {
            return "Indexer engine is stopping now.";
        }
        return null;
    }

    public void run() {
        if (isInterrupted()) {
            state = new IndexerStateStopped();
            return;
        }
        try {
            //Connect to database.
            pm.connect();
            state = new IndexerStateRunning();
            //Delete old documents.
            fileNameProcessing = "Verifing pre-indexed documents...";
            verifyDocumentsExistence();
            fileNameProcessing = null;
            if (isInterrupted()) {
                state = new IndexerStateStopped();
                return;
            }
            //Index the documents in the paths.
            for (Path path : paths) {
                exploreDirectory(new File(path.getPath()));
            }
            //Stop the indexer.
            state = new IndexerStateStopped();
            fileNameProcessing = null;
        } catch (Exception ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.WARNING,
                    "Couldn't explore directory to index.", ex);
        }
    }

    private void exploreDirectory(File directory) throws Exception {
        if (directory == null) {
            throw new IllegalArgumentException("Directory null.");
        }
        if (isInterrupted()) {
            state = new IndexerStateStopped();
            return;
        }
        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            return;
        }

        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                exploreDirectory(file);
            } else {
                try {
                    processFile(file);
                } catch (Exception ex) {
                    Logger.getLogger(Indexer.class.getName()).log(Level.INFO,
                            ex.getMessage(), ex);
                }
            }
        }
    }

    private void processFile(File file) throws Exception {
        if (file == null) {
            throw new IllegalArgumentException("File null.");
        }
        if (isInterrupted()) {
            state = new IndexerStateStopped();
            return;
        }
        Document document = new Document(file);

        FileProcessor fp = null;
        //Verify extension.
        int extPos = file.getName().lastIndexOf(".");
        if (extPos == -1) {
            return;
        }
        String ext = file.getName().substring(extPos).toLowerCase();
        String name = file.getName().substring(0, extPos).toLowerCase();
        document.setTitle(name);

        boolean acceptedFormat = false;
        //PDF Files
        if (ext.equals(".pdf")) {
            fp = new PDFProcessor();
            document.setExtension(Document.TYPE_PDF);
            acceptedFormat = true;
        }
        //HTML Files
        if (ext.equals(".htm") || ext.equals(".html")) {
            fp = new HTMLProcessor();
            document.setExtension(Document.TYPE_HTML);
            acceptedFormat = true;
        }
        //Text Files
        if (ext.equals(".txt")) {
            fp = new TXTProcessor();
            document.setExtension(Document.TYPE_TXT);
            acceptedFormat = true;
        }

        if (!acceptedFormat) {
            return;
        }

        if (!file.canRead()) {
            throw new Exception("Couldn't read file " + file.getPath());
        }

        //Verify if document was indexed
        if (isDocumentExists(document)) {
            return;
        }

        Date t_inicio = new Date();
        System.out.println("Processing file " + document.getFile() + "...");
        fileNameProcessing = document.getFile().getName();
        List<Word> words = null;
        try {
            //Extract the words from the document.
            words = fp.process(document);
        } catch (Exception ex) {
            //Delete the incomplete indexed document and throws the exception.
            deleteDocumentFromDB(document);
            throw ex;
        }
        System.out.println("OK.");
        verifyAndPersistWords(words, document);
        //Save the process time.
        DocumentDB docDB = new DocumentDB(pm.getSqlConnection());
        Document aux = docDB.read(document.getOid());
        aux.setTitle(fp.getDocument().getTitle());
        aux.setResume(fp.getDocument().getResume());
        aux.setExtension(document.getExtension());
        Date t_fin = new Date();
        aux.setTimeIndexed(t_fin.getTime() - t_inicio.getTime());
        docDB.save(aux);
        Vocabulary.reload();
        fileNameProcessing = null;
    }

    private void verifyAndPersistWords(List<Word> words, Document doc) throws Exception {
        if (words == null) {
            throw new IllegalArgumentException("Words list null.");
        }
        if (doc == null) {
            throw new IllegalArgumentException("Document null.");
        }
        System.out.println("Loading vocabulary from db... ");
        Vocabulary vocabulary = Vocabulary.loadFromDB(pm.getSqlConnection());
        System.out.println("OK.");
        Hashtable<String, Word> wordsTable = vocabulary.getWords();
        WordDB wdb = new WordDB(pm.getSqlConnection());
        PostRowDB postRowDB = new PostRowDB(pm.getSqlConnection());
        System.out.println("Saving " + words.size() + " words of the document... ");
        for (Word word : words) {
            boolean newWord = false;
            Word aux = null;
            if (wordsTable.isEmpty() || (aux = wordsTable.get(word.getName())) == null) {
                newWord = true;
                wdb.save(word);
                aux = word;
            }
            PostRow postRow = new PostRow();
            postRow.setDocument(doc);
            postRow.setWord(aux);
            postRow.setTf(word.getMaxTf());
            postRowDB.save(postRow);
            if (!newWord) {
                //Update actual word.
                aux = wdb.read(true, aux.getOid());
                aux.addOccurrenceInDocument(doc, false);
                aux.setNr(aux.getNr() + 1);
                wdb.save(aux);
            }
        }
        System.out.println(words.size() + " words saved in the DB.");
    }

    private boolean isDocumentExists(Document newDoc) throws Exception {
        if (newDoc == null) {
            throw new IllegalArgumentException("Document null.");
        }
        try {
            DocumentDB docDB = new DocumentDB(pm.getSqlConnection());
            Document aux = docDB.read(newDoc.getFile().getPath());

            if (aux == null) {
                //Create new file 
                aux = newDoc;
                docDB.save(aux);
                return false;
            }

            if (newDoc.equals(aux)) {
                return true;
            } else {
                //The document exists but is modified then it's indexed again
                deleteDocumentFromDB(aux);
                //Create new file 
                aux = newDoc;
                docDB.save(aux);
                return false;
            }
        } catch (Exception ex) {
            throw new Exception(
                    "Error: Isn't possible verify document existence: " +
                    newDoc.getFile().getPath() + "\n" +
                    ex.getMessage(), ex);
        }
    }

    /**
     * Delete the document form db.
     * @param doc Document to delete.
     */
    private void deleteDocumentFromDB(Document doc) throws SQLException {
        DocumentDB docDB = new DocumentDB(pm.getSqlConnection());
        PostRowDB postRowDB = new PostRowDB(pm.getSqlConnection());
        WordDB wordDB = new WordDB(pm.getSqlConnection());
        List<PostRow> listpr = postRowDB.read(doc);
        for (PostRow postRow : listpr) {
            Word word = postRow.getWord();
            //Delete postrow
            postRowDB.delete(postRow);
            //Update MaxTf an Nr of Word
            wordDB.updateMaxTfAndNr(word);
        }
        //Delete document
        docDB.delete(doc);
    }

    /**
     * Verify the existence of the documents in database and if not exists then
     * delete the file that it is referenced.
     */
    public void verifyDocumentsExistence() throws Exception {
        DocumentDB docs = new DocumentDB(pm.getSqlConnection());
        List<Document> allDocs;
        allDocs = docs.readAll();

        for (Document doc : allDocs) {
            if (isInterrupted()) {
                state = new IndexerStateStopped();
                return;
            }
            if (!doc.getFile().exists()) {
                System.out.println("Deleting document " +
                        doc.getFile().getPath() + "...");
                deleteDocumentFromDB(doc);
                System.out.println("OK.");
            }
        }
    }
}
