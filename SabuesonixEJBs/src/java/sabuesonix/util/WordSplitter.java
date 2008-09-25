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
package sabuesonix.util;

import java.util.Vector;

/**
 * HTMLProcessor class.
 * Busico, Marcelo (marcelobusico@gmail.com)
 * Funes, Franco (funesfranco@gmail.com)
 * Zilocchi, Emiliano (ezilocchi@gmail.com)
 * @version September/2008
 */
public abstract class WordSplitter {

    /**
     * Split the text in a vector with words.
     * @param text Text to split.
     * @return Vector with the words of the text.
     */
    public static final Vector<String> getWordList(String text) {
        Vector<String> vector = new Vector<String>();
        String[] words = text.split("[\\W&&[^áéíóúÁÉÍÓÚÑñ´]]");
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (!word.isEmpty()) {
                vector.add(word);
            }
        }
        return vector;
    }
}
