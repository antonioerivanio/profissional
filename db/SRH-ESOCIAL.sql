  CREATE TABLE "SRH"."ESOCIAL_REABERTURAFOLHA" 
   (	"ID" NUMBER(10,0) CONSTRAINT "NN_ESOCIAL_REABERTURAFOLHA" NOT NULL ENABLE, 
	"REFERENCIA" VARCHAR2(30 BYTE), 
	"TP_INSC" NUMBER(1,0), 
	"NR_INSC" VARCHAR2(14 BYTE), 
	"PER_APUR" VARCHAR2(7 BYTE), 
    "IND_APURACAO" NUMBER(1,0),    
	 CONSTRAINT "PK_ESOCIAL_REABERTURAFOLHA" PRIMARY KEY ("ID"), 
	 CONSTRAINT "UN_ESOCIAL_REABERTURAFOLHA" UNIQUE ("REFERENCIA")
   ) ;
   
  GRANT REFERENCES,SELECT ON SRH.ESOCIAL_REABERTURAFOLHA TO CONECTOR_ESOCIAL;
   
   
 CREATE TABLE "SRH"."ESOCIAL_TERMINOVINCULO" 
   (	"ID" NUMBER(9,0), 
	"IDFUNCIONAL" NUMBER(9,0) NOT NULL ENABLE, 
	"REFERENCIA" VARCHAR2(30 BYTE), 
	"TP_INSC_EMPREGADOR" NUMBER(1,0), 
	"NR_INSC_EMPREGADOR" VARCHAR2(15 BYTE), 
	"CPF_TRAB" VARCHAR2(14 BYTE), 
	"MATRICULA" VARCHAR2(30 BYTE), 
	"COD_CATEGORIA" NUMBER(10,0), 
	"DT_TERM" DATE, 
	"NR_PROCTRABALHO" VARCHAR2(20 BYTE), 	
	"TP_INSC_LOTACAO" NUMBER(1,0), 
	"NR_INSC_LOTACAO" VARCHAR2(15 BYTE), 
	"COD_LOTACAO" NUMBER(10,0), 
	"DT_FIMQUAR" DATE, 
	 CONSTRAINT "PK_ESOCIAL_TERMINOVINCULO" PRIMARY KEY ("ID"),
     CONSTRAINT  "UN_ESOCIAL_TERMINOVINCULO" UNIQUE(REFERENCIA),
	 CONSTRAINT "FK_TERMINOVINCULO_FUNCIONAL" FOREIGN KEY ("IDFUNCIONAL")
	  REFERENCES "SRH"."TB_FUNCIONAL" ("ID") ENABLE
   );

   
 CREATE TABLE "SRH"."ESOCIAL_DESLIGAMENTO" 
   (	"ID" NUMBER(9,0), 
	"IDFUNCIONAL" NUMBER(9,0) NOT NULL ENABLE, 
	"REFERENCIA" VARCHAR2(30 BYTE), 
	"MTV_DESLIG" VARCHAR2(2 BYTE), 
	"DT_DESLIG" DATE, 
	"CPF_TRAB" VARCHAR2(14 BYTE), 
	"MATRICULA" VARCHAR2(30 BYTE), 
	"NM_TRAB" VARCHAR2(70 BYTE), 
	"NR_PROCTRABALHO" VARCHAR2(20 BYTE), 
	"OBSERVACAO" VARCHAR2(255 BYTE), 
	"TP_INSC_EMPREGADOR" NUMBER(1,0), 
	"NR_INSC_EMPREGADOR" VARCHAR2(15 BYTE), 
	"COD_LOTACAO" NUMBER(10,0), 
	"TP_INSC_LOTACAO" NUMBER(1,0), 
	"NR_INSC_LOTACAO" VARCHAR2(15 BYTE), 
	"GRAU_EXP" NUMBER, 
	 CONSTRAINT "PK_ESOCIAL_DESLIGAMENTO" PRIMARY KEY ("ID") , 
	 CONSTRAINT "UN_ESOCIAL_DESLIGAMENTO" UNIQUE ("REFERENCIA"), 
	 CONSTRAINT "FK_DESLIGAMENTO_FUNCIONAL" FOREIGN KEY ("IDFUNCIONAL")
	  REFERENCES "SRH"."TB_FUNCIONAL" ("ID") ENABLE
   );
   

   

CREATE TABLE "SRH"."ESOCIAL_EVENTO_VIGENCIA" (
    "ID"                   NUMBER(9, 0) NOT NULL ENABLE,
    "INICIOVALIDADE"       DATE,
    "FIMVALIDADE"          DATE,
    "INICIONOVAVALIDADE"   DATE,
    "FIMNOVAVALIDADE"      DATE,
    "FLEXCLUIDO"           NUMBER(1, 0) DEFAULT 0 NOT NULL ENABLE,
    "FLTRANSMITIDO"        NUMBER(1, 0) DEFAULT 0 NOT NULL ENABLE,
    "REFERENCIA"           VARCHAR2(100 BYTE),
    "EVENTO"               VARCHAR2(5 BYTE),
    CONSTRAINT "ESOCIAL_EVENTO_VIGENCIA_PK" PRIMARY KEY ( "ID" )
);

COMMENT ON COLUMN "SRH"."ESOCIAL_EVENTO_VIGENCIA"."INICIOVALIDADE" IS 'Deve ser uma data válida, igual ou posterior à data inicial de implantação do eSocial. Ao utilizar esse campo no XML colocar no formato AAAA-MM.';
COMMENT ON COLUMN "SRH"."ESOCIAL_EVENTO_VIGENCIA"."FIMVALIDADE" IS 'Deve ser uma data válida, posterior à data de início de validade informada anteriormente. Ao utilizar esse campo no XML colocar no formato AAAA-MM.';

GRANT SELECT ON SRH.esocial_evento_vigencia TO CONECTOR_ESOCIAL;
GRANT SELECT ON SRH.esocial_empregador TO CONECTOR_ESOCIAL;

DROP TABLE SRH.esocial_empregador PURGE; 

CREATE TABLE "SRH"."ESOCIAL_EMPREGADOR" (
    "ID"                        NUMBER,
    "TIPOINSCRICAO"             NUMBER(1, 0),
    "CNPJ"                      VARCHAR2(14 BYTE),
    "NOMEORGAO"                 VARCHAR2(100 BYTE),
    "CLASSIFICACAOTRIBUTARIA"   VARCHAR2(2 BYTE),
    "NATUREZAJURIDICA"          VARCHAR2(4 BYTE),
    "REGISTROEMPREGADOS"        NUMBER(1, 0) DEFAULT 0,
    "ENTIDADEEDUCATIVA"         CHAR(1 BYTE) DEFAULT 'N',
    "TRABALHOTEMPORARIO"        CHAR(1 BYTE) DEFAULT 'N',
    "ENTEFEDERATIVO"            CHAR(1 BYTE),
    "ENTEFEDERATIVOCNPJ"        VARCHAR2(14 BYTE),
    "ENTEFEDERATIVONOME"        VARCHAR2(100 BYTE),
    "ENTEFEDERATIVOUF"          CHAR(2 BYTE),
    "ENTEFEDERATIVOMUNICIPIO"   VARCHAR2(7 BYTE),
    "ENTEFEDERATIVORPPS"        CHAR(1 BYTE),
    "SUBTETO"                   NUMBER(1, 0),
    "VALORSUBTETO"              NUMBER(14, 2),
    "SITUACAO"                  NUMBER(1, 0),
    "DESONERACAOFOLHA"          NUMBER(1, 0) DEFAULT 0,
    "IDCONTATO"                 NUMBER(4, 0),
    "IDESOCIALVIGENCIA"         NUMBER(9, 0),
    "COOPERATIVA"               NUMBER(1, 0) DEFAULT 0,
    "CONSTRUTORA"               NUMBER(1, 0) DEFAULT 0,
    "NRSIAFI"                   CHAR(1 BYTE) DEFAULT 'N',
    CONSTRAINT "PK_ESOCIAL_EMPREGADOR" PRIMARY KEY ( "ID" ),
    CONSTRAINT "ESOCIAL_EMPREGADOR_FK1" FOREIGN KEY ( "IDESOCIALVIGENCIA" ) REFERENCES "SRH"."ESOCIAL_EVENTO_VIGENCIA" ( "ID" )
);

