CREATE TABLE USRDBVAH.TB_LANCA_CLIENTE_SETOR
(
  CD_FORNECEDOR INTEGER NOT NULL,
  CD_SETOR      INTEGER NOT NULL
);
CREATE INDEX USRDBVAH.FK_CLI_SET_INDEX_B ON USRDBVAH.TB_LANCA_CLIENTE_SETOR
(
  CD_SETOR ASC
);
CREATE UNIQUE INDEX USRDBVAH.PRIMARY_KEY_B ON USRDBVAH.TB_LANCA_CLIENTE_SETOR
(
  CD_FORNECEDOR ASC, CD_SETOR ASC
);
ALTER TABLE USRDBVAH.TB_LANCA_CLIENTE_SETOR ADD CONSTRAINT CONSTRAINT_B PRIMARY KEY (CD_FORNECEDOR, CD_SETOR) USING INDEX USRDBVAH.PRIMARY_KEY_B;
CREATE TABLE USRDBVAH.TB_LANCA_COMENTARIOS
(
  ID            INTEGER       NOT NULL,
  DT_CRIACAO    DATE          NOT NULL,
  NM_DETALHES   VARCHAR2(255) NOT NULL,
  ID_PUSER      INTEGER       NOT NULL,
  ID_LANCAMENTO INTEGER       NOT NULL
);
CREATE INDEX USRDBVAH.FK_PUSER_INDEX_B ON USRDBVAH.TB_LANCA_COMENTARIOS
(
  ID_PUSER ASC
);
CREATE UNIQUE INDEX USRDBVAH.PRIMARY_KEY_BD ON USRDBVAH.TB_LANCA_COMENTARIOS
(
  ID ASC
);
CREATE INDEX USRDBVAH.FK_LANC_INDEX_B ON USRDBVAH.TB_LANCA_COMENTARIOS
(
  ID_LANCAMENTO ASC
);
ALTER TABLE USRDBVAH.TB_LANCA_COMENTARIOS ADD CONSTRAINT TB_LANCA_COMENTARIOS_PK PRIMARY KEY (ID) USING INDEX USRDBVAH.PRIMARY_KEY_BD;
CREATE TABLE USRDBVAH.TB_LANCA_CONTRATO
(
  ID                        INTEGER NOT NULL,
  DT_INICIO                 DATE,
  DT_REAJUSTE               DATE,
  DT_FINAL                  DATE,
  NM_TITULO                 VARCHAR2(255),
  ID_FORNECEDOR_CONTRATANTE INTEGER NOT NULL
);
CREATE INDEX USRDBVAH.FK_CON_FOR_INDEX_F ON USRDBVAH.TB_LANCA_CONTRATO
(
  ID_FORNECEDOR_CONTRATANTE ASC
);
CREATE UNIQUE INDEX USRDBVAH.PRIMARY_KEY_F ON USRDBVAH.TB_LANCA_CONTRATO
(
  ID ASC
);
ALTER TABLE USRDBVAH.TB_LANCA_CONTRATO ADD CONSTRAINT TB_LANCA_CONTRATO_PK PRIMARY KEY (ID) USING INDEX USRDBVAH.PRIMARY_KEY_F;
CREATE TABLE USRDBVAH.TB_LANCA_CONTRATO_SETOR
(
  ID          INTEGER NOT NULL,
  ID_CONTRATO INTEGER NOT NULL,
  ID_SETOR    INTEGER,
  ID_CLIENTE  INTEGER
);
CREATE INDEX USRDBVAH.FK_CON_CON_SET_INDEX_2 ON USRDBVAH.TB_LANCA_CONTRATO_SETOR
(
  ID_CONTRATO ASC
);
CREATE INDEX USRDBVAH.FK_CON_SET_SET_INDEX_2 ON USRDBVAH.TB_LANCA_CONTRATO_SETOR
(
  ID_SETOR ASC
);
CREATE UNIQUE INDEX USRDBVAH.PRIMARY_KEY_2 ON USRDBVAH.TB_LANCA_CONTRATO_SETOR
(
  ID ASC
);
CREATE INDEX USRDBVAH.FK_CON_SET_FOR_INDEX_2 ON USRDBVAH.TB_LANCA_CONTRATO_SETOR
(
  ID_CLIENTE ASC
);
ALTER TABLE USRDBVAH.TB_LANCA_CONTRATO_SETOR ADD CONSTRAINT TB_LANCA_CONTRATO_SETOR_PK PRIMARY KEY (ID) USING INDEX USRDBVAH.PRIMARY_KEY_2;
CREATE TABLE USRDBVAH.TB_LANCA_LANCAMENTO
(
  ID                 INTEGER       NOT NULL,
  DT_LANCAMENTO      DATE,
  DT_VIGENCIA        DATE,
  CD_STATUS          VARCHAR2(255) NOT NULL,
  VL_TOTAL           NUMBER(10, 2),
  ID_SERVICO         INTEGER       NOT NULL,
  ID_SERVICO_VALORES INTEGER       NOT NULL,
  ID_USU_LANCADOR    INTEGER       NOT NULL
);
CREATE INDEX USRDBVAH.FK_SER_INDEX_7 ON USRDBVAH.TB_LANCA_LANCAMENTO
(
  ID_SERVICO ASC
);
CREATE INDEX USRDBVAH.FK_SER_VAL_INDEX_7 ON USRDBVAH.TB_LANCA_LANCAMENTO
(
  ID_SERVICO_VALORES ASC
);
CREATE INDEX USRDBVAH.FK_US_LAN_INDEX_7 ON USRDBVAH.TB_LANCA_LANCAMENTO
(
  ID_USU_LANCADOR ASC
);
CREATE UNIQUE INDEX USRDBVAH.PRIMARY_KEY_7 ON USRDBVAH.TB_LANCA_LANCAMENTO
(
  ID ASC
);
ALTER TABLE USRDBVAH.TB_LANCA_LANCAMENTO ADD CONSTRAINT TB_LANCA_LANCAMENTO_PK PRIMARY KEY (ID) USING INDEX USRDBVAH.PRIMARY_KEY_7;
CREATE TABLE USRDBVAH.TB_LANCA_LANCAMENTO_VALORES
(
  ID                  INTEGER       NOT NULL,
  DT_LANCAMENTO       DATE,
  DT_ATUALIZACAO      DATE,
  VL_LANCAMENTO       NUMBER(10, 2) NOT NULL,
  VL_VALOR_A          NUMBER(10, 2),
  VL_VALOR_B          NUMBER(10, 2),
  ID_CONTRATO_SERVICO INTEGER       NOT NULL,
  ID_LANCAMENTO       INTEGER       NOT NULL
);
CREATE INDEX USRDBVAH.FK_CON_SER_INDEX_B ON USRDBVAH.TB_LANCA_LANCAMENTO_VALORES
(
  ID_CONTRATO_SERVICO ASC
);
CREATE INDEX USRDBVAH.FK_LAN_INDEX_B ON USRDBVAH.TB_LANCA_LANCAMENTO_VALORES
(
  ID_LANCAMENTO ASC
);
CREATE UNIQUE INDEX USRDBVAH.PRIMARY_KEY_B3 ON USRDBVAH.TB_LANCA_LANCAMENTO_VALORES
(
  ID ASC
);
ALTER TABLE USRDBVAH.TB_LANCA_LANCAMENTO_VALORES ADD CONSTRAINT TB_LANCA_LANCAMENTO_VALORES_PK PRIMARY KEY (ID) USING INDEX USRDBVAH.PRIMARY_KEY_B3;
CREATE TABLE USRDBVAH.TB_LANCA_SERVICO
(
  ID                  INTEGER NOT NULL,
  SN_FATURAVEL        CHAR(1),
  SN_AGRUPAVEL        CHAR(1),
  SN_COMPULSORIO      CHAR(1),
  SN_CONTA_SETOR      CHAR(1),
  NM_TITULO           VARCHAR2(255),
  CD_TIPO             VARCHAR2(255),
  CD_CONTA_CUSTO      INTEGER NOT NULL,
  CD_HISTORICO_PADRAO INTEGER NOT NULL,
  CD_TP_DOCUMENTO     INTEGER NOT NULL,
  CD_CONTA_CONTABIL   INTEGER,
  CD_CONTA_RESULTADO  INTEGER NOT NULL
);
CREATE INDEX USRDBVAH.FK_CON_CON_INDEX_E ON USRDBVAH.TB_LANCA_SERVICO
(
  CD_CONTA_CONTABIL ASC
);
CREATE INDEX USRDBVAH.FK_HIS_PAD_INDEX_E ON USRDBVAH.TB_LANCA_SERVICO
(
  CD_HISTORICO_PADRAO ASC
);
CREATE INDEX USRDBVAH.FK_CON_RES_INDEX_E ON USRDBVAH.TB_LANCA_SERVICO
(
  CD_CONTA_RESULTADO ASC
);
CREATE UNIQUE INDEX USRDBVAH.PRIMARY_KEY_E ON USRDBVAH.TB_LANCA_SERVICO
(
  ID ASC
);
CREATE INDEX USRDBVAH.FK_TP_DOC_INDEX_E ON USRDBVAH.TB_LANCA_SERVICO
(
  CD_TP_DOCUMENTO ASC
);
CREATE INDEX USRDBVAH.FK_CON_CUS_INDEX_E ON USRDBVAH.TB_LANCA_SERVICO
(
  CD_CONTA_CUSTO ASC
);
ALTER TABLE USRDBVAH.TB_LANCA_SERVICO ADD CONSTRAINT TB_LANCA_SERVICO_PK PRIMARY KEY (ID) USING INDEX USRDBVAH.PRIMARY_KEY_E;
--  ERROR: Table name length exceeds maximum allowed length(30)
CREATE TABLE USRDBVAH.TB_LANCA_SER_CON_SER
(
  ID         INTEGER NOT NULL,
  ID_SERVICO INTEGER NOT NULL
);
CREATE INDEX USRDBVAH.FK_SER_CON_SER_INDEX_7 ON USRDBVAH.TB_LANCA_SER_CON_SER
(
  ID_SERVICO ASC
);
CREATE UNIQUE INDEX USRDBVAH.PRIMARY_KEY_73 ON USRDBVAH.TB_LANCA_SER_CON_SER
(
  ID ASC, ID_SERVICO ASC
);
ALTER TABLE USRDBVAH.TB_LANCA_SER_CON_SER ADD CONSTRAINT CONSTRAINT_73 PRIMARY KEY (ID, ID_SERVICO) USING INDEX USRDBVAH.PRIMARY_KEY_73;
CREATE TABLE USRDBVAH.TB_LANCA_SERVICO_VALORES
(
  ID         INTEGER NOT NULL,
  DT_INICIO  DATE,
  DT_FINAL   DATE,
  VL_VALOR   NUMBER(10, 2),
  VL_VALOR_A NUMBER(10, 2),
  VL_VALOR_B NUMBER(10, 2),
  VL_VALOR_C NUMBER(10, 2),
  ID_SERVICO INTEGER NOT NULL
);
CREATE INDEX USRDBVAH.FK_SER_INDEX_8 ON USRDBVAH.TB_LANCA_SERVICO_VALORES
(
  ID_SERVICO ASC
);
CREATE UNIQUE INDEX USRDBVAH.PRIMARY_KEY_8 ON USRDBVAH.TB_LANCA_SERVICO_VALORES
(
  ID ASC
);
ALTER TABLE USRDBVAH.TB_LANCA_SERVICO_VALORES ADD CONSTRAINT TB_LANCA_SERVICO_VALORES_PK PRIMARY KEY (ID) USING INDEX USRDBVAH.PRIMARY_KEY_8;
CREATE TABLE USRDBVAH.TB_LANCA_USUARIO
(
  ID        INTEGER       NOT NULL,
  DS_LOGIN  VARCHAR2(255) NOT NULL,
  NM_TITULO VARCHAR2(255)
);
CREATE UNIQUE INDEX USRDBVAH.UK_LOGIN_INDEX_6 ON USRDBVAH.TB_LANCA_USUARIO
(
  DS_LOGIN ASC
);
CREATE UNIQUE INDEX USRDBVAH.PRIMARY_KEY_6 ON USRDBVAH.TB_LANCA_USUARIO
(
  ID ASC
);
ALTER TABLE USRDBVAH.TB_LANCA_USUARIO ADD CONSTRAINT TB_LANCA_USUARIO_PK PRIMARY KEY (ID) USING INDEX USRDBVAH.PRIMARY_KEY_6;
CREATE TABLE USRDBVAH.TB_LANCA_USUARIO_SERVICO
(
  ID_PUSER   INTEGER NOT NULL,
  ID_SERVICO INTEGER NOT NULL
);
CREATE UNIQUE INDEX USRDBVAH.PRIMARY_KEY_1 ON USRDBVAH.TB_LANCA_USUARIO_SERVICO
(
  ID_PUSER ASC, ID_SERVICO ASC
);
CREATE INDEX USRDBVAH.FK_SER_INDEX_1 ON USRDBVAH.TB_LANCA_USUARIO_SERVICO
(
  ID_SERVICO ASC
);
CREATE TABLE USRDBVAH.TB_LANCA_USUARIO_ROLE
(
  ID_PUSER INTEGER       NOT NULL,
  CD_ROLE  VARCHAR2(255) NOT NULL
);
CREATE INDEX USRDBVAH.PK_USU_ROL ON USRDBVAH.TB_LANCA_USUARIO_ROLE
(
  ID_PUSER ASC, CD_ROLE ASC
);

