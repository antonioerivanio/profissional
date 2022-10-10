
CREATE TABLE IF NOT EXISTS public.usuarios
(
    id integer NOT NULL DEFAULT nextval('usuarios_id_seq'::regclass),
    matricula character varying(10) COLLATE pg_catalog."default",
    cpf character varying(11) COLLATE pg_catalog."default",
    email character varying(45) COLLATE pg_catalog."default" NOT NULL,
    password character varying(64) COLLATE pg_catalog."default" NOT NULL,
    enabled boolean,
    nome character varying(70) COLLATE pg_catalog."default",
    login character varying(11) COLLATE pg_catalog."default",
    CONSTRAINT usuarios_pkey PRIMARY KEY (id),
    CONSTRAINT usuarios_matricula_key UNIQUE (matricula)
)

CREATE TABLE grupos (
  id serial,
	usuario_id int,
  	nome varchar(45) NOT NULL,
  	descricao varchar(64) NOT NULL,
 	PRIMARY KEY (id),
	CONSTRAINT fk_usuariosgrupos FOREIGN KEY(usuario_id) REFERENCES usuarios(id)
);
drop table grupos;

CREATE TABLE pessoas (
	id serial,	
	nome varchar(70) not null,
	matricula varchar(10) ,
	cpf varchar(11),
 	ismembrocomissao BOOLEAN DEFAULT false,
	funcao varchar(50) not null,
 	PRIMARY KEY (id),
	UNIQUE (matricula, cpf, nome)
);
drop table pessoas;

CREATE TABLE grupos_usuarios(
	grupo_id int,
	usuario_id int,
	 PRIMARY KEY (grupo_id, usuario_id),
 	CONSTRAINT fk_grupos FOREIGN KEY(grupo_id) REFERENCES grupos(id),
 	CONSTRAINT fk_usuarios FOREIGN KEY(usuario_id) REFERENCES usuarios(id)
)


CREATE TABLE votos(
	id serial,
	usuario_id int,
	tipo varchar(20) not null,
	 PRIMARY KEY (id),
	unique(usuario_id, tipo),
 	CONSTRAINT fk_usuarios FOREIGN KEY(usuario_id) REFERENCES usuarios(id)
)