COMMENT ON COLUMN "SRH"."ESOCIAL_EMPREGADOR"."ID" IS 'Chave Primária da Tabela';
COMMENT ON COLUMN "SRH"."ESOCIAL_EMPREGADOR"."TIPOINSCRICAO" IS 'Código correspondente ao tipo de inscrição, conforme tabela 5 do eSocial. [1] CNPJ [2] CPF';
COMMENT ON COLUMN "SRH"."ESOCIAL_EMPREGADOR"."CNPJ" IS 'Inscrição no CNPJ. No eSocial selecionar 8 primeiros dígitos (Raiz/Base).';
COMMENT ON COLUMN "SRH"."ESOCIAL_EMPREGADOR"."NOMEORGAO" IS 'Razão Social';
COMMENT ON COLUMN "SRH"."ESOCIAL_EMPREGADOR"."CLASSIFICACAOTRIBUTARIA" IS 'Preencher com o código correspondente à classificação tributária do contribuinte, conforme tabela 8 do eSocial.';
COMMENT ON COLUMN "SRH"."ESOCIAL_EMPREGADOR"."NATUREZAJURIDICA" IS 'Preencher com o código da Natureza Jurídica do Contribuinte, conforme tabela 21 do eSocial.';
COMMENT ON COLUMN "SRH"."ESOCIAL_EMPREGADOR"."REGISTROEMPREGADOS" IS 'Indica se houve opção pelo registro eletrônico de empregados. [0] Não optou pelo registro eletrônico de empregados; [1] Optou pelo registro eletrônico de empregados';
COMMENT ON COLUMN "SRH"."ESOCIAL_EMPREGADOR"."ENTIDADEEDUCATIVA" IS 'Indicativo de entidade educativa sem fins lucrativos que tenha por objetivo a assistência ao adolescente e à educação profissional (art. 430, inciso II, CLT) ou de entidade de prática desportiva filiada ao Sistema Nacional do Desporto ou a Sistema de Desporto de Estado, do Distrito Federal ou de Município (art. 430, inciso III, CLT). Aceitar apenas [S]/[N]';
COMMENT ON COLUMN "SRH"."ESOCIAL_EMPREGADOR"."TRABALHOTEMPORARIO" IS  'Indicativo de Empresa de Trabalho Temporário (Lei n° 6.019/1974), com registro no Ministério do Trabalho: [N] - Não é Empresa de Trabalho Temporário; [S] - Empresa de Trabalho Temporário.';
COMMENT ON COLUMN "SRH"."ESOCIAL_EMPREGADOR"."ENTEFEDERATIVO" IS 'Informar se o Órgão Público é o Ente Federativo Responsável - EFR ou se é uma unidade administrativa autônoma vinculada a um EFR. [S]-É uma EFR [N]-Não é EFR.';
COMMENT ON COLUMN "SRH"."ESOCIAL_EMPREGADOR"."ENTEFEDERATIVOCNPJ" IS 'CNPJ do Ente Federativo Responsável - EFR Validação: Preenchimento obrigatório se ENTEFEDERATIVO = [N]';
COMMENT ON COLUMN "SRH"."ESOCIAL_EMPREGADOR"."ENTEFEDERATIVONOME" IS 'Nome do Ente Federativo ao qual o órgão está vinculado';
COMMENT ON COLUMN "SRH"."ESOCIAL_EMPREGADOR"."ENTEFEDERATIVOUF" IS 'Preencher com a sigla da Unidade da Federação';
COMMENT ON COLUMN "SRH"."ESOCIAL_EMPREGADOR"."ENTEFEDERATIVOMUNICIPIO" IS  'Preencher com o código do município, conforme tabela do IBGE';
COMMENT ON COLUMN "SRH"."ESOCIAL_EMPREGADOR"."ENTEFEDERATIVORPPS" IS 'Informar se o ente público possui Regime Próprio de Previdência Social - RPPS. [S]-Sim [N]-Não';
COMMENT ON COLUMN "SRH"."ESOCIAL_EMPREGADOR"."SUBTETO" IS 'Preencher com o poder a que se refere o subteto: 1 - Executivo; 2 - Judiciário; 3 - Legislativo; 9 - Todos os poderes.';
COMMENT ON COLUMN "SRH"."ESOCIAL_EMPREGADOR"."VALORSUBTETO" IS 'Preencher com o valor do subteto do Ente Federativo.';
COMMENT ON COLUMN "SRH"."ESOCIAL_EMPREGADOR"."SITUACAO" IS 'Indicativo da Situação da Pessoa Jurídica: 0 - Situação Normal; 1 - Extinção; 2 - Fusão; 3 - Cisão; 4 - Incorporação.';
COMMENT ON COLUMN "SRH"."ESOCIAL_EMPREGADOR"."DESONERACAOFOLHA" IS 'Indicativo de Desoneração da Folha: 0 - Não Aplicável; 1 - Empresa enquadrada nos art. 7º a 9º da Lei 12.546/2011. Validação: Pode ser igual a [1] apenas se a classificação tributária for igual a [02,03,99]. Nos demais casos, deve ser igual a [0]. Valores Válidos: 0, 1.';
COMMENT ON TABLE "SRH"."ESOCIAL_EMPREGADOR" IS 'Tabela que representa o evento S-1000 contendo as informações cadastrais, alíquotas e demais dados necessários ao preenchimento e validação dos demais eventos do eSocial, inclusive para apuração das contribuições previdenciárias. Esse é o primeiro evento que deve ser transmitido pelo empregador/contribuinte/órgão público. Não pode ser enviado qualquer outro evento antes deste.';

INSERT INTO "SRH"."ESOCIAL_EMPREGADOR" (ID, TIPOINSCRICAO, CNPJ, NOMEORGAO, CLASSIFICACAOTRIBUTARIA, NATUREZAJURIDICA, REGISTROEMPREGADOS, ENTIDADEEDUCATIVA, TRABALHOTEMPORARIO, ENTEFEDERATIVO, ENTEFEDERATIVOCNPJ, ENTEFEDERATIVONOME, ENTEFEDERATIVOUF, ENTEFEDERATIVOMUNICIPIO, ENTEFEDERATIVORPPS, SUBTETO, VALORSUBTETO, SITUACAO, DESONERACAOFOLHA, COOPERATIVA, CONSTRUTORA, NRSIAFI) VALUES ('1', '1', '09499757000146', 'TRIBUNAL DE CONTAS DO CEARÁ', '85', '1058', '0', 'N', 'N', 'N', '07954480000179', 'CEARA', 'CE', '2304400', 'S', '2', '25322,25', '0', '0', '0', '0', 'N');

DROP TABLE SRH.esocial_estabelecimento PURGE;

CREATE TABLE "SRH"."ESOCIAL_ESTABELECIMENTO" (
    "ID"                  NUMBER(4, 0),
    "TIPOINSCRICAO"       NUMBER(1, 0),
    "NRINSCRICAO"         VARCHAR2(15 BYTE),
    "CNAE"                NUMBER(7, 0),
    "RAT"                 NUMBER(1, 0),
    "FAP"                 NUMBER(5, 4),
    "RAT_AJUSTADO"        NUMBER(5, 4),
    "RAT_PROCESSOTIPO"    NUMBER(1, 0),
    "RAT_PROCESSONR"      VARCHAR2(21 BYTE),
    "RAT_CODSUSPENSAO"    NUMBER(14, 0),
    "FAP_PROCESSOTIPO"    NUMBER(1, 0),
    "FAP_PROCESSONR"      VARCHAR2(21 BYTE),
    "FAP_CODSUSPENSAO"    NUMBER(14, 0),
    "REGISTROPONTO"       NUMBER(1, 0),
    "APRENDIZCONTRATO"    NUMBER(1, 0),
    "APRENDIZPROCESSO"    VARCHAR2(20 BYTE),
    "APRENDIZENTIDADE"    CHAR(1 BYTE),
    "PCDCONTRATO"         NUMBER(1, 0),
    "PCDPROCESSO"         VARCHAR2(20 BYTE),
    "IDESOCIALVIGENCIA"   NUMBER(9, 0),
    CONSTRAINT "PK_ESOCIAL_ESTABELECIMENTO" PRIMARY KEY ( "ID" ),
    CONSTRAINT "ESOCIAL_ESTABELECIMENTO_FK1" FOREIGN KEY ( "IDESOCIALVIGENCIA" ) REFERENCES "SRH"."ESOCIAL_EVENTO_VIGENCIA" ( "ID" )
);