CREATE TABLE "USRDBVAH"."TB_LANCA_SETOR_DETALHE"
(
  ID          INTEGER        NOT NULL,
  CD_SETOR    DECIMAL(38, 0) NOT NULL,
  CD_REDUZIDO DECIMAL(38, 0) NOT NULL,
  VL_AREA_M2  NUMBER(10, 2),
  CD_INSC_IMOBI VARCHAR2(50)
);
CREATE UNIQUE INDEX USRDBVAH.PK_SETOR_DETALHE_IDX ON USRDBVAH.TB_LANCA_SETOR_DETALHE
(
  ID ASC
);
ALTER TABLE "USRDBVAH"."TB_LANCA_SETOR_DETALHE" ADD CONSTRAINT PK_SETOR_DETALHE PRIMARY KEY (ID) USING INDEX USRDBVAH.PK_SETOR_DETALHE_IDX;
ALTER TABLE "USRDBVAH"."TB_LANCA_SETOR_DETALHE" ADD CONSTRAINT FK_SETOR_DETALHE_CONTA FOREIGN KEY (CD_REDUZIDO) REFERENCES "DBAMV"."PLANO_CONTAS" (CD_REDUZIDO);
ALTER TABLE "USRDBVAH"."TB_LANCA_SETOR_DETALHE" ADD CONSTRAINT FK_SETOR_DETALHE_SETOR FOREIGN KEY (CD_SETOR) REFERENCES "DBAMV"."TB_SETOR" (CD_SETOR);

