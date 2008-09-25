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

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.objects.PdfFileInformation;
import org.jpedal.objects.PdfPageData;
import sabuesonix.entities.Document;
import sabuesonix.entities.Word;

/**
 * PDFProcessor class.
 * Busico, Marcelo (marcelobusico@gmail.com)
 * Funes, Franco (funesfranco@gmail.com)
 * Zilocchi, Emiliano (ezilocchi@gmail.com)
 * @version September/2008
 */
public class PDFProcessor implements FileProcessor {

    private List<Word> allWords = null;
    private File file = null;
    private Document document = null;
    private Exception decoderException = null;
    private boolean decodeSucess = false;

    public PDFProcessor() {
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
                decodePDF();
                decodeSucess = true;
            } catch (Exception ex) {
                decoderException = ex;
            }
        }
    }

    /**
     * Decode the PDF file and set all words found.
     */
    private void decodePDF() throws Exception {
        /**
         * the decoder object which decodes the pdf and returns a data object
         * PdfDecoder returns a PdfException if there is a problem
         */
        PdfDecoder decoder = new PdfDecoder(false);
        boolean decoderError = false;

        allWords = new LinkedList<Word>();

        PdfDecoder.useTextExtraction();

        decoder.setExtractionMode(PdfDecoder.TEXT); //extract just text
        decoder.init(true);

        //always reset to use unaltered co-ords - allow use of rotated or unrotated
        // co-ordinates on pages with rotation (used to be in PdfDecoder)
        PdfGroupingAlgorithms.useUnrotatedCoords = false;

        /**
         * open the file (and read metadata including pages in  file)
         */
        decoder.openPdfFile(file.getPath());

        //Extract the metadata from the document.
        PdfFileInformation currentFileInformation = decoder.getFileInformationData();
        String[] fieldNames = currentFileInformation.getFieldNames();
        for (int i = 0; i < fieldNames.length; i++) {
            String fn = fieldNames[i];
            if (fn.equals("Title")) {
                String title = currentFileInformation.getFieldValues()[i];
                if (title != null && !title.trim().isEmpty()) {
                    document.setTitle(title);
                }
            }
            if (fn.equals("Subject")) {
                String subject = currentFileInformation.getFieldValues()[i];
                if (subject != null && !subject.trim().isEmpty()) {
                    document.setResume(subject);
                }
            }
        }

        /**
         * extract data from pdf (if allowed). 
         */
        if (!decoder.isExtractionAllowed()) {
            throw new Exception("Text extraction not allowed");
        } else if (decoder.isEncrypted() && !decoder.isPasswordSupplied()) {
            throw new Exception("Text is encrypted");
        } else {
            //page range
            int start = 1, end = decoder.getPageCount();

            /**
             * extract data from pdf
             */
            for (int page = start; page < end + 1; page++) { //read pages
                try {
                    try {
                        //decode the page
                        decoder.decodePage(page);
                    } catch (Exception ex) {
                        decoderError = true;
                        throw ex;
                    }

                    //create a grouping object to apply grouping to data
                    PdfGroupingAlgorithms currentGrouping = decoder.getGroupingObject();

                    PdfPageData currentPageData = decoder.getPdfPageData();

                    int x1 = currentPageData.getMediaBoxX(page);
                    int x2 = currentPageData.getMediaBoxWidth(page) + x1;

                    int y2 = currentPageData.getMediaBoxX(page);
                    int y1 = currentPageData.getMediaBoxHeight(page) - y2;

                    //The call to extract the list
                    Vector words = null;

                    //define punctuation
                    try {
                        words = currentGrouping.extractTextAsWordlist(
                                x1,
                                y1,
                                x2,
                                y2,
                                page,
                                false,
                                true, "&:=()!;.,?*\\/\"\"\'\'-“”@");
                    } catch (PdfException e) {
                        decoder.closePdfFile();
                        throw new Exception("Exception= " + e + " in " + file.getPath());
                    }

                    if (words != null) {
                        //Add all words in allWords attribute
                        for (int i = 0; i < words.size(); i = i + 5) {
                            Object strWord = words.get(i);
                            String text = strWord.toString().toLowerCase();
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

                    //remove data once written out
                    decoder.flushObjectValues(false);
                } catch (Exception ex) {
                    System.out.println("Page " + page + " with errors. Ommiting.");
                }
            }
            //flush any text data read
            decoder.flushObjectValues(true);
        }

        //close the pdf file
        if (!decoderError) {
            decoder.closePdfFile();
        }
    }
}