COMMENT ON COLUMN "SRH"."ESOCIAL_ESTABELECIMENTO"."ID" IS 'Chave Primária da Tabela';
COMMENT ON COLUMN "SRH"."ESOCIAL_ESTABELECIMENTO"."TIPOINSCRICAO" IS 'Preencher com o código correspondente ao tipo de inscrição do estabelecimento ou obra, conforme tabela 5 Validação: Deve ser igual a [1] (CNPJ), [4] CNO (Cadastro Nacional de Obra).';
COMMENT ON COLUMN "SRH"."ESOCIAL_ESTABELECIMENTO"."NRINSCRICAO" IS  'Informar o número de inscrição do estabelecimento, obra de construção civil ou órgão público de acordo com o tipo de inscrição indicado no campo TIPOINSCRICAO.';
COMMENT ON COLUMN "SRH"."ESOCIAL_ESTABELECIMENTO"."CNAE" IS 'Preencher com o código do CNAE (Classificação Nacional de Atividade Econômica) conforme tabela do Anexo V do Regulamento da Previdência Social, referente a atividade econômica preponderante do estabelecimento. Validação: Deve ser um número existente na tabela CNAE.';
COMMENT ON COLUMN "SRH"."ESOCIAL_ESTABELECIMENTO"."RAT" IS 'Preencher com a alíquota definida para o RAT (Risco Ambiental do Trabalho) na legislação vigente para a atividade (CNAE) preponderante. A divergência só é permitida se existir o registro complementar com informações sobre o processo administrativo/judicial que permite a aplicação de alíquotas diferentes. Validação: Deve ser igual a 1, 2 ou 3. Se a alíquota informada for diferente da definida na legislação vigente para o CNAE informado deverá haver informações de processo';
COMMENT ON COLUMN "SRH"."ESOCIAL_ESTABELECIMENTO"."FAP" IS 'Fator Acidentário de Prevenção - FAP. Validação: Preenchimento obrigatório pela Pessoa Jurídica. Não preencher para Pessoa Física. O FAP informado deve corresponder àquele definido pelo Órgão Governamental Competente para o estabelecimento. A divergência só é permitida se houver processo informado em {procAdmJudFap}. Deve ser um número maior ou igual a 0,5000 e menor ou igual a 2,0000.';
COMMENT ON COLUMN "SRH"."ESOCIAL_ESTABELECIMENTO"."RAT_AJUSTADO" IS 'Alíquota do RAT após ajuste pelo FAP Validação: Deve corresponder ao resultado da multiplicação dos campos RAT e FAP. Preenchimento obrigatório pela Pessoa Jurídica.';
COMMENT ON COLUMN "SRH"."ESOCIAL_ESTABELECIMENTO"."RAT_PROCESSOTIPO" IS 'Preencher com o código correspondente ao tipo de processo: 1 - Administrativo; 2 - Judicial. Valores Válidos: 1, 2.';
COMMENT ON COLUMN "SRH"."ESOCIAL_ESTABELECIMENTO"."RAT_PROCESSONR" IS 'Informar um número de processo cadastrado através do evento S-1070, cujo {indMatProc} seja igual a [1]. Validação: Deve ser um número de processo administrativo ou judicial válido e existente na Tabela de Processos (S-1070).';
COMMENT ON COLUMN "SRH"."ESOCIAL_ESTABELECIMENTO"."RAT_CODSUSPENSAO" IS 'Código do Indicativo da Suspensão, atribuído pelo empregador. Validação: A informação prestada deve estar de acordo com o que foi informado em S-1070.';
COMMENT ON COLUMN "SRH"."ESOCIAL_ESTABELECIMENTO"."FAP_PROCESSOTIPO" IS 'Preencher com o código correspondente ao tipo de processo: 1 - Administrativo; 2 - Judicial; 4 - Processo FAP. Valores Válidos: 1, 2, 4.';
COMMENT ON COLUMN "SRH"."ESOCIAL_ESTABELECIMENTO"."FAP_PROCESSONR" IS 'Informar um número de processo cadastrado através do evento S-1070, cujo {indMatProc} seja igual a [1]. Validação: Deve ser um número de processo administrativo ou judicial válido e existente na Tabela de Processos (S-1070).';
COMMENT ON COLUMN "SRH"."ESOCIAL_ESTABELECIMENTO"."FAP_CODSUSPENSAO" IS 'Código do Indicativo da Suspensão, atribuído pelo empregador em S-1070. Validação: A informação prestada deve estar de acordo com o que foi informado em S-1070.';
COMMENT ON COLUMN "SRH"."ESOCIAL_ESTABELECIMENTO"."REGISTROPONTO" IS  'Opção de registro de ponto (jornada) adotada pelo estabelecimento. Indicar o sistema de controle de ponto preponderante, conforme opções: 0 - Não utiliza; 1 - Manual; 2 - Mecânico; 3 - Eletrônico (portaria MTE 1.510/2009); 4 - Não eletrônico alternativo (art. 1° da Portaria MTE 373/2011); 5 - Eletrônico alternativo ( art. 2° da Portaria MTE 373/2011); 6 - Eletrônico - outros. Valores Válidos: 0, 1, 2, 3, 4, 5, 6.';
COMMENT ON COLUMN "SRH"."ESOCIAL_ESTABELECIMENTO"."APRENDIZCONTRATO" IS  'Indicativo de contratação de aprendiz: 0 - Dispensado de acordo com a lei; 1 - Dispensado, mesmo que parcialmente, em virtude de processo judicial; 2 - Obrigado. Valores Válidos: 0, 1, 2.';
COMMENT ON COLUMN "SRH"."ESOCIAL_ESTABELECIMENTO"."APRENDIZPROCESSO" IS 'Preencher com o número do processo judicial. Validação: O preenchimento é obrigatório se {contApr} for igual a [1]. Deve ser um número de processo judicial válido e existente na Tabela de Processos - S-1070.';
COMMENT ON COLUMN "SRH"."ESOCIAL_ESTABELECIMENTO"."APRENDIZENTIDADE" IS 'Informar se o estabelecimento realiza a contratação de aprendiz por intermédio de entidade educativa sem fins lucrativos que tenha por objetivo a assistência ao adolescente e à educação profissional (art. 430, inciso II, CLT) ou por entidade de prática desportiva filiada ao Sistema Nacional do Desporto ou a Sistema de Desporto de Estado, do Distrito Federal ou de Município (art. 430, inciso III, CLT): S - Sim; N - Não. Validação: O preenchimento é obrigatório se {contApr} for igual a [1, 2]. Valores Válidos: S, N.';
COMMENT ON COLUMN "SRH"."ESOCIAL_ESTABELECIMENTO"."PCDCONTRATO" IS 'Indicativo de contratação de PCD: 0 - Dispensado de acordo com a lei; 1 - Dispensado, mesmo que parcialmente, em virtude de processo judicial; 2 - Com exigibilidade suspensa, mesmo que parcialmente em virtude de Termo de Compromisso firmado com o Ministério do Trabalho; 9 - Obrigado. Valores Válidos: 0, 1, 2, 9.';
COMMENT ON COLUMN "SRH"."ESOCIAL_ESTABELECIMENTO"."PCDPROCESSO" IS 'Preencher com o número do processo judicial. Validação: Informação obrigatória se {contPCD} = [1]. Deve ser um número de processo administrativo ou judicial válido e existente na Tabela de Processos - S-1070.';
COMMENT ON TABLE "SRH"."ESOCIAL_ESTABELECIMENTO" IS  'Tabela que representa o evento S-1005, detalhando as informações de cada estabelecimento (matriz e filiais) do empregador/contribuinte/órgão público, como: informações relativas ao CNAE preponderante, Fator Acidentário de Prevenção - FAP, alíquota GILRAT, dentre outras. As informações prestadas no evento são utilizadas na apuração das contribuições incidentes sobre as remunerações dos trabalhadores dos referidos estabelecimentos. O órgão público informará as suas respectivas unidades, individualizadas por CNPJ, como estabelecimento.';

INSERT INTO "SRH"."ESOCIAL_ESTABELECIMENTO" (ID, TIPOINSCRICAO, NRINSCRICAO, CNAE, RAT, FAP, RAT_AJUSTADO, REGISTROPONTO, APRENDIZCONTRATO, PCDCONTRATO) VALUES ('1', '1', '09499757000146', '8411600', '2', '0,5', '1', '6', '0', '0');

GRANT SELECT ON SRH.esocial_estabelecimento TO CONECTOR_ESOCIAL;

CREATE TABLE "SRH"."ESOCIAL_RUBRICACONFIG" (	
	"ID" 				NUMBER(6,0), 
	"CODIGORUBRICA" 	VARCHAR2(30 BYTE), 
	"IDTABELARUBRICA" 	NUMBER(4,0), 
	"DESCRICAO" 		VARCHAR2(100 BYTE), 
	"TIPO" 				NUMBER(1,0), 
	"CODIGOPREVID" 		VARCHAR2(2 BYTE), 
	"CODIGOIRRF" 		VARCHAR2(2 BYTE), 
	"CODIGOFGTS" 		VARCHAR2(2 BYTE), 
	"CODIGOSINDICATO" 	VARCHAR2(2 BYTE), 
	"OBSERVACAO" 		VARCHAR2(255 BYTE), 
	"IDRUBRICATCE" 		NUMBER(3,0), 
	"IDRUBRICAESOCIAL" 	NUMBER(4,0), 
	"IDESOCIALVIGENCIA" NUMBER(9,0), 
	 CONSTRAINT "PK_ESOCIALRUBRICACONFIG" PRIMARY KEY ("ID"), 
	 CONSTRAINT "ESOCIAL_RUBRICACONFIG_FK2" FOREIGN KEY ("IDRUBRICATCE") REFERENCES "SRH"."TB_RUBRICA" ("ID") ENABLE, 
	 CONSTRAINT "ESOCIAL_RUBRICACONFIG_FK3" FOREIGN KEY ("IDRUBRICAESOCIAL") REFERENCES "SRH"."ESOCIAL_RUBRICA" ("ID") ENABLE, 
	 CONSTRAINT "ESOCIAL_RUBRICACONFIG_FK1" FOREIGN KEY ("IDTABELARUBRICA") REFERENCES "SRH"."ESOCIAL_RUBRICA_TABELA" ("ID") ENABLE, 
	 CONSTRAINT "ESOCIAL_RUBRICACONFIG_FK4" FOREIGN KEY ("IDESOCIALVIGENCIA") REFERENCES "SRH"."ESOCIAL_EVENTO_VIGENCIA" ("ID") ENABLE
   );