// TABELA DE MEDIDORES

CREATE TABLE "USRDBVAH"."TB_LANCA_MEDIDOR"
(
  ID          INTEGER      NOT NULL,
  NM_CODIGO   VARCHAR2(50) NOT NULL,
  NM_TITULO   VARCHAR2(50) NOT NULL,
  CD_TIPO     VARCHAR2(3)  NOT NULL,
  NM_DETALHES VARCHAR2(255)
);
CREATE UNIQUE INDEX USRDBVAH.PK_LANCA_MEDIDOR_IDX ON USRDBVAH.TB_LANCA_MEDIDOR
(
  ID ASC
);
ALTER TABLE "USRDBVAH"."TB_LANCA_MEDIDOR" ADD CONSTRAINT PK_LANCA_MEDIDOR PRIMARY KEY (ID) USING INDEX USRDBVAH.PK_LANCA_MEDIDOR_IDX;

// TABELA DE RELAÇÃO ENTRE SETORES E MEDIDORES

CREATE TABLE "USRDBVAH"."TB_LANCA_SETOR_MEDIDOR"
(
  ID            INTEGER        NOT NULL,
  CD_SETOR      DECIMAL(38, 0) NOT NULL,
  ID_MEDIDOR    INTEGER        NOT NULL,
  VL_PERCENTUAL NUMBER(10, 2)
);
CREATE UNIQUE INDEX USRDBVAH.PK_SETOR_MEDIDOR_IDX ON USRDBVAH.TB_LANCA_SETOR_MEDIDOR
(
  ID ASC
);
ALTER TABLE "USRDBVAH"."TB_LANCA_SETOR_MEDIDOR" ADD CONSTRAINT PK_SETOR_MEDIDOR PRIMARY KEY (ID) USING INDEX USRDBVAH.PK_SETOR_MEDIDOR_IDX;
ALTER TABLE "USRDBVAH"."TB_LANCA_SETOR_MEDIDOR" ADD CONSTRAINT FK_SET_MED_SETOR FOREIGN KEY (CD_SETOR) REFERENCES "DBAMV"."TB_SETOR" (CD_SETOR);
ALTER TABLE "USRDBVAH"."TB_LANCA_SETOR_MEDIDOR" ADD CONSTRAINT FK_SET_MED_MEDIDOR FOREIGN KEY (ID_MEDIDOR) REFERENCES "USRDBVAH"."TB_LANCA_MEDIDOR" (ID);

