
insert into qsi_Centre (id_centre, nom, dir3,usuari,actiu,visible_web) values (1,'Conselleria Administracions Publiques i Modernitzacio','A04026906','u97091',1,1);
insert into qsi_Centre (id_centre, nom, dir3,usuari,actiu,visible_web) values (2,'Conselleria Universitats i Recerca','564244234','u97091',1,1);
insert into qsi_Centre (id_centre, nom, dir3,usuari,actiu,visible_web) values (3,'Conselleria de Presidencia','5245244234','u97091',1,1);
commit;

insert into qsi_subcentre (id_subcentre, nom, dir3, id_centre,usuari,actiu,visible_web) values (qsi_secuencia.nextval,'Direccio General de Funcio Publica','A04026908',1,'u97091',1,1);
insert into qsi_subcentre (id_subcentre, nom, dir3, id_centre,usuari,actiu,visible_web) values (qsi_secuencia.nextval,'Direccio General de Modernitzacio i Administracio Digital','A04027005',1,'u97091',1,1);
insert into qsi_subcentre (id_subcentre, nom, dir3, id_centre,usuari,actiu,visible_web) values (qsi_secuencia.nextval,'Secretaria General de AAPP i Modernitzacio','A04027049',1,'u97091',1,1);
commit;

insert into qsi_Entrada (id_entrada, nom, usuari,activa) values (1,'Queixa','u97091',1);
insert into qsi_Entrada (id_entrada, nom, usuari,activa) values (2,'Suggeriment','u97091',1);

insert into qsi_escrit (id_escrit, nom, usuari,actiu) values (1,'Presencial','u97091',1);
insert into qsi_escrit (id_escrit, nom, usuari,actiu) values (2,'Telefon / Fax','u97091',1);

insert into qsi_idioma(id_idioma, nom, usuari, actiu) values (1,'Catala','u97091',1);
insert into qsi_idioma(id_idioma, nom, usuari, actiu) values (2,'Castella','u97091',1);

insert into qsi_provincia(id_provincia, nom, usuari, activa) values (1,'Illes Balears','u97091',1);
insert into qsi_provincia(id_provincia, nom, usuari, activa) values (2,'Madrid','u97091',1);
insert into qsi_provincia(id_provincia, nom, usuari, activa) values (3,'Catalunya','u97091',1);
insert into qsi_provincia(id_provincia, nom, usuari, activa) values (4,'País Basc','u97091',1);

insert into qsi_municipi(id_municipi, nom, id_provincia, usuari, actiu) values (1,'Palma de Mallorca',1,'u97091',1);
insert into qsi_municipi(id_municipi, nom, id_provincia, usuari, actiu) values (2,'Sencelles',1,'u97091',1);
insert into qsi_municipi(id_municipi, nom, id_provincia, usuari, actiu) values (3,'Marratxí',1,'u97091',1);
insert into qsi_municipi(id_municipi, nom, id_provincia, usuari, actiu) values (4,'Madrid',2,'u97091',1);
insert into qsi_municipi(id_municipi, nom, id_provincia, usuari, actiu) values (5,'Barcelona',3,'u97091',1);
insert into qsi_municipi(id_municipi, nom, id_provincia, usuari, actiu) values (6,'Bilbao',4,'u97091',1);

commit;

select qsi_sequencia.nextval from dual;

insert into qsi_identificacio(id_identificacio, nom, usuari, activa) values (qsi_sequencia.nextval,'DNI','u97091',1);
insert into qsi_identificacio(id_identificacio, nom, usuari, activa) values (qsi_sequencia.nextval,'NIF','u97091',1);
insert into qsi_identificacio(id_identificacio, nom, usuari, activa) values (qsi_sequencia.nextval,'Passport','u97091',1);

commit;

select * from qsi_sequencia_Expedient;
select * from qsi_expedient;