COMMENT ON COLUMN "SRH"."ESOCIAL_RUBRICACONFIG"."ID" IS 'Chave Primária da Tabela';
COMMENT ON COLUMN "SRH"."ESOCIAL_RUBRICACONFIG"."CODIGORUBRICA" IS 'Informar o código atribuído pela empresa e que identifica a rubrica em sua folha de pagamento. Validação: O código não pode conter a expressão "eSocial" nas 7 (sete) primeiras posições.';
COMMENT ON COLUMN "SRH"."ESOCIAL_RUBRICACONFIG"."IDTABELARUBRICA" IS 'Representa o campo "ideTabRubr" do evento S-1010 para diferenciar sua tabela de rubricas, quando necessário. Por exemplo, se um empregador tem quatro estabelecimentos e cada um adota uma tabela de rubricas diferente e, por exemplo, o código 0001 representa a verba salário no estabelecimento matriz, a verba horas extras na filial 1, a verba adicional noturno na filial 2 e a verba comissões na filial 3, o empregador poderá criar na tabela de rubricas do eSocial a indicação, 001, 002, 003 e 004 no campo "ideTabRubr" e, assim, permanecer utilizando o código 0001 acima referido.';
COMMENT ON COLUMN "SRH"."ESOCIAL_RUBRICACONFIG"."DESCRICAO" IS 'Informar a descrição (nome) da rubrica no sistema de folha de pagamento da empresa.';
COMMENT ON COLUMN "SRH"."ESOCIAL_RUBRICACONFIG"."TIPO" IS 'Tipo de rubrica: [1] - Vencimento, provento ou pensão; [2] - Desconto; [3] - Informativa; [4] - Informativa dedutora.';
COMMENT ON COLUMN "SRH"."ESOCIAL_RUBRICACONFIG"."CODIGOPREVID" IS 'Código de incidência tributária da rubrica para a Previdência Social: 00 - Não é base de cálculo; 01 - Não é base de cálculo em função de acordos internacionais de previdência social; Base de cálculo das contribuições sociais - Salário de Contribuição: 11 - Mensal; 12 - 13o Salário; 13 - Exclusiva do Empregador - mensal; 14 - Exclusiva do Empregador - 13° salário; 15 - Exclusiva do segurado - mensal; 16 - Exclusiva do segurado - 13° salário; 21 - Salário maternidade mensal pago pelo Empregador; 22 - Salário maternidade - 13o Salário, pago pelo Empregador; 25 - Salário maternidade mensal pago pelo INSS; 26 - Salário maternidade - 13° salário, pago pelo INSS; Contribuição descontada do Segurado sobre salário de contribuição: 31 - Mensal; 32 - 13o Salário; 34 - SEST; 35 - SENAT; Outros: 51 - Salário-família; Suspensão de incidência sobre Salário de Contribuição em decorrência de decisão judicial: 91 - Mensal; 92 - 13o Salário; 93 - Salário maternidade; 94 - Salário maternidade 13o salário; 95 - Exclusiva do Empregador - mensal; 96 - Exclusiva do Empregador - 13º salário; 97 - Exclusiva do Empregador - Salário maternidade; 98 - Exclusiva do Empregador - Salário maternidade 13º salário. Validação: Para utilização dos códigos [91,92,93,94,95,96,97,98], é necessária a existência de registro complementar com informações de processo.';
COMMENT ON COLUMN "SRH"."ESOCIAL_RUBRICACONFIG"."CODIGOIRRF" IS 'Código de incidência tributária da rubrica para o IRRF:  09 - Verba transitada pela folha de pagamento de natureza diversa de rendimento ou retenção/isenção/dedução de IR; Rendimento tributável (base de cálculo do IR): 11 - Remuneração mensal; 12 - 13º Salário; 13 - Férias; 14 - PLR; Retenção do IRRF efetuada sobre: 31 - Remuneração mensal; 32 - 13º Salário; 33 - Férias; 34 - PLR; Dedução do rendimento tributável do IRRF: 41 - Previdência Social Oficial - PSO - Remuner. mensal; 42 - PSO - 13º salário; 43 - PSO - Férias; 46 - Previdência Privada - salário mensal; 47 - Previdência Privada - 13º salário; 48 - Previdência privada - Férias; 51 - Pensão Alimentícia - Remuneração mensal; 52 - Pensão Alimentícia - 13º salário; 53 - Pensão Alimentícia - Férias; 54 - Pensão Alimentícia - PLR; 61 - Fundo de Aposentadoria Programada Individual - FAPI - Remuneração mensal; 62 - Fundo de Aposentadoria Programada Individual - FAPI - 13º salário; 63 - Fundação de Previdência Complementar do Servidor Público - Funpresp - Remuneração mensal; 64 - Fundação de Previdência Complementar do Servidor Público - Funpresp - 13º salário; 65 - Fundação de previdência complementar do servidor público - Férias; 66 - Fundo de Aposentadoria Programada Individual - FAPI - Férias; 67 - Plano privado coletivo de assistência à saúde;	 Rendimento não tributável ou isento do IRRF: 70 - Parcela Isenta 65 anos - Remuneração mensal; 71 - Parcela Isenta 65 anos - 13º salário; 72 - Diárias; 73 - Ajuda de custo; 74 - Indenização e rescisão de contrato, inclusive a título de PDV e acidentes de trabalho; 75 - Abono pecuniário; 76 - Pensão, aposentadoria ou reforma por moléstia grave ou acidente em serviço - Remuneração Mensal; 77 - Pensão, aposentadoria ou reforma por moléstia grave ou acidente em serviço - 13º salário; 700 - Auxílio moradia; 701 - Parte não tributável do valor de serviço de transporte de passageiros ou cargas; 79 - Outras isenções (o nome da rubrica deve ser claro para identificação da natureza dos valores); Exigibilidade suspensa - Rendimento tributável (base de cálculo do IR): 9011 - Remuneração mensal; 9012 - 13º salário; 9013 - Férias; 9014 - PLR; Exigibilidade suspensa - Retenção do IRRF efetuada sobre: 9031 - Remuneração mensal; 9032 - 13º salário; 9033 - Férias; 9034 - PLR; 9831 - Depósito judicial - Mensal; 9832 - Depósito judicial - 13º salário; 9833 - Depósito judicial - Férias; 9834 - Depósito judicial - PLR; Exigibilidade suspensa - Dedução da base de cálculo do IRRF: 9041 - Previdência Social Oficial - PSO - Remuneração mensal; 9042 - PSO - 13º salário; 9043 - PSO - Férias; 9046 - Previdência privada - Salário mensal; 9047 - Previdência privada - 13º salário; 9048 - Previdência privada - Férias; 9051 - Pensão alimentícia - Remuneração mensal; 9052 - Pensão alimentícia - 13º salário; 9053 - Pensão alimentícia - Férias; 9054 - Pensão alimentícia - PLR; 9061 - Fundo de Aposentadoria Programada Individual - FAPI - Remuneração mensal; 9062 - Fundo de Aposentadoria Programada Individual - FAPI - 13º salário; 9063 - Fundação de previdência complementar do servidor público - Remuneração mensal; 9064 - Fundação de previdência complementar do servidor público - 13º salário; 9065 - Fundação de previdência complementar do servidor público - Férias; 9066 - Fundo de Aposentadoria Programada Individual - FAPI - Férias; 9067 - Plano privado coletivo de assistência à saúde; Compensação judicial: 9082 - Compensação judicial do ano-calendário; 9083 - Compensação judicial de anos anteriores.';
COMMENT ON COLUMN "SRH"."ESOCIAL_RUBRICACONFIG"."CODIGOFGTS" IS 'Código de incidência da rubrica para o FGTS: 00 - Não é base de cálculo do FGTS, 11 - Base de cálculo do FGTS mensal, 12 - Base de cálculo do FGTS 13° salário, 21 - Base de cálculo do FGTS aviso prévio indenizado, 91 - Incidência suspensa em decorrência de decisão judicial - FGTS mensal, 92 - Incidência suspensa em decorrência de decisão judicial - FGTS 13º salário, 93 - Incidência suspensa em decorrência de decisão judicial - FGTS aviso prévio indenizado. Validação: Para utilização de código [91, 92, 93], é necessária a existência de grupo com informações relativas ao processo.';
COMMENT ON COLUMN "SRH"."ESOCIAL_RUBRICACONFIG"."CODIGOSINDICATO" IS 'Código de incidência tributária da rubrica para a Contribuição Sindical Laboral: 00 - Não é base de cálculo; 11 - Base de cálculo; 31 - Valor da contribuição sindical laboral descontada; 91 - Incidência suspensa em decorrência de decisão judicial Validação: No caso de preenchimento com o código 91, é necessária a existência de registro complementar com informações do processo.';
COMMENT ON COLUMN "SRH"."ESOCIAL_RUBRICACONFIG"."OBSERVACAO" IS 'Observações relacionadas à rubrica ou à sua utilização.';
COMMENT ON COLUMN "SRH"."ESOCIAL_RUBRICACONFIG"."IDRUBRICAESOCIAL" IS 'Informar o código de classificação da rubrica de acordo com a Tabela 3 - Tabela de Natureza das Rubricas da Folha de Pagamento. Validação: Deve ser um código existente na Tabela 3 - Tabela de Natureza das Rubricas da Folha de Pagamento.';
COMMENT ON TABLE "SRH"."ESOCIAL_RUBRICACONFIG"  IS 'Tabela que representa o evento S-1010 contendo o detalhamento das informações das rubricas constantes da folha de pagamento do empregador/órgão público, permitindo a correlação destas com as constantes da Tabela-03 do eSocial. É utilizada para inclusão, alteração e exclusão de registros na Tabela de Rubricas do empregador/contribuinte/órgão público. As informações consolidadas desta tabela são utilizadas para validação dos eventos de remuneração dos trabalhadores.';