// TABELA DE RELAÇÃO ENTRE LANÇAMENTOS E MEDIDORES

CREATE TABLE USRDBVAH.TB_LANCA_LEITURAS
(
  ID                  INTEGER NOT NULL,
  ID_LANCAMENTO       INTEGER NOT NULL,
  ID_MEDIDOR          INTEGER NOT NULL,
  VL_LEITURA_ATUAL    DECIMAL(10, 2),
  VL_LEITURA_ANTERIOR DECIMAL(10, 2)
);
CREATE UNIQUE INDEX USRDBVAH.PK_LEITURAS_IDX ON USRDBVAH.TB_LANCA_LEITURAS
(
  ID ASC
);
ALTER TABLE USRDBVAH.TB_LANCA_LEITURAS ADD CONSTRAINT PK_LEITURAS PRIMARY KEY (ID) USING INDEX USRDBVAH.PK_LEITURAS_IDX;
ALTER TABLE USRDBVAH.TB_LANCA_LEITURAS ADD CONSTRAINT FK_LEITURAS_LANCAMENTO FOREIGN KEY (ID_LANCAMENTO) REFERENCES DBAMV.TB_LANCA_LANCAMENTO (ID);
ALTER TABLE USRDBVAH.TB_LANCA_LEITURAS ADD CONSTRAINT FK_LEITURAS_MEDIDOR FOREIGN KEY (ID_MEDIDOR) REFERENCES USRDBVAH.TB_LANCA_MEDIDOR (ID);

