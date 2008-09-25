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
package sabuesonix.indexer.processors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import sabuesonix.entities.Document;
import sabuesonix.entities.Word;
import sabuesonix.util.WordSplitter;

/**
 * TXTProcessor class.
 * Busico, Marcelo (marcelobusico@gmail.com)
 * Funes, Franco (funesfranco@gmail.com)
 * Zilocchi, Emiliano (ezilocchi@gmail.com)
 * @version September/2008
 */
public class TXTProcessor implements FileProcessor {

    private List<Word> allWords = null;
    private File file = null;
    private Document document = null;
    private Exception decoderException = null;
    private boolean decodeSucess = false;

    public TXTProcessor() {
    }

    public List<Word> process(Document document) throws Exception {
        this.file = document.getFile();
        this.document = document;
        Thread decoderThread = new Thread(new Decoder());
        decoderThread.start();
        decoderThread.join();
        if (decoderException != null) {
            throw decoderException;
        }
        if (!decodeSucess) {
            throw new Exception(
                    "Decoder Exception: The document wasn't processed.");
        }
        return allWords;
    }

    public Document getDocument() {
        return document;
    }

    private class Decoder implements Runnable {

        public void run() {
            try {
                //Decode the file.
                decodeTXT();
                decodeSucess = true;
            } catch (Exception ex) {
                decoderException = ex;
            }
        }
    }

    /**
     * Decode the text file and set all words found.
     */
    private void decodeTXT() throws Exception {
        allWords = new LinkedList<Word>();
        FileReader fileReader = new FileReader(document.getFile());
        BufferedReader br = new BufferedReader(fileReader);
        String res = "";
        while (br.ready()) {
            res += " " + br.readLine();
        }
        br.close();
        fileReader.close();
        Vector<String> words = WordSplitter.getWordList(res);

        if (words != null) {
            //Add all words in allWords attribute
            for (int i = 0; i < words.size(); i++) {
                String text = words.get(i).toLowerCase();
                try {
                    Word word = new Word(text, document);
                    int pos = allWords.indexOf(word);
                    if (pos == -1) {
                        allWords.add(word);
                    } else {
                        Word w1 = allWords.get(pos);
                        w1.addOccurrenceInDocument(document, true);
                    }
                } catch (Exception e) {
                    //Invalid word. Nothing to do.
                }
            }
        }
    }
}