GRANT SELECT ON SRH.esocial_rubricaconfig TO CONECTOR_ESOCIAL;

ALTER TABLE SRH.esocial_rubrica_tabela ADD codigo VARCHAR2(8);

INSERT INTO "SRH"."ESOCIAL_RUBRICA_TABELA" (ID, DESCRICAO, CODIGO) VALUES ('1', 'Tabela Padrão', '001');
INSERT INTO "SRH"."ESOCIAL_RUBRICA_TABELA" (ID, DESCRICAO, CODIGO) VALUES ('2', 'Tabela Isenção IRRF', '002');
INSERT INTO "SRH"."ESOCIAL_RUBRICA_TABELA" (ID, DESCRICAO, CODIGO) VALUES ('3', 'Tabela com a opção de incidência de contribuição previdenciária sobre cargo comissionado e outras vantagens variáveis previstas em lei', '003');

CREATE TABLE "SRH"."ESOCIAL_TRIBUTARIO" (
	"ID" 					NUMBER(4,0), 
	"CODIGOLOTACAO" 		VARCHAR2(30 BYTE), 
	"TIPOLOTACAO" 			VARCHAR2(2 BYTE), 
	"TIPOINSCRICAOLOTACAO" 	NUMBER(1,0), 
	"NUMEROINSCRICAO" 		VARCHAR2(15 BYTE), 
	"FPASLOTACAO" 			NUMBER(3,0), 
	"CODIGOTERCEIROS" 		VARCHAR2(4 BYTE), 
	"CODIGOTERCSUSPENSO" 	VARCHAR2(4 BYTE), 
	"IDESOCIALVIGENCIA" 	NUMBER(9,0), 
	 CONSTRAINT "PK_ESOCIAL_TRIBUTARIO" PRIMARY KEY ("ID"), 
	 CONSTRAINT "ESOCIAL_TRIBUTARIO_FK1" FOREIGN KEY ("IDESOCIALVIGENCIA") REFERENCES "SRH"."ESOCIAL_EVENTO_VIGENCIA" ("ID") ENABLE
   );

COMMENT ON COLUMN "SRH"."ESOCIAL_TRIBUTARIO"."ID" IS 'Chave Primária da Tabela';
COMMENT ON COLUMN "SRH"."ESOCIAL_TRIBUTARIO"."CODIGOLOTACAO" IS 'Informar o código atribuído pela empresa para a lotação tributária. Validação: O código atribuído não pode conter a expressão "eSocial" nas 7 primeiras posições.';
COMMENT ON COLUMN "SRH"."ESOCIAL_TRIBUTARIO"."TIPOLOTACAO" IS 'Preencher com o código correspondente ao tipo de lotação, conforme tabela 10. Validação: Deve ser um código válido, existente na tabela 10, e compatível com a Classificação Tributária indicada no evento de Informações Cadastrais do Empregador.';
COMMENT ON COLUMN "SRH"."ESOCIAL_TRIBUTARIO"."TIPOINSCRICAOLOTACAO" IS 'Preencher com o código correspondente ao tipo de inscrição, conforme tabela 5. Validação: O campo não deve ser preenchido se {tpLotacao} for igual a [01, 10, 21, 24, 90, 91]. Nos demais casos, observar conteúdo exigido para o campo {nrInsc}, conforme Tabela 10 - Tipos de Lotação Tributária. Valores Válidos: 1, 2, 4.';
COMMENT ON COLUMN "SRH"."ESOCIAL_TRIBUTARIO"."NUMEROINSCRICAO" IS 'Preencher com o número de Inscrição (CNPJ, CPF, CNO) ao qual pertence a lotação tributária, conforme indicado na tabela 10 - Tipos de Lotação Tributária. Validação: a) Deve ser preenchido de acordo com o conteúdo exigido, conforme especificado no campo {tpInsc} e na tabela de tipos de Lotação Tributária. b) Deve ser um identificador válido, constante das bases da RFB.';
COMMENT ON COLUMN "SRH"."ESOCIAL_TRIBUTARIO"."FPASLOTACAO" IS 'Preencher com o código relativo ao FPAS. Validação: Deve ser um código FPAS válido, conforme tabela 4.';
COMMENT ON COLUMN "SRH"."ESOCIAL_TRIBUTARIO"."CODIGOTERCEIROS" IS 'Preencher com o código de Terceiros, conforme tabela 4, já considerando a existência de eventuais convênios para recolhimento direto. Exemplo: Se o contribuinte está enquadrado com FPAS 507, cujo código cheio de Terceiros é 0079, se possuir convênio com Senai deve informar o código 0075. Validação: Se a classificação tributária for igual a [01, 02, 03, 04], informar 0000. Nos demais casos, o código de terceiros informado deve ser compatível com o código de FPAS informado, conforme Tabela 04.';
COMMENT ON COLUMN "SRH"."ESOCIAL_TRIBUTARIO"."CODIGOTERCSUSPENSO" IS 'Informar o código combinado dos Terceiros para os quais o recolhimento está suspenso em virtude de processos Judiciais. Exemplo: Se o contribuinte possui decisões de processos para suspensão de recolhimentos ao Sesi (0008) e ao Sebrae (0064), deve informar o código combinado das duas entidades, ou seja, 0072. Validação: Deve ser um código consistente com a Tabela 4. Deve haver um processo em {procJudTerceiro} para cada código de Terceiro cujo recolhimento esteja suspenso.';

GRANT SELECT ON SRH.ESOCIAL_TRIBUTARIO TO CONECTOR_ESOCIAL;

INSERT INTO "SRH"."ESOCIAL_TRIBUTARIO" (ID, CODIGOLOTACAO, TIPOLOTACAO, FPASLOTACAO, CODIGOTERCEIROS) VALUES ('1', 'LOTACAO-BASICA', '01', '582', '0000');

ALTER TABLE SRH.TB_OCUPACAO DROP COLUMN INICIOVALIDADE;
ALTER TABLE SRH.TB_OCUPACAO DROP COLUMN FIMVALIDADE;
ALTER TABLE SRH.TB_OCUPACAO DROP COLUMN INICIONOVAVALIDADE;
ALTER TABLE SRH.TB_OCUPACAO DROP COLUMN FIMNOVAVALIDADE;
ALTER TABLE SRH.TB_OCUPACAO DROP COLUMN INICIOEXCLUSAO;
ALTER TABLE SRH.TB_OCUPACAO DROP COLUMN FIMEXCLUSAO;
ALTER TABLE SRH.TB_OCUPACAO ADD (NUMEROLEI VARCHAR2(12));
ALTER TABLE SRH.TB_OCUPACAO ADD (DATALEI DATE );
ALTER TABLE SRH.TB_OCUPACAO ADD (SITUACAOLEI NUMBER(1));
ALTER TABLE SRH.TB_OCUPACAO ADD (IDESOCIALVIGENCIA NUMBER(9));
ALTER TABLE SRH.TB_OCUPACAO  MODIFY (DEDICACAOEXCLUSIVA VARCHAR2(1));
ALTER TABLE SRH.TB_OCUPACAO ADD CONSTRAINT TB_OCUPACAO_FK1 FOREIGN KEY (IDESOCIALVIGENCIA) REFERENCES SRH.ESOCIAL_EVENTO_VIGENCIA (ID) ENABLE;

COMMENT ON COLUMN SRH.TB_OCUPACAO.CODIGO_ESOCIAL IS '[eSocial] Preencher com o código do cargo. Validação: O código atribuído não pode conter a expressão "eSocial" nas 7 (sete) primeiras posições.';
COMMENT ON COLUMN SRH.TB_OCUPACAO.CBO IS '[eSocial] Preencher com o código correspondente à possibilidade de acumulação de cargos: 1 - Não acumulável; 2 - Profissional de Saúde; 3 - Professor; 4 - Técnico/Científico. Valores Válidos: 1, 2, 3, 4.';
COMMENT ON COLUMN SRH.TB_OCUPACAO.TIPOACUMULACAO IS '[eSocial] Preencher com o código correspondente à possibilidade de acumulação de cargos: 1 - Não acumulável; 2 - Profissional de Saúde; 3 - Professor; 4 - Técnico/Científico. Valores Válidos: 1, 2, 3, 4.';
COMMENT ON COLUMN SRH.TB_OCUPACAO.TEMPOESPECIAL IS '[eSocial] Preencher com o código correspondente a possibilidade de contagem de tempo especial: 1 - Não; 2 - Professor (Infantil, Fundamental e Médio); 3 - Professor de Ensino Superior, Magistrado, Membro de Ministério Público, Membro do Tribunal de Contas (com ingresso anterior a 16/12/1998 EC nr. 20/98); 4 - Atividade de risco. Valores Válidos: 1, 2, 3, 4.';
COMMENT ON COLUMN SRH.TB_OCUPACAO.DEDICACAOEXCLUSIVA IS '[eSocial] Indicar se é cargo de dedicação exclusiva: S - Sim; N - Não.';
COMMENT ON COLUMN SRH.TB_OCUPACAO.SITUACAOLEI IS '[eSocial] Situação gerada pela Lei. Preencher com uma das opções: 1 - Criação; 2 - Extinção; 3 - Reestruturação. Valores Válidos: 1, 2, 3.';

