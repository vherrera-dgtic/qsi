-- Table: "QSSI"."QSI_MATERIA"

-- DROP TABLE "QSSI"."QSI_MATERIA";

-- Haurem de crear els usuaris (?)

-- Permisos a www_qssi;
GRANT SELECT, INSERT, UPDATE, DELETE ON QSI_CENTRE TO www_qssi;
GRANT SELECT, INSERT, UPDATE, DELETE ON QSI_ENTRADA TO www_qssi;
GRANT SELECT, INSERT, UPDATE, DELETE ON QSI_ESCRIT TO www_qssi;
GRANT SELECT, INSERT, UPDATE, DELETE ON QSI_MATERIA TO www_qssi;
GRANT SELECT, INSERT, UPDATE, DELETE ON QSI_MOTIU TO www_qssi;
GRANT SELECT, INSERT, UPDATE, DELETE ON QSI_QUEIXA TO www_qssi;
GRANT SELECT, INSERT, UPDATE, DELETE ON QSI_SUBCENTRE TO www_qssi;
GRANT SELECT, INSERT, UPDATE, DELETE ON QSI_IDIOMA TO www_qssi;
GRANT SELECT, INSERT, UPDATE, DELETE ON QSI_PROVINCIA TO www_qssi;
GRANT SELECT, INSERT, UPDATE, DELETE ON QSI_ILLA TO www_qssi;
GRANT SELECT, INSERT, UPDATE, DELETE ON QSI_MUNICIPI TO www_qssi;
GRANT SELECT, INSERT, UPDATE, DELETE ON QSI_EXPEDIENT TO www_qssi;
GRANT SELECT, INSERT, UPDATE, DELETE ON qsi_sequencia_expedient TO www_qssi;

GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO www_qssi;

CREATE TABLE "QSSI"."QSI_MATERIA"
(
    "ID" integer NOT NULL,
    "NOM" character(256) COLLATE pg_catalog."default" NOT NULL,
    "DATA_CREACIO" date NOT NULL,
    "USUARI" character(10) COLLATE pg_catalog."default" NOT NULL,
    "ACTIVA" boolean NOT NULL,
    CONSTRAINT "QSI_MATERIA_pkey" PRIMARY KEY ("ID")
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE "QSSI"."QSI_MATERIA"
    OWNER to qssi;
COMMENT ON TABLE "QSSI"."QSI_MATERIA"
    IS 'Taula per emmagatzemar matèries';
    
CREATE TABLE "public"."QSI_PROVINCIA"
(
    "id_municipi" integer NOT NULL,
    "nom" character(256) COLLATE pg_catalog."default" NOT NULL,
    "data_creacio" date NOT NULL,
    "usuari" character(255) COLLATE pg_catalog."default" NOT NULL,
    "actiu" boolean NOT NULL,
    CONSTRAINT "QSI_PROVINCIA_pkey" PRIMARY KEY ("id_municipi")
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE "public"."QSI_PROVINCIA"
    OWNER to qssi;
COMMENT ON TABLE "public"."QSI_PROVINCIA"
    IS 'Taula per emmagatzemar provincies';