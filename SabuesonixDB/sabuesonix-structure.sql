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


-- User (Only execute in first run)
--CREATE USER 'sabuesonix' IDENTIFIED BY 'sabuesonix';

-- DB Schema
DROP DATABASE IF EXISTS sabuesonix;
CREATE DATABASE sabuesonix;
USE sabuesonix;

-- Word Table
DROP TABLE IF EXISTS word CASCADE;
CREATE TABLE word (
	id 		INTEGER			NOT NULL AUTO_INCREMENT,
	name		VARCHAR(100)		NOT NULL,
	nr		INTEGER			NOT NULL,
	maxTf		INTEGER			NOT NULL,
	PRIMARY KEY(id),
	CHECK(name <> ''),
	CHECK(nr > 0),
	CHECK(maxtf > 0)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
GRANT ALL ON word TO 'sabuesonix'@'%';

-- Document Table
DROP TABLE IF EXISTS document CASCADE;
CREATE TABLE document (
	id 		INTEGER			NOT NULL AUTO_INCREMENT,
	path		VARCHAR(255)		NOT NULL,
	title		VARCHAR(150)		,
	resume		VARCHAR(500)		,
	size		BIGINT			NOT NULL,
	extension	VARCHAR(100)		NOT NULL,
	timeIndexed	BIGINT			NOT NULL,
	timeLastModified	BIGINT		NOT NULL,
	PRIMARY KEY(id),
	CHECK (path <> ''),
	CHECK (url <> '')
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
GRANT ALL ON document TO 'sabuesonix'@'%';

-- Post Table
DROP TABLE IF EXISTS postrow CASCADE;
CREATE TABLE postrow (
	id		BIGINT			NOT NULL AUTO_INCREMENT,
	idDocument	INTEGER			NOT NULL,
	idWord		INTEGER			NOT NULL,
	tf		INTEGER			NOT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY(idDocument)			REFERENCES document(id),
	FOREIGN	KEY(idWord)			REFERENCES word(id),
	CHECK(tf > 0)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
GRANT ALL ON post TO 'sabuesonix'@'%';

-- Path Table
DROP TABLE IF EXISTS path CASCADE;
CREATE TABLE path (
	id 		INTEGER			NOT NULL AUTO_INCREMENT,
	path		VARCHAR(500)		NOT NULL,
	PRIMARY KEY(id),
	CHECK (path <> '')
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
GRANT ALL ON path TO 'sabuesonix'@'%';