// TABELA DE

CREATE TABLE USRDBVAH.TB_LANCA_LAN_CON_REC
(
  ID NUMBER(*),
  CD_CON_REC NUMBER (*),
  CONSTRAINT TB_LANCA_LAN_CON_REC_PK PRIMARY KEY (ID, CD_CON_REC),
  CONSTRAINT FK_LANCA_LAN_CON_REC_1 FOREIGN KEY (ID) REFERENCES USRDBVAH.TB_LANCA_LANCAMENTO (ID),
  CONSTRAINT FK_LANCA_LAN_CON_REC_2 FOREIGN KEY (CD_CON_REC) REFERENCES DBAMV.CON_REC (CD_CON_REC)
);

// CONSTRAINTS

ALTER TABLE USRDBVAH.TB_LANCA_USUARIO_SERVICO ADD CONSTRAINT CONSTRAINT_1 PRIMARY KEY (ID_PUSER, ID_SERVICO) USING INDEX USRDBVAH.PRIMARY_KEY_1;
ALTER TABLE USRDBVAH.TB_LANCA_USUARIO_SERVICO ADD CONSTRAINT FK_LAN_USU_SER_USU FOREIGN KEY (ID_PUSER) REFERENCES USRDBVAH.TB_LANCA_USUARIO (ID) NOT DEFERRABLE;
ALTER TABLE USRDBVAH.TB_LANCA_LANCAMENTO ADD CONSTRAINT FK_LAN_LAN_USU FOREIGN KEY (ID_USU_LANCADOR) REFERENCES USRDBVAH.TB_LANCA_USUARIO (ID) NOT DEFERRABLE;
ALTER TABLE USRDBVAH.TB_LANCA_CLIENTE_SETOR ADD CONSTRAINT FK_CLI_SET_FOR FOREIGN KEY (CD_FORNECEDOR) REFERENCES DBAMV.FORNECEDOR (CD_FORNECEDOR) NOT DEFERRABLE;
ALTER TABLE USRDBVAH.TB_LANCA_CLIENTE_SETOR ADD CONSTRAINT FK_CLI_SET_SET FOREIGN KEY (CD_SETOR) REFERENCES DBAMV.SETOR (CD_SETOR) NOT DEFERRABLE;
ALTER TABLE USRDBVAH.TB_LANCA_CONTRATO_SETOR ADD CONSTRAINT FK_CON_CON_SET FOREIGN KEY (ID_CONTRATO) REFERENCES USRDBVAH.TB_LANCA_CONTRATO (ID) NOT DEFERRABLE;
ALTER TABLE USRDBVAH.TB_LANCA_CONTRATO ADD CONSTRAINT FK_CON_FOR FOREIGN KEY (ID_FORNECEDOR_CONTRATANTE) REFERENCES DBAMV.FORNECEDOR (CD_FORNECEDOR) NOT DEFERRABLE;
ALTER TABLE USRDBVAH.TB_LANCA_CONTRATO_SETOR ADD CONSTRAINT FK_CON_SET_FOR FOREIGN KEY (ID_CLIENTE) REFERENCES DBAMV.FORNECEDOR (CD_FORNECEDOR) NOT DEFERRABLE;
ALTER TABLE USRDBVAH.TB_LANCA_CONTRATO_SETOR ADD CONSTRAINT FK_CON_SET_SET FOREIGN KEY (ID_SETOR) REFERENCES DBAMV.SETOR (CD_SETOR) NOT DEFERRABLE;
ALTER TABLE USRDBVAH.TB_LANCA_LANCAMENTO_VALORES ADD CONSTRAINT FK_LAN FOREIGN KEY (ID_LANCAMENTO) REFERENCES USRDBVAH.TB_LANCA_LANCAMENTO (ID) NOT DEFERRABLE;
ALTER TABLE USRDBVAH.TB_LANCA_COMENTARIOS ADD CONSTRAINT FK_LAN_COM_LAN FOREIGN KEY (ID_LANCAMENTO) REFERENCES USRDBVAH.TB_LANCA_LANCAMENTO (ID) NOT DEFERRABLE;
ALTER TABLE USRDBVAH.TB_LANCA_COMENTARIOS ADD CONSTRAINT FK_LAN_CON_USU FOREIGN KEY (ID_PUSER) REFERENCES USRDBVAH.TB_LANCA_USUARIO (ID) NOT DEFERRABLE;
ALTER TABLE USRDBVAH.TB_LANCA_LANCAMENTO ADD CONSTRAINT FK_LAN_SER FOREIGN KEY (ID_SERVICO) REFERENCES USRDBVAH.TB_LANCA_SERVICO (ID) NOT DEFERRABLE;
ALTER TABLE USRDBVAH.TB_LANCA_SERVICO ADD CONSTRAINT FK_LAN_SER_CONT FOREIGN KEY (CD_CONTA_CONTABIL) REFERENCES DBAMV.PLANO_CONTAS (CD_REDUZIDO) NOT DEFERRABLE;
ALTER TABLE USRDBVAH.TB_LANCA_SERVICO ADD CONSTRAINT FK_LAN_SER_CON_CUS FOREIGN KEY (CD_CONTA_CUSTO) REFERENCES DBAMV.ITEM_RES (CD_ITEM_RES) NOT DEFERRABLE;
ALTER TABLE USRDBVAH.TB_LANCA_SERVICO ADD CONSTRAINT FK_LAN_SER_RES FOREIGN KEY (CD_CONTA_RESULTADO) REFERENCES DBAMV.PLANO_CONTAS (CD_REDUZIDO) NOT DEFERRABLE;
ALTER TABLE USRDBVAH.TB_LANCA_LANCAMENTO ADD CONSTRAINT FK_LAN_SER_VAL FOREIGN KEY (ID_SERVICO_VALORES) REFERENCES USRDBVAH.TB_LANCA_SERVICO_VALORES (ID) NOT DEFERRABLE;
ALTER TABLE USRDBVAH.TB_LANCA_SERVICO ADD CONSTRAINT FK_LAN_SERv1 FOREIGN KEY (CD_TP_DOCUMENTO) REFERENCES DBAMV.TIP_DOC (CD_TIP_DOC) NOT DEFERRABLE;
ALTER TABLE USRDBVAH.TB_LANCA_LANCAMENTO_VALORES ADD CONSTRAINT FK_LAN_VAL_CON_SET FOREIGN KEY (ID_CONTRATO_SERVICO) REFERENCES USRDBVAH.TB_LANCA_CONTRATO_SETOR (ID) NOT DEFERRABLE;
ALTER TABLE USRDBVAH.TB_LANCA_SERVICO ADD CONSTRAINT FK_LAN_SER_HIS_PAD FOREIGN KEY (CD_HISTORICO_PADRAO) REFERENCES DBAMV.HISTORICO_PADRAO (CD_HISTORICO_PADRAO) NOT DEFERRABLE;
ALTER TABLE USRDBVAH.TB_LANCA_SER_CON_SER ADD CONSTRAINT FK_SER_CON_SER_CON_SET FOREIGN KEY (ID) REFERENCES USRDBVAH.TB_LANCA_CONTRATO_SETOR (ID) NOT DEFERRABLE;
ALTER TABLE USRDBVAH.TB_LANCA_SER_CON_SER ADD CONSTRAINT FK_SER_CON_SER_SER FOREIGN KEY (ID_SERVICO) REFERENCES USRDBVAH.TB_LANCA_SERVICO (ID) NOT DEFERRABLE;
ALTER TABLE USRDBVAH.TB_LANCA_SERVICO_VALORES ADD CONSTRAINT FK_SER_VAL FOREIGN KEY (ID_SERVICO) REFERENCES USRDBVAH.TB_LANCA_SERVICO (ID) NOT DEFERRABLE;
ALTER TABLE USRDBVAH.TB_LANCA_USUARIO_SERVICO ADD CONSTRAINT FK_USU_SER_SER FOREIGN KEY (ID_SERVICO) REFERENCES USRDBVAH.TB_LANCA_SERVICO (ID) NOT DEFERRABLE;
ALTER TABLE USRDBVAH.TB_LANCA_USUARIO_ROLE ADD CONSTRAINT CONSTRAINT_9 PRIMARY KEY (ID_PUSER, CD_ROLE) USING INDEX USRDBVAH.PK_USU_ROL;
ALTER TABLE USRDBVAH.TB_LANCA_USUARIO_ROLE ADD CONSTRAINT FK_LAN_USER_ROLE FOREIGN KEY (ID_PUSER) REFERENCES USRDBVAH.TB_LANCA_USUARIO (ID);

