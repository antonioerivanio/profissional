
DROP TABLE FP_AUXILIOSAUDEREQDOC;
DROP TABLE FP_AUXILIOSAUDEREQDEP;
DROP TABLE FP_AUXILIOSAUDEREQITEM;
DROP TABLE FP_AUXILIOSAUDEREQ;


CREATE TABLE "SRH"."FP_AUXILIOSAUDEREQ" 
   (	"ID" NUMBER(10,0) CONSTRAINT "NN_FP_AUXILIOSAUDEREQ" NOT NULL ENABLE, 
	"IDFUNCIONAL" NUMBER(10,0), 
	"IDUSUARIO" NUMBER(6,0), 	
	"VALOR_TOTALSOLICITADO" NUMBER(12,2), 
	"DT_INICIOREQ" DATE CONSTRAINT "DT_INICIOREQ" NOT NULL ENABLE, 
	"DT_FIMREQ" DATE, 
	"DT_ALTERACAO" DATE,
	"STATUSFUNCIONAL" VARCHAR2(10 BYTE) CONSTRAINT "NN_STATUSFUNCIONAL" NOT NULL ENABLE, 
	"FLG_AFIRMACAOSERVERDADE" CHAR(1 BYTE) DEFAULT 0 CONSTRAINT "NN_AFIRMACAOSERVERDADE" NOT NULL ENABLE, 
	"OBSERVACAO" VARCHAR2(500 BYTE), 
	"STATUSAPROVACAO" VARCHAR2(255 BYTE), 
	 "FLG_DELETADO" NUMBER(1,0),
	 "DT_DELETE" DATE,
    
	 CONSTRAINT "CK_FL_AFIRMACAOSERVERDADE" CHECK (FLG_AFIRMACAOSERVERDADE IN (0,1)) ENABLE, 
	 CONSTRAINT "PK_AUXILIOSAUDEREQ" PRIMARY KEY ("ID"),
 
	 CONSTRAINT "FK_AUXILIOSAUDEREQ_FUNCIONAL" FOREIGN KEY ("IDFUNCIONAL")
	  REFERENCES "SRH"."TB_FUNCIONAL" ("ID") ENABLE, 
      
	 CONSTRAINT "FK_AUXILIOSAUDEREQ_USUARIO" FOREIGN KEY ("IDUSUARIO")
	  REFERENCES "SCA"."USUARIO" ("ID") ENABLE
   )  ;
   
   
    CREATE TABLE "SRH"."FP_AUXILIOSAUDEREQDEP" 
   (	"ID" NUMBER(10,0) CONSTRAINT "NN_AUXILIOSAUDEREQDEP" NOT NULL ENABLE, 
	"IDAUXILIOSAUDEREQ" NUMBER(6,0) CONSTRAINT "NN_ID_AUXILIOSAUDEREQ" NOT NULL ENABLE, 
	"IDDEPENDENTE" NUMBER(6,0) CONSTRAINT "NN_ID_DEPENDENTE" NOT NULL ENABLE, 
	"IDPESSOAJURIDICA" NUMBER(6,0), 
	"VALOR_PLANOSAUDE" NUMBER(5,2), 
	"FLG_DELETADO" CHAR(1 BYTE) DEFAULT 0, 
    
	 CONSTRAINT "PK_AUXILIOSAUDEREQDEP" PRIMARY KEY ("ID"), 
	 CONSTRAINT "FK_DEP_TBDEPENDENTE" FOREIGN KEY ("IDDEPENDENTE")
	  REFERENCES "SRH"."TB_DEPENDENTE" ("ID") ENABLE, 
      
	 CONSTRAINT "FK_DEP_TBPESSOAJURIDICA" FOREIGN KEY ("IDPESSOAJURIDICA")
	  REFERENCES "SRH"."TB_PESSOAJURIDICA" ("ID") ENABLE, 
      
	 CONSTRAINT "FK_FP_AUXILIOSAUDEREQ" FOREIGN KEY ("IDAUXILIOSAUDEREQ")
	  REFERENCES "SRH"."FP_AUXILIOSAUDEREQ" ("ID") ENABLE
   )  ;


   CREATE TABLE "SRH"."FP_AUXILIOSAUDEREQITEM" 
   (	
        "ID" NUMBER(10,0) CONSTRAINT "NN_FP_AUXILIOSAUDEREQITEM" NOT NULL ENABLE, 
        "IDAUXILIOSAUDEREQ" NUMBER(10),
        "IDPESSOAJURIDICA" NUMBER(6,0), 
        "VALOR_PLANOSAUDE" NUMBER(12,2), 	
        "FLG_DELETADO" NUMBER(1,0),
        "DT_DELETE" DATE,
        DT_INCLUSAO DATE,
    
	 CONSTRAINT "CK_FLG_DELETADO" CHECK (FLG_DELETADO IN (0,1)) ENABLE, 
     
	 CONSTRAINT "PK_AUXILIOSAUDEREQITEM" PRIMARY KEY ("ID"),
      
      CONSTRAINT "FK_FP_AUXILIOSAUDEREQ" FOREIGN KEY ("IDAUXILIOSAUDEREQ")
	  REFERENCES "SRH"."FP_AUXILIOSAUDEREQ" ("ID") ENABLE,
      
	 CONSTRAINT "FK_AUXILIOSAUDEREQ_PJ" FOREIGN KEY ("IDPESSOAJURIDICA")
	  REFERENCES "SRH"."TB_PESSOAJURIDICA" ("ID") ENABLE
   )  ;
   
   
   CREATE TABLE FP_AUXILIOSAUDEREQDOC(
    ID NUMBER(10) CONSTRAINT NN_FP_AUXILIOSAUDREQDOC NOT NULL,
    IDAUXILIOSAUDEREQITEM NUMBER(10),
    IDAUXILIOSAUDEREQDEP NUMBER(10),
    CAMINHO_ARQ VARCHAR(200),
    NOME_ARQ VARCHAR(200),
    DESC_ARQUIVO VARCHAR(200),
    DT_INCLUSAO DATE,
    DT_DELETE DATE,
    FLG_DELETADO NUMBER(1) DEFAULT 0,
    
    CONSTRAINT "PK_AUXILIOSAUDEREDOC" PRIMARY KEY ("ID"),
    
    CONSTRAINT "FK_DOC_AUXILIOSAUDEREQITEM" FOREIGN KEY ("IDAUXILIOSAUDEREQITEM")
    REFERENCES "SRH"."FP_AUXILIOSAUDEREQITEM" ("ID"),
    
    CONSTRAINT "FK_DOC_AUXILIOSAUDEREQDEP" FOREIGN KEY("IDAUXILIOSAUDEREQDEP")
    REFERENCES "SRH"."FP_AUXILIOSAUDEREQDEP" ("ID")
);