CREATE TABLE "SRH"."TB_CARREIRA" (	
	"ID" 					NUMBER(4,0), 
    "CODIGO" 				VARCHAR2(30 BYTE), 
    "DESCRICAO" 			VARCHAR2(100 BYTE), 
    "NUMEROLEI" 			VARCHAR2(12 BYTE), 
    "DATALEI" 				DATE, 
    "SITUACAOLEI" 			NUMBER(1,0), 
    "IDESOCIALVIGENCIA" 	NUMBER(9,0), 
    CONSTRAINT "PK_ESOCIAL_CARREIRA" PRIMARY KEY ("ID"), 
    CONSTRAINT "TB_CARREIRA_FK1" FOREIGN KEY ("IDESOCIALVIGENCIA") REFERENCES "SRH"."ESOCIAL_EVENTO_VIGENCIA" ("ID") ENABLE
);

COMMENT ON COLUMN "SRH"."TB_CARREIRA"."ID" IS 'Chave Primária da Tabela';
COMMENT ON COLUMN "SRH"."TB_CARREIRA"."CODIGO" IS 'Preencher com o código da carreira. Validação: O código atribuído não pode conter a expressão "eSocial" nas 7 (sete) primeiras posições.';
COMMENT ON COLUMN "SRH"."TB_CARREIRA"."DESCRICAO" IS 'Nome da Carreira';
COMMENT ON COLUMN "SRH"."TB_CARREIRA"."NUMEROLEI" IS 'Número da Lei que estruturou a carreira.';
COMMENT ON COLUMN "SRH"."TB_CARREIRA"."DATALEI" IS 'Data da Lei';
COMMENT ON COLUMN "SRH"."TB_CARREIRA"."SITUACAOLEI" IS 'Situação gerada pela Lei. Preencher com uma das opções: 1 - Criação; 2 - Extinção; 3 - Reestruturação. Valores Válidos: 1, 2, 3.';

GRANT SELECT ON SRH.TB_CARREIRA TO CONECTOR_ESOCIAL;