CREATE SEQUENCE USRDBVAH.SEQ_LANCA_USUARIO;
CREATE SEQUENCE USRDBVAH.SEQ_LANCA_CONTRATO;
CREATE SEQUENCE USRDBVAH.SEQ_LANCA_CONTRATO_SETOR;
CREATE SEQUENCE USRDBVAH.SEQ_LANCA_SERVICO;
CREATE SEQUENCE USRDBVAH.SEQ_LANCA_LANC_VALORES;
CREATE SEQUENCE USRDBVAH.SEQ_LANCA_LANCAMENTO;
CREATE SEQUENCE USRDBVAH.SEQ_LANCA_COMENTARIOS;
CREATE SEQUENCE USRDBVAH.SEQ_LANCA_SERV_VALORES;
CREATE SEQUENCE USRDBVAH.SEQ_LANCA_SER_SEC_CON;
CREATE SEQUENCE USRDBVAH.SEQ_LANCA_SETOR_DETALHE;
CREATE SEQUENCE USRDBVAH.SEQ_LANCA_MEDIDOR;
CREATE SEQUENCE USRDBVAH.SEQ_LANCA_SETOR_MEDIDOR;
CREATE SEQUENCE USRDBVAH.SEQ_LANCA_LEITURAS;
CREATE SEQUENCE USRDBVAH.SEQ_NR_DOC_CON_REC;