CREATE SEQUENCE  "SRH"."SEQ_FP_AUXILIOSAUDEREQ"  MINVALUE 1 MAXVALUE 999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE ;
CREATE SEQUENCE  "SRH"."SEQ_FP_AUXILIOSAUDEREQDOC"  MINVALUE 1 MAXVALUE 999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE ;
CREATE SEQUENCE  "SRH"."SEQ_FP_AUXILIOSAUDEREQITEM"  MINVALUE 1 MAXVALUE 999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE ;
CREATE SEQUENCE  "SRH"."SEQ_FP_AUXILIOSAUDEREQDEP"  MINVALUE 1 MAXVALUE 999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE ;
/*usa[ select max(id) +1   from fp_auxiliosaudebase ] o resultado será o valor inicial da sequence*/
CREATE SEQUENCE  "SRH"."SEQ_AUXILIOSAUDEREQBASE"  MINVALUE 1 MAXVALUE 99999 INCREMENT BY 1 START WITH 21 CACHE 20 NOORDER  NOCYCLE ;

--Adicionar um Coluna na tabela pessoa juridica --
ALTER TABLE TB_PESSOAJURIDICA ADD (
	FLGTIPOEMPRESA NUMBER(1)
);



select  SEQ_FP_AUXILIOSAUDEREQDEP.nextval from dual;

