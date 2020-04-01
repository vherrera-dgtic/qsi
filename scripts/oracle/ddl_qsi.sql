-- Creacio de l'usuari. Nota: es necessari tenir creat el tablespace QSSI
-- Es fa amb usuari system
create user QSI IDENTIFIED BY zairita13 DEFAULT TABLESPACE QSSI TEMPORARY TABLESPACE TEMP;
commit;
GRANT CONNECT, RESOURCE TO QSI;
GRANT CREATE VIEW TO QSI;
GRANT CREATE SESSION TO QSI;
ALTER USER QSI QUOTA UNLIMITED ON QSSI;

CREATE USER www_qsi identified by tirolita13 default tablespace QSSI TEMPORARY TABLESPACE TEMP;
GRANT CREATE SESSION TO www_qsi;

-- Es fa amb l'usuari   qsi
-- Creacio de la sequencia
CREATE SEQUENCE qsi_sequencia
    INCREMENT BY 1
    START WITH 1
    MINVALUE 1;
    
DROP SEQUENCE qsi_secuencia;
-- Esborram les taules
DROP TABLE QSI_EXPEDIENT;
DROP TABLE QSI_SUBCENTRE;
DROP TABLE QSI_CENTRE;
DROP TABLE QSI_ENTRADA;
DROP TABLE QSI_ESCRIT;
DROP TABLE QSI_FITXER;
DROP TABLE QSI_IDENTIFICACIO;
DROP TABLE QSI_IDIOMA;
DROP TABLE QSI_ILLA;
DROP TABLE QSI_MATERIA;
DROP TABLE QSI_MOTIU;
DROP TABLE QSI_MUNICIPI;
DROP TABLE QSI_QUEIXA;
DROP TABLE QSI_PROVINCIA;
DROP TABLE QSI_SEQUENCIA_EXPEDIENT;


-- Creacio de taules
CREATE TABLE QSI_CENTRE(
	id_centre NUMBER(5) PRIMARY KEY,
    nom VARCHAR2(60) NOT NULL,
    dir3 VARCHAR2(20),
    data_creacio DATE DEFAULT (sysdate),
    usuari VARCHAR(20),
    actiu NUMBER(1,0),
    visible_web NUMBER(1,0));
  
COMMENT ON TABLE QSI_CENTRE IS 'Taula de centres';

CREATE TABLE QSI_ENTRADA(
    id_entrada NUMBER(5) PRIMARY KEY,
    nom VARCHAR2(80) NOT NULL,
    data_creacio DATE DEFAULT (sysdate),
    usuari VARCHAR(20),
    activa NUMBER(1,0));
    
COMMENT ON TABLE QSI_ENTRADA IS 'Tipus d''entrada';

CREATE TABLE QSI_ESCRIT(
    id_escrit NUMBER(5) PRIMARY KEY,
    nom VARCHAR2(80) NOT NULL,
    data_creacio DATE DEFAULT (sysdate),
    usuari VARCHAR(20),
    actiu NUMBER(1,0));
    
COMMENT ON TABLE QSI_ESCRIT IS 'Tipus d''escrit';

CREATE TABLE QSI_FITXER (
    id_fitxer NUMBER(9) PRIMARY KEY,
    id_expedient NUMBER(9),
    id_document_arxiu_caib VARCHAR(80),
    url_arxiu_caib VARCHAR(255));
COMMENT ON TABLE QSI_FITXER IS 'Fitxer generat';


CREATE TABLE QSI_IDENTIFICACIO (
    id_identificacio NUMBER(6) PRIMARY KEY,
    nom VARCHAR2(80) NOT NULL,
    data_creacio DATE DEFAULT (sysdate),
    usuari VARCHAR2(20),
    activa NUMBER(1,0));
COMMENT ON TABLE QSI_IDENTIFICACIO IS 'TODO: saber objecte de la taula';

CREATE TABLE QSI_IDIOMA (
    id_idioma NUMBER(2) PRIMARY KEY,
    nom VARCHAR2(20),
    data_creacio DATE DEFAULT (sysdate),
    usuari VARCHAR2(20),
    actiu NUMBER(1,0));
COMMENT ON TABLE QSI_IDIOMA IS 'Idioma usuari';
    
    
 CREATE TABLE QSI_ILLA (
    id_illa NUMBER(2) PRIMARY KEY,
    nom VARCHAR2(20),
    data_creacio DATE DEFAULT (sysdate),
    usuari VARCHAR2(20),
    activa NUMBER(1,0));
COMMENT ON TABLE QSI_ILLA IS 'TODO: deprecated table';    

 CREATE TABLE QSI_MATERIA (
    id_materia NUMBER(3) PRIMARY KEY,
    nom VARCHAR2(80),
    data_creacio DATE DEFAULT (sysdate),
    usuari VARCHAR2(20),
    activa NUMBER(1,0));
COMMENT ON TABLE QSI_MATERIA IS 'Tipo de materia';  

CREATE TABLE QSI_MOTIU (
    id_motiu NUMBER(3) PRIMARY KEY,
    nom VARCHAR2(80),
    data_creacio DATE DEFAULT (sysdate),
    usuari VARCHAR2(20),
    actiu NUMBER(1,0)); 
COMMENT ON TABLE QSI_MOTIU IS 'Tipus de motiu';

