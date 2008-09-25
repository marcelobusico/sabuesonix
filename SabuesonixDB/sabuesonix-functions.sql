-- Copyright (C) 2008 Busico, Funes, Zilocchi
-- 
-- Authors:
--    Busico, Marcelo (marcelobusico@gmail.com)
--    Funes, Franco (funesfranco@gmail.com)
--    Zilocchi, Emiliano (ezilocchi@gmail.com)
-- 
-- This program is free software: you can redistribute it and/or modify
-- it under the terms of the GNU General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.
-- 
-- This program is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.
-- 
-- You should have received a copy of the GNU General Public License
-- along with this program.  If not, see <http://www.gnu.org/licenses/>.

DELIMITER ;-
USE sabuesonix;-

-- -------------------------------------------------
-- Save Words
-- -------------------------------------------------
DROP FUNCTION IF EXISTS `fn_saveWord`;-
CREATE FUNCTION `fn_saveWord` (idWord Integer, wname varchar(100), wnr Integer, wmaxTf Integer) RETURNS INTEGER
DETERMINISTIC
BEGIN

Declare var_count Integer;
Declare var_idWord Integer;

    SELECT COUNT(*)
      INTO var_count
      FROM word
      WHERE id = idWord;

    IF (var_count > 0) THEN
      UPDATE word SET
        name = wname,
	nr = wnr,
	maxTf = wmaxTf
      WHERE id = idWord;
    ELSE
      INSERT INTO word(name, nr, maxTf)
        VALUES (wname, wnr, wmaxTf);
        SET var_idWord = LAST_INSERT_ID();
    END IF;
    RETURN var_idWord;
END;-

-- -------------------------------------------------
-- Save Documents
-- -------------------------------------------------
DROP FUNCTION IF EXISTS `fn_saveDocuments`;-
CREATE FUNCTION `fn_saveDocuments` (idDocument Integer, dpath varchar(255), dtitle varchar(150), dresume varchar(500) , dsize Bigint, dextension varchar(100),  dtimeIndexed Bigint, dtimeLastModified Bigint) RETURNS INTEGER
DETERMINISTIC
BEGIN

Declare var_count Integer;
Declare var_idDocument Integer;

    SELECT COUNT(*)
      INTO var_count
      FROM document
      WHERE id = idDocument;

    IF (var_count > 0) THEN
      UPDATE document SET
        path = dpath,
	title= dtitle,
	resume= dresume,
	size = dsize,
	extension=dextension,
	timeIndexed=dtimeIndexed,
	timeLastModified = dtimeLastModified
      WHERE id = idDocument;
    ELSE
      INSERT INTO document(path, title, resume, size, extension, timeIndexed, timeLastModified)
        VALUES (dpath, dtitle, dresume, dsize, dextension, dtimeIndexed, dtimeLastModified);
        SET var_idDocument = LAST_INSERT_ID();
    END IF;
    RETURN var_idDocument;
END;-

-- -------------------------------------------------
-- Save PostRow
-- -------------------------------------------------
DROP FUNCTION IF EXISTS `fn_savePostRow`;-
CREATE FUNCTION `fn_savePostRow` (idpost Bigint, pidDocument Integer, pidWord Integer, ptf Integer) RETURNS BIGINT
DETERMINISTIC
BEGIN

Declare var_count Integer;
Declare var_idPost BIGINT;

    SELECT COUNT(*)
      INTO var_count
      FROM postrow
      WHERE id = idpost;

    IF (var_count > 0) THEN
      UPDATE postrow SET
        idDocument = pidDocument,
	idWord = pidWord,
	tf = ptf
      WHERE id = idpost;
    ELSE
      INSERT INTO postrow(idDocument, idWord, tf)
        VALUES (pidDocument, pidWord, ptf);
        SET var_idPost = LAST_INSERT_ID();
    END IF;
    RETURN var_idPost;
END;-