INSERT INTO "SRH"."TB_CARREIRA" (ID, CODIGO, DESCRICAO, NUMEROLEI, DATALEI, SITUACAOLEI) VALUES ('1', '001', 'Controle Externo', '13.783/2006', TO_DATE('2019-01-14 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), '3');

CREATE TABLE "SRH"."TB_GRADE_HORARIO" (	
	"ID" 				NUMBER(4,0), 
	"CODIGO" 			VARCHAR2(30 BYTE), 
	"HORAENTRADA" 		VARCHAR2(4 BYTE), 
	"HORASAIDA" 		VARCHAR2(4 BYTE), 
	"DURACAOJORNADA" 	NUMBER(4,0), 
	"FLEXIVEL" 			CHAR(1 BYTE), 
	"TIPOINTERVALO" 	NUMBER(1,0), 
	"DURACAOINTERVALO" 	NUMBER(3,0), 
	"INICIOINTERVALO" 	VARCHAR2(4 BYTE), 
	"FINALINTERVALO" 	VARCHAR2(4 BYTE), 
	"IDESOCIALVIGENCIA" NUMBER(9,0), 
	 CONSTRAINT "PK_HORARIOTRABALHO" PRIMARY KEY ("ID"), 
	 CONSTRAINT "TB_GRADE_HORARIO_FK1" FOREIGN KEY ("IDESOCIALVIGENCIA") REFERENCES "SRH"."ESOCIAL_EVENTO_VIGENCIA" ("ID") ENABLE
);

COMMENT ON COLUMN "SRH"."TB_GRADE_HORARIO"."ID" IS 'Chave Primária da Tabela';
COMMENT ON COLUMN "SRH"."TB_GRADE_HORARIO"."CODIGO" IS 'Preencher com o código atribuído pela empresa para o Horário Contratual. Validação: O código atribuído não pode conter a expressão "eSocial" nas 7 (sete) primeiras posições.';
COMMENT ON COLUMN "SRH"."TB_GRADE_HORARIO"."HORAENTRADA" IS 'Informar hora da entrada, no formato HHMM. Validação: Deve estar no intervalo entre [0000] e [2359], criticando inclusive a segunda parte do número, que indica os minutos, que deve ser menor ou igual a 59.';
COMMENT ON COLUMN "SRH"."TB_GRADE_HORARIO"."HORASAIDA" IS 'Informar hora da saída, no formato HHMM. Validação: Deve estar no intervalo entre [0000] e [2359], criticando inclusive a segunda parte do número, que indica os minutos, que deve ser menor ou igual a 59.';
COMMENT ON COLUMN "SRH"."TB_GRADE_HORARIO"."DURACAOJORNADA" IS 'Preencher com o tempo de duração da jornada, em minutos. Devem ser consideradas as horas reduzidas noturnas, se houver.';
COMMENT ON COLUMN "SRH"."TB_GRADE_HORARIO"."FLEXIVEL" IS 'Indicar se é permitida a flexibilidade: S - Sim; N - Não. Valores Válidos: S, N.';
COMMENT ON COLUMN "SRH"."TB_GRADE_HORARIO"."TIPOINTERVALO" IS 'Tipo de Intervalo da Jornada: 1 - Intervalo em Horário Fixo; 2 - Intervalo em Horário Variável. Valores Válidos: 1, 2.';
COMMENT ON COLUMN "SRH"."TB_GRADE_HORARIO"."DURACAOINTERVALO" IS 'Preencher com o tempo de duração do intervalo, em minutos.';
COMMENT ON COLUMN "SRH"."TB_GRADE_HORARIO"."INICIOINTERVALO" IS 'Informar a hora de início do intervalo, no formato HHMM. Validação: Deve estar no intervalo entre [0000] e [2359], criticando inclusive a segunda parte do número, que indica os minutos, que deve ser menor ou igual a 59. Somente deve ser informado se {tipoIntervalo}=1.';
COMMENT ON COLUMN "SRH"."TB_GRADE_HORARIO"."FINALINTERVALO" IS 'Informar a hora de término do intervalo, no formato HHMM. Validação: Deve estar no intervalo entre [0000] e [2359], criticando inclusive a segunda parte do número, que indica os minutos, que deve ser menor ou igual a 59. Somente deve ser informado se {tipoIntervalo}=1.';
COMMENT ON TABLE "SRH"."TB_GRADE_HORARIO"  IS 'Tabela que representa o evento S-1050 contendo as informações dos horários de trabalhos da organização.';


GRANT SELECT ON SRH.TB_GRADE_HORARIO TO CONECTOR_ESOCIAL;


CREATE TABLE "SRH"."TB_AMBIENTE_TRABALHO" (	
	"ID" 				NUMBER(4,0), 
	"CODIGO" 			VARCHAR2(30 BYTE), 
	"NOMEAMBIENTE" 		VARCHAR2(100 BYTE), 
	"DESCRICAOAMBIENTE" VARCHAR2(4000 BYTE), 
	"LOCALAMBIENTE" 	NUMBER(1,0), 
	"CODIGOLOTACAO" 	VARCHAR2(30 BYTE), 
	"IDESTABELECIMENTO" NUMBER(4,0), 
	"IDESOCIALVIGENCIA" NUMBER(9,0), 
	 CONSTRAINT "PK_AMBIENTETRABALHO" PRIMARY KEY ("ID") ENABLE, 
	 CONSTRAINT "ESOCIAL_AMBIENTETRABALHO_FK1" FOREIGN KEY ("IDESTABELECIMENTO") REFERENCES "SRH"."ESOCIAL_ESTABELECIMENTO" ("ID") ENABLE, 
	 CONSTRAINT "TB_AMBIENTE_TRABALHO_FK1" FOREIGN KEY ("IDESOCIALVIGENCIA") REFERENCES "SRH"."ESOCIAL_EVENTO_VIGENCIA" ("ID") ENABLE
);

COMMENT ON COLUMN "SRH"."TB_AMBIENTE_TRABALHO"."ID" IS 'Chave Primária da Tabela';
COMMENT ON COLUMN "SRH"."TB_AMBIENTE_TRABALHO"."CODIGO" IS 'Preencher com o código do ambiente de trabalho. Validação: O código atribuído não pode conter a expressão "eSocial" nas 7 (sete) primeiras posições.';
COMMENT ON COLUMN "SRH"."TB_AMBIENTE_TRABALHO"."NOMEAMBIENTE" IS 'Informar o nome do ambiente de trabalho.';
COMMENT ON COLUMN "SRH"."TB_AMBIENTE_TRABALHO"."DESCRICAOAMBIENTE" IS 'Descrição mais detalhada do ambiente de trabalho.';
COMMENT ON COLUMN "SRH"."TB_AMBIENTE_TRABALHO"."LOCALAMBIENTE" IS 'Preencher com uma das opções: 1 - Estabelecimento do próprio empregador; 2 - Estabelecimento de terceiros; 3 - Prestação de serviços em instalações de terceiros não consideradas como lotações dos tipos 03 a 09 da Tabela 10. Valores Válidos: 1, 2, 3.';
COMMENT ON COLUMN "SRH"."TB_AMBIENTE_TRABALHO"."CODIGOLOTACAO" IS 'Informar o código atribuído pela empresa para a lotação tributária. Validação: Preenchimento obrigatório e exclusivo se {localAmb} = [2]. Se informado, deve ser um código existente em S-1020 - Tabela de Lotações Tributárias.';
COMMENT ON TABLE "SRH"."TB_AMBIENTE_TRABALHO"  IS 'Tabela que representa o evento S-1060. As informações consolidadas desta tabela são utilizadas para validação do evento de condições ambientais do trabalho. Devem ser informados os ambientes de trabalho da empresa em que há trabalhadores exercendo atividades. O evento Tabela de Ambientes de Trabalho deve ser enviado antes dos eventos S2240 (Condições Ambientais do Trabalho – Fatores de Risco - Início) e S-2210 (Comunicação de Acidente de Trabalho)';

GRANT SELECT ON SRH.TB_AMBIENTE_TRABALHO TO CONECTOR_ESOCIAL;

INSERT INTO "SRH"."TB_AMBIENTE_TRABALHO" (ID, CODIGO, NOMEAMBIENTE, DESCRICAOAMBIENTE, LOCALAMBIENTE, IDESTABELECIMENTO) VALUES ('1', '001', 'SEDE TCE', 'Abrange as dependências do Tribunal de Contas do Estado do Ceará contidas nos edifícios sede e Anexo-II', '1', '1');
INSERT INTO "SRH"."TB_AMBIENTE_TRABALHO" (ID, CODIGO, NOMEAMBIENTE, DESCRICAOAMBIENTE, LOCALAMBIENTE, IDESTABELECIMENTO) VALUES ('2', '002', 'Instituto Plácido Castelo', 'Abrange as dependências da Escola de Contas Ministro Plácido Castelo', '1', '1');

CREATE TABLE "SRH"."ESOCIAL_PROCESSO" (	
	"ID" 				NUMBER(6,0), 
	"TIPOPROCESSO" 		NUMBER(1,0), 
	"NUMEROPROCESSO" 	VARCHAR2(21 BYTE), 
	"AUTORIA" 			NUMBER(1,0), 
	"MATERIA" 			NUMBER(2,0), 
	"OBSERVACAO" 		VARCHAR2(255 BYTE), 
	"IDVARA" 			NUMBER(4,0), 
	"MUNICIPIOVARA" 	NUMBER(8,0), 
	"IDESOCIALVIGENCIA" NUMBER(9,0), 
	 CONSTRAINT "PK_ESOCIAL_PROCESSO" PRIMARY KEY ("ID") ENABLE, 
	 CONSTRAINT "ESOCIAL_PROCESSO_FK1" FOREIGN KEY ("MUNICIPIOVARA") REFERENCES "SRH"."TB_MUNICIPIO" ("ID") ENABLE, 
	 CONSTRAINT "ESOCIAL_PROCESSO_FK2" FOREIGN KEY ("IDESOCIALVIGENCIA") REFERENCES "SRH"."ESOCIAL_EVENTO_VIGENCIA" ("ID") ENABLE
   );

COMMENT ON COLUMN "SRH"."ESOCIAL_PROCESSO"."ID" IS 'Chave Primária da Tabela';
COMMENT ON COLUMN "SRH"."ESOCIAL_PROCESSO"."TIPOPROCESSO" IS 'Preencher com o código correspondente ao tipo de processo: 1 - Administrativo; 2 - Judicial; 4 - Processo FAP. Valores Válidos: 1, 2, 4.';
COMMENT ON COLUMN "SRH"."ESOCIAL_PROCESSO"."NUMEROPROCESSO" IS 'Informar o número do processo administrativo/judicial ou do benefício de acordo com o tipo informado em {tpProc}. Validação: Deve ser um número de processo/benefício válido e: a) Se {tpProc} = [1], deve possuir 17 (dezessete) ou 21 (vinte e um) algarismos; b) Se {tpProc} = [2], deve possuir 20 (vinte) algarismos; c) Se {tpProc} = [3], deve possuir 10 (dez) algarismos; d) Se {tpProc} = [4], deve possuir 16 (dezesseis) algarismos.';
COMMENT ON COLUMN "SRH"."ESOCIAL_PROCESSO"."AUTORIA" IS 'Indicativo da autoria da ação judicial: 1 - Próprio contribuinte; 2 - Outra entidade, empresa ou empregado. Validação: Preenchimento obrigatório se {tpProc} = [2]. Valores Válidos: 1, 2.';
COMMENT ON COLUMN "SRH"."ESOCIAL_PROCESSO"."MATERIA" IS 'Indicativo da matéria do processo ou alvará judicial: 1 - Exclusivamente tributária ou tributária e FGTS; 7 - Exclusivamente FGTS e/ou Contribuição Social Rescisória (Lei Complementar 110/2001).';
COMMENT ON COLUMN "SRH"."ESOCIAL_PROCESSO"."OBSERVACAO" IS 'Observações relacionadas ao processo';
COMMENT ON COLUMN "SRH"."ESOCIAL_PROCESSO"."IDVARA" IS 'Código de Identificação da Vara.';
COMMENT ON COLUMN "SRH"."ESOCIAL_PROCESSO"."MUNICIPIOVARA" IS 'Preencher com o código do município, conforme tabela do IBGE. Validação: Se informado, deve ser um código existente na tabela do IBGE.';

GRANT SELECT ON SRH.ESOCIAL_PROCESSO TO CONECTOR_ESOCIAL;

CREATE TABLE "SRH"."ESOCIAL_PROCESSO_SUSPENSAO" (	
	"ID" 					NUMBER(14,0) NOT NULL ENABLE, 
	"INDICATIVOSUSPENSAO" 	VARCHAR2(2 BYTE), 
	"DATADECISAO" 			DATE, 
	"INDICATIVODEPOSITO" 	VARCHAR2(1 BYTE), 
	"IDPROCESSO" 			NUMBER(6,0), 
	 CONSTRAINT "ESOCIAL_PROCESSO_SUSPENSAO_PK" PRIMARY KEY ("ID") ENABLE, 
	 CONSTRAINT "FK_ESOCIAL_PROCESSO" FOREIGN KEY ("IDPROCESSO") REFERENCES "SRH"."ESOCIAL_PROCESSO" ("ID") ENABLE
   );

COMMENT ON COLUMN "SRH"."ESOCIAL_PROCESSO_SUSPENSAO"."ID" IS 'Código do Indicativo da Suspensão, atribuído pelo empregador.';
COMMENT ON COLUMN "SRH"."ESOCIAL_PROCESSO_SUSPENSAO"."INDICATIVOSUSPENSAO" IS 'Indicativo de suspensão da exigibilidade: 01 - Liminar em Mandado de Segurança; 02 - Depósito Judicial do Montante Integral; 03 - Depósito Administrativo do Montante Integral; 04 - Antecipação de Tutela; 05 - Liminar em Medida Cautelar; 08 - Sentença em Mandado de Segurança Favorável ao Contribuinte; 09 - Sentença em Ação Ordinária Favorável ao Contribuinte e Confirmada pelo TRF; 10 - Acórdão do TRF Favorável ao Contribuinte; 11 - Acórdão do STJ em Recurso Especial Favorável ao Contribuinte; 12 - Acórdão do STF em Recurso Extraordinário Favorável ao Contribuinte; 13 - Sentença 1ª instância não transitada em julgado com efeito suspensivo; 14 - Contestação Administrativa FAP; 90 - Decisão Definitiva a favor do contribuinte; 92 - Sem suspensão da exigibilidade. Validação: Se {tpProc} = [1], deve ser preenchido com [03, 14, 92]. Se {tpProc} = [2], deve ser preenchido com [01, 02, 04, 05, 08, 09, 10, 11, 12, 13, 90, 92]. Se {tpProc} = [4], deve ser preenchido com [14]. Valores Válidos: 01, 02, 03, 04, 05, 08, 09, 10, 11, 12, 13, 14, 90, 92.';
COMMENT ON COLUMN "SRH"."ESOCIAL_PROCESSO_SUSPENSAO"."DATADECISAO" IS 'Data da decisão, sentença ou despacho administrativo.';
COMMENT ON COLUMN "SRH"."ESOCIAL_PROCESSO_SUSPENSAO"."INDICATIVODEPOSITO" IS 'Indicativo de Depósito do Montante Integral: S - Sim; N - Não. Validação: Se {indSusp} = [90], preencher obrigatoriamente com [N]. Se {indSusp} = [02, 03] preencher obrigatoriamente com [S]. Valores Válidos: S, N.';
COMMENT ON COLUMN "SRH"."ESOCIAL_PROCESSO_SUSPENSAO"."IDPROCESSO" IS 'Processo enviado ao eSocial.';
   
GRANT SELECT ON SRH.ESOCIAL_PROCESSO_SUSPENSAO TO CONECTOR_ESOCIAL;

ALTER TABLE SRH.tb_representacaocargo DROP COLUMN iniciovalidade;
ALTER TABLE SRH.tb_representacaocargo DROP COLUMN fimvalidade;
ALTER TABLE SRH.tb_representacaocargo DROP COLUMN inicionovavalidade;
ALTER TABLE SRH.tb_representacaocargo DROP COLUMN fimnovavalidade;
ALTER TABLE SRH.tb_representacaocargo DROP COLUMN inicioexclusao;
ALTER TABLE SRH.tb_representacaocargo DROP COLUMN fimexclusao;
ALTER TABLE SRH.tb_representacaocargo ADD (idesocialvigencia NUMBER(9));
ALTER TABLE SRH.tb_representacaocargo RENAME COLUMN codigo_esocial TO codfuncao;
ALTER TABLE SRH.tb_representacaocargo ADD CONSTRAINT tb_representacaocargo_fk1 FOREIGN KEY ( idesocialvigencia ) REFERENCES SRH.esocial_evento_vigencia ( id );

DROP TABLE SRH.esocial_cargos_lei;

DROP TABLE SRH.esocial_config;

CREATE TABLE "SRH"."ESOCIAL_EVENTO" (
    "ID"          NUMBER(9, 0) NOT NULL ENABLE,
    "CODIGO"      VARCHAR2(6 BYTE),
    "DESCRICAO"   VARCHAR2(255 BYTE),
    CONSTRAINT "ESOCIAL_EVENTO_PK" PRIMARY KEY ( "ID" )
);

Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('1000','S-1000','Informações do Empregador/Contribuinte/Órgão Público');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('1005','S-1005','Tabela de Estabelecimentos, Obras ou Unidades de Órgãos Públicos');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('1010','S-1010','Tabela de Rubricas');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('1020','S-1020','Tabela de Lotações Tributárias');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('1030','S-1030','Tabela de Cargos/Empregos Públicos');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('1035','S-1035','Tabela de Carreiras Públicas');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('1040','S-1040','Tabela de Funções/Cargos em Comissão');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('1050','S-1050','Tabela de Horários/Turnos de Trabalho');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('1060','S-1060','Tabela de Ambientes de Trabalho');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('1070','S-1070','Tabela de Processos Administrativos/Judiciais');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('1080','S-1080','Tabela de Operadores Portuários');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('1200','S-1200','Remuneração de trabalhador vinculado ao Regime Geral de Previd. Social');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('1202','S-1202','Remuneração de servidor vinculado a Regime Próprio de Previd. Social');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('1207','S-1207','Benefícios previdenciários - RPPS');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('1210','S-1210','Pagamentos de Rendimentos do Trabalho');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('1250','S-1250','Aquisição de Produção Rural');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('1260','S-1260','Comercialização da Produção Rural Pessoa Física');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('1270','S-1270','Contratação de Trabalhadores Avulsos Não Portuários');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('1280','S-1280','Informações Complementares aos Eventos Periódicos');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('1295','S-1295','Solicitação de Totalização para Pagamento em Contingência');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('1298','S-1298','Reabertura dos Eventos Periódicos');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('1299','S-1299','Fechamento dos Eventos Periódicos');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('1300','S-1300','Contribuição Sindical Patronal');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('2200','S-2200','Cadastramento Inicial do Vínculo e Admissão/Ingresso de Trabalhador');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('2205','S-2205','Alteração de Dados Cadastrais do Trabalhador');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('2206','S-2206','Alteração de Contrato de Trabalho');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('2210','S-2210','Comunicação de Acidente de Trabalho');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('2220','S-2220','Monitoramento da Saúde do Trabalhador');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('2230','S-2230','Afastamento Temporário');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('2240','S-2240','Condições Ambientais do Trabalho - Fatores de Risco');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('2250','S-2250','Aviso Prévio');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('2260','S-2260','Convocação para Trabalho Intermitente');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('2298','S-2298','Reintegração');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('2299','S-2299','Desligamento');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('2300','S-2300','Trabalhador Sem Vínculo de Emprego/Estatutário - Início');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('2306','S-2306','Trabalhador Sem Vínculo de Emprego/Estatutário - Alteração Contratual');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('2399','S-2399','Trabalhador Sem Vínculo de Emprego/Estatutário - Término');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('2400','S-2400','Cadastro de Benefícios Previdenciários - RPPS');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('3000','S-3000','Exclusão de eventos');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('5001','S-5001','Informações das contribuições sociais por trabalhador');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('5002','S-5002','Imposto de Renda Retido na Fonte');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('5011','S-5011','Informações das contribuições sociais consolidadas por contribuinte');
Insert into SRH.ESOCIAL_EVENTO (ID,CODIGO,DESCRICAO) values ('5012','S-5012','Informações do IRRF consolidadas por contribuinte');

COMMIT;

CREATE TABLE "SRH"."ESOCIAL_NOTIFICACAO" (
    "ID"           NUMBER(9, 0) NOT NULL ENABLE,
    "DESCRICAO"    VARCHAR2(255 BYTE) NOT NULL ENABLE,
    "DATA"         DATE NOT NULL ENABLE,
    "TIPO"         CHAR(1 BYTE) NOT NULL ENABLE,
    "IDEVENTO"     NUMBER(9, 0) NOT NULL ENABLE,
    "REFERENCIA"   VARCHAR2(30 BYTE) NOT NULL ENABLE,
    CONSTRAINT "ESOCIAL_NOTIFICACAO_PK" PRIMARY KEY ( "ID" ),
    CONSTRAINT "ESOCIAL_NOTIFICACAO_FK1" FOREIGN KEY ( "IDEVENTO" ) REFERENCES "SRH"."ESOCIAL_EVENTO" ( "ID" )
);

GRANT ALL PRIVILEGES ON SRH.esocial_notificacao TO notificacao_esocial;

CREATE SEQUENCE "SRH"."SEQ_ESOCIAL_NOTIFICACAO" NOCACHE;


ALTER TABLE SRH.tb_dependente ADD (flinvalido NUMBER(1));
ALTER TABLE SRH.tb_dependente ADD (codigoesocial VARCHAR2(2));
COMMENT ON COLUMN SRH.tb_dependente.flinvalido IS 'Informar se o dependente tem incapacidade física ou mental para o trabalho: 0 - Não; 1 - Sim';
COMMENT ON COLUMN SRH.tb_dependente.codigoesocial IS 'Tipo de dependente conforme tabela 07 do eSocial';

ALTER TABLE SRH.tb_funcional ADD (datanomeacao DATE);
ALTER TABLE SRH.tb_funcional ADD (decisaojudicial NUMBER(1));
ALTER TABLE SRH.tb_funcional ADD (idfuncionaloriginal NUMBER(4));
COMMENT ON COLUMN SRH.tb_funcional.datanomeacao IS 'Preencher com a data da nomeação do servidor. Validação: Deve ser posterior à data de nascimento do trabalhador e igual ou anterior à data da posse.';

CREATE TABLE "SRH"."TB_FUNCIONALDEFICIENCIA" (
    "ID"              NUMBER(9, 0) NOT NULL ENABLE,
    "IDFUNCIONAL"     NUMBER(4, 0) NOT NULL ENABLE,
    "FLFISICA"        NUMBER(1, 0) NOT NULL ENABLE,
    "FLVISUAL"        NUMBER(1, 0) NOT NULL ENABLE,
    "FLAUDITIVA"      NUMBER(1, 0) NOT NULL ENABLE,
    "FLMENTAL"        NUMBER(1, 0) NOT NULL ENABLE,
    "FLINTELECTUAL"   NUMBER(1, 0) NOT NULL ENABLE,
    "FLREADAPTADO"    NUMBER(1, 0) NOT NULL ENABLE,
    "FLCOTA"          NUMBER(1, 0) NOT NULL ENABLE,
    "OBSERVACAO"      VARCHAR2(255 BYTE),
    CONSTRAINT "TB_FUNCIONALDEFICIENCIA_PK" PRIMARY KEY ( "ID" ),
    CONSTRAINT "TB_FUNCIONALDEFICIENCIA_FK1" FOREIGN KEY ( "IDFUNCIONAL" ) REFERENCES "SRH"."TB_FUNCIONAL" ( "ID" )
);

<!-- ADICIONADO FLG NA TABLE PESSOA JURIDICA PARA INDICAR SE A EMPRESA CADASTRADA É DA AREA DA SAUDE, QUE SERÁ USANDO NA TELA DE AUXILIO SAUDE -->
alter table SHR.tb_pessoajuridica add (
    FG_EMPRESAAREASAUDE char(1),
    constraint CK_FG_EMPRESAAREASAUDE CHECK(FG_EMPRESAAREASAUDE IN(0,1))
)