CREATE TABLE QSI_PROVINCIA (
   id_provincia NUMBER(3) PRIMARY KEY,
   nom VARCHAR2(80),
   data_creacio DATE DEFAULT (sysdate),
   usuari VARCHAR2(20),
   activa NUMBER(1,0));
COMMENT ON TABLE QSI_PROVINCIA IS 'Provincia usuari';

CREATE TABLE QSI_MUNICIPI (
    id_municipi NUMBER PRIMARY KEY,
    nom VARCHAR2(80),
    data_creacio DATE DEFAULT (sysdate),
    id_provincia NUMBER(3) REFERENCES QSI_PROVINCIA(id_provincia),
    usuari VARCHAR2(20),
    actiu NUMBER(1,0));
COMMENT ON TABLE QSI_MUNICIPI IS 'Municipi usuari';	

CREATE TABLE QSI_QUEIXA (
    id_queixa NUMBER PRIMARY KEY,
    nom VARCHAR2(80),
    data_creacio DATE DEFAULT (sysdate),
    usuari VARCHAR2(20),
    activa NUMBER(1,0));
COMMENT ON TABLE QSI_QUEIXA IS 'Tipus de queixa';

CREATE TABLE QSI_SEQUENCIA_EXPEDIENT (
    id_sequencia NUMBER PRIMARY KEY,
    valor NUMBER);
COMMENT ON TABLE QSI_SEQUENCIA_EXPEDIENT IS 'Numeracio expedient';

CREATE TABLE QSI_SUBCENTRE (
	id_subcentre NUMBER PRIMARY KEY,
    nom VARCHAR2(255),
    dir3 VARCHAR2(20),
    id_centre NUMBER(5) REFERENCES QSI_CENTRE(id_centre),
    data_creacio DATE DEFAULT (sysdate),
    usuari VARCHAR2(20),
    actiu NUMBER(1,0),
    visible_web NUMBER(1,0));
COMMENT ON TABLE QSI_SUBCENTRE IS 'Subcentre gestor';


CREATE TABLE QSI_EXPEDIENT (
	id_expedient NUMBER PRIMARY KEY,
	assumpte VARCHAR2(255),
    unitat_organica VARCHAR2(255),
	data_entrada DATE DEFAULT (sysdate),
	data_creacio DATE DEFAULT (sysdate),
    via_contestacio VARCHAR2(60),
	id_gestor NUMBER,
	usuari_assignat VARCHAR2(20),
	text_peticio VARCHAR2(255),
	text_resposta VARCHAR2(255),
	data_resposta DATE,
    id_estat NUMBER(2),
	num_identificacio VARCHAR2(20),
	nom VARCHAR2(80),
	llinatge1 VARCHAR2(80),
	llinatge2 VARCHAR2(80),
	telefon VARCHAR2(12),
	email VARCHAR2(60),
	direccio VARCHAR2(160),
	numero VARCHAR2(10),
	pis VARCHAR2(10),
    codi_postal VARCHAR2(10),
    id_expedient_arxiu_caib VARCHAR2(80),
	url_vi_expedient_arxiu_caib VARCHAR2(255),
	id_escrit NUMBER(5) REFERENCES QSI_ESCRIT (id_escrit),
	id_centre NUMBER(5) REFERENCES QSI_CENTRE (id_centre),
    id_subcentre NUMBER REFERENCES QSI_SUBCENTRE (id_subcentre),
    id_materia NUMBER(3) REFERENCES QSI_MATERIA (id_materia),
    id_motiu NUMBER(3) REFERENCES QSI_MOTIU (id_motiu), 
    id_queixa NUMBER REFERENCES QSI_QUEIXA (id_queixa),
	id_entrada NUMBER(5) REFERENCES QSI_ENTRADA (id_entrada),
	id_idioma NUMBER(2) REFERENCES QSI_IDIOMA (id_idioma),
    id_municipi NUMBER REFERENCES QSI_MUNICIPI (id_municipi),
    id_identificacio NUMBER(6) REFERENCES QSI_IDENTIFICACIO (id_identificacio)
);
COMMENT ON TABLE QSI_EXPEDIENT IS 'Expedient QSI';


-- Atorgament dels grants a l'usuari www_qsi
GRANT insert, update, delete, select on qsi_centre to www_qsi;
GRANT insert, update, delete, select on qsi_entrada to www_qsi;
GRANT insert, update, delete, select on qsi_escrit to www_qsi;
GRANT insert, update, delete, select on qsi_fitxer to www_qsi;
GRANT insert, update, delete, select on qsi_identificacio to www_qsi;
GRANT insert, update, delete, select on qsi_idioma to www_qsi;
GRANT insert, update, delete, select on qsi_illa to www_qsi;
GRANT insert, update, delete, select on qsi_materia to www_qsi;
GRANT insert, update, delete, select on qsi_motiu to www_qsi;
GRANT insert, update, delete, select on qsi_provincia to www_qsi;
GRANT insert, update, delete, select on qsi_municipi to www_qsi;
GRANT insert, update, delete, select on qsi_queixa to www_qsi;
GRANT insert, update, delete, select on qsi_sequencia_expedient to www_qsi;
GRANT insert, update, delete, select on qsi_subcentre to www_qsi;
GRANT insert, update, delete, select on qsi_expedient to www_qsi;
GRANT select on qsi.qsi_sequencia to www_qsi;



