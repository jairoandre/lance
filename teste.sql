SELECT
  'Empresa ' || e.cd_multi_empresa || ' - ' || e.ds_multi_empresa                                       Empresa,
  e.cd_multi_empresa                                                                                    cd_multi_empresa,
  V1.CD_BANCO                                                                                           CD_BANCO,
  e.cd_multi_empresa || ' - ' || e.ds_multi_empresa                                                     Empresa_o,
  V1.DS_BANCO                                                                                           DS_BANCO,
  V1.CD_CON_COR                                                                                         CD_CON_COR,
  V1.DS_CON_COR                                                                                         DS_CON_COR,
  V1.CD_AGENCIA                                                                                         CD_AGENCIA,
  V1.DS_AGENCIA                                                                                         DS_AGENCIA,
  V1.NR_CONTA                                                                                           NR_CONTA,
  V1.VL_SLD_ATUAL_CONTA                                                                                 VL_SLD_ATUAL_CONTA,
  V1.VL_CHQ_DEP_NAO_COMP                                                                                VL_CHQ_DEP_NAO_COMP,
  V1.CD_CHQ_DEP_NAO_COMP                                                                                CD_CHQ_DEP_NAO_COMP,
  V1.VL_CHQ_EMI_NAO_COMP                                                                                VL_CHQ_EMI_NAO_COMP,
  V1.CD_CHQ_EMI_NAO_COMP                                                                                CD_CHQ_EMI_NAO_COMP,
  V1.SN_CONTA_ESPECIAL                                                                                  SN_CONTA_ESPECIAL,
  V1.VL_LIMITE_CHEQUE_ESPECIAL                                                                          VL_LIMITE_CHEQUE_ESPECIAL,
  decode(v2.dt_estorno, NULL, DECODE(V2.CD_CHEQUE, NULL, V2.DT_MOVIMENTACAO, decode('C', 'C',
                                                                                    decode(v2.dt_compensacao, NULL,
                                                                                           Decode(366, 967,
                                                                                                  v2.dt_compensacao,
                                                                                                  v2.dt_emissao),
                                                                                           Decode(366, 374,
                                                                                                  v2.dt_compensacao,
                                                                                                  v2.dt_movimentacao)),
                                                                                    v2.dt_emissao)),
         v2.dt_estorno)                                                                                 DT_MOVIMENTACAO,
  V2.NR_DOCUMENTO_IDENTIFICACAO                                                                         NR_DOCUMENTO_IDENTIFICACAO,
  V2.CD_MOV_CONCOR                                                                                      CD_MOV_CONCOR,
  V2.DS_MOVIMENTACAO                                                                                    DS_MOVIMENTACAO,
  Decode('L', 'A', V2.VL_MOVIMENTACAO, V2.VL_MOVIMENTACAO - Abs(Nvl(Tar.Vl_Movimentacao, 0))) VL_MOVIMENTACAO,
  V2.TP_OPERACAO_SALDO_CONTA                                                                            TP_OPERACAO_SALDO_CONTA,
  V2.Cd_Mov_Deposito_Agrup
FROM DBAMV.V_BCO_CONTA V1, dbamv.multi_empresas e, DBAMV.V_MOV_CONCOR V2, (SELECT
                                                                             count(*)            total,
                                                                             count(decode(M.Sn_Conciliado, 'S', 'S',
                                                                                          NULL)) sim,
                                                                             m.cd_mov_deposito_agrup
                                                                           FROM dbamv.mov_concor M
                                                                           WHERE m.cd_mov_deposito_agrup IS NOT NULL
                                                                           GROUP BY
                                                                             M.cd_mov_deposito_agrup) Tb_Conciliacao,
  (SELECT
     Cd_Mov_Pai,
     Nvl(Sum(Vl_Movimentacao), 0) Vl_Movimentacao
   FROM Dbamv.V_Mov_Concor M
   WHERE 'L' = 'L' AND M.Cd_Estrutural = '1.6.1.1.10.3'
   GROUP BY Cd_Mov_Pai) Tar
WHERE V1.CD_CON_COR = V2.CD_CON_COR AND V1.cd_multi_empresa = e.cd_multi_empresa AND
      V2.Cd_Mov_Deposito_Agrup = Tb_Conciliacao.Cd_Mov_Deposito_Agrup (+) AND V1.CD_CON_COR = V2.CD_CON_COR AND
      NVL(V2.VL_MOVIMENTACAO, 0) <> 0 AND V2.Cd_Mov_ConCor = Tar.Cd_Mov_Pai (+) AND V1.CD_BANCO = 341 AND
      e.cd_multi_empresa = '1' AND V2.CD_PROCESSO = 155 AND decode(v2.dt_estorno, NULL,
                                                                   DECODE(V2.CD_CHEQUE, NULL, V2.DT_MOVIMENTACAO,
                                                                          decode('C', 'C',
                                                                                 decode(v2.dt_compensacao, NULL,
                                                                                        Decode(366, 967,
                                                                                               v2.dt_compensacao,
                                                                                               v2.dt_emissao),
                                                                                        Decode(366, 374,
                                                                                               v2.dt_compensacao,
                                                                                               v2.dt_movimentacao)),
                                                                                 v2.dt_emissao)), v2.dt_estorno) >=
                                                            TO_DATE(TO_CHAR('03/08/2016', 'DD/MM/YYYY') || ' 00:00:00',
                                                                    'DD/MM/YYYY HH24:MI:SS') AND
      decode(v2.dt_estorno, NULL, DECODE(V2.CD_CHEQUE, NULL, V2.DT_MOVIMENTACAO, decode('C', 'C',
                                                                                        decode(v2.dt_compensacao, NULL,
                                                                                               Decode(366, 967,
                                                                                                      v2.dt_compensacao,
                                                                                                      v2.dt_emissao),
                                                                                               Decode(366, 374,
                                                                                                      v2.dt_compensacao,
                                                                                                      v2.dt_movimentacao)),
                                                                                        v2.dt_emissao)), v2.dt_estorno)
      <= TO_DATE(TO_CHAR('03/08/2016', 'DD/MM/YYYY') || ' 23:59:59', 'DD/MM/YYYY HH24:MI:SS') AND ('N' = 'N' OR
                                                                                                  ('N' = 'S' AND
                                                                                                   (
                                                                                                     V2.CD_MOV_DEPOSITO_AGRUP
                                                                                                     IS NULL OR (
                                                                                                       V2.CD_MOV_DEPOSITO_AGRUP
                                                                                                       IS NOT NULL AND
                                                                                                       Tb_Conciliacao.Total
                                                                                                       <>
                                                                                                       Tb_Conciliacao.Sim
                                                                                                       AND
                                                                                                       Tb_Conciliacao.Sim
                                                                                                       <> 0)))) AND
      (('L' = 'L' AND V2.CD_ESTRUTURAL <> '1.6.1.1.10.3') OR ('L' = 'A'))
UNION SELECT
        'Empresa ' || e.cd_multi_empresa || ' - ' || e.ds_multi_empresa                          Empresa,
        e.cd_multi_empresa                                                                       cd_multi_empresa,
        V1.CD_BANCO                                                                              CD_BANCO,
        e.cd_multi_empresa || ' - ' || e.ds_multi_empresa                                        Empresa_o,
        V1.DS_BANCO                                                                              DS_BANCO,
        V1.CD_CON_COR                                                                            CD_CON_COR,
        V1.DS_CON_COR                                                                            DS_CON_COR,
        V1.CD_AGENCIA                                                                            CD_AGENCIA,
        V1.DS_AGENCIA                                                                            DS_AGENCIA,
        V1.NR_CONTA                                                                              NR_CONTA,
        V1.VL_SLD_ATUAL_CONTA                                                                    VL_SLD_ATUAL_CONTA,
        V1.VL_CHQ_DEP_NAO_COMP                                                                   VL_CHQ_DEP_NAO_COMP,
        V1.CD_CHQ_DEP_NAO_COMP                                                                   CD_CHQ_DEP_NAO_COMP,
        V1.VL_CHQ_EMI_NAO_COMP                                                                   VL_CHQ_EMI_NAO_COMP,
        V1.CD_CHQ_EMI_NAO_COMP                                                                   CD_CHQ_EMI_NAO_COMP,
        V1.SN_CONTA_ESPECIAL                                                                     SN_CONTA_ESPECIAL,
        V1.VL_LIMITE_CHEQUE_ESPECIAL                                                             VL_LIMITE_CHEQUE_ESPECIAL,
        decode(v2.dt_estorno, NULL, DECODE(V2.CD_CHEQUE, NULL, V2.DT_MOVIMENTACAO, decode('C', 'C',
                                                                                          decode(v2.dt_compensacao,
                                                                                                 NULL,
                                                                                                 Decode(366,
                                                                                                        967,
                                                                                                        v2.dt_compensacao,
                                                                                                        v2.dt_emissao),
                                                                                                 Decode(366,
                                                                                                        374,
                                                                                                        v2.dt_compensacao,
                                                                                                        v2.dt_movimentacao)),
                                                                                          v2.dt_emissao)),
               v2.dt_estorno),
        'AG-' || To_Char(
            V2.Cd_Mov_Deposito_Agrup)                                                            NR_DOCUMENTO_IDENTIFICACAO,
        0                                                                                        CD_MOV_CONCOR,
        decode(V2.TP_OPERACAO_SALDO_CONTA, '+',
               Decode(v2.CD_MOV_CAIXA, NULL, 'CRED. AGRUP. MVSAUDE', 'DEP. AGRUP. CAIXA'), '-',
               Decode(v2.cd_bordero, NULL, 'AGRUPADO', 'PGTO BORD. ' || To_Char(v2.cd_bordero))) DS_MOVIMENTACAO,
        Sum(Decode('L', 'A', V2.VL_MOVIMENTACAO,
                   V2.VL_MOVIMENTACAO - Abs(Nvl(Tar.Vl_Movimentacao, 0))))                       VL_MOVIMENTACAO,
        V2.TP_OPERACAO_SALDO_CONTA                                                               TP_OPERACAO_SALDO_CONTA,
        V2.Cd_Mov_Deposito_Agrup
      FROM DBAMV.V_BCO_CONTA V1, dbamv.multi_empresas e, DBAMV.V_MOV_CONCOR V2, (SELECT
                                                                                   count(*)          total,
                                                                                   count(
                                                                                       decode(M.Sn_Conciliado, 'S', 'S',
                                                                                              NULL)) sim,
                                                                                   m.cd_mov_deposito_agrup
                                                                                 FROM dbamv.mov_concor M
                                                                                 WHERE
                                                                                   m.cd_mov_deposito_agrup IS NOT NULL
                                                                                 GROUP BY
                                                                                   M.cd_mov_deposito_agrup) Tb_Conciliacao,
        (SELECT
           Cd_Mov_Pai,
           Nvl(Sum(Vl_Movimentacao), 0) Vl_Movimentacao
         FROM Dbamv.V_Mov_Concor M
         WHERE 'L' = 'L' AND M.Cd_Estrutural = '1.6.1.1.10.3'
         GROUP BY Cd_Mov_Pai) Tar
      WHERE V1.CD_CON_COR = V2.CD_CON_COR AND V1.cd_multi_empresa = e.cd_multi_empresa AND
            V2.Cd_Mov_Deposito_Agrup = Tb_Conciliacao.Cd_Mov_Deposito_Agrup (+) AND 'N' = 'S' AND
            V1.CD_CON_COR = V2.CD_CON_COR AND NVL(V2.VL_MOVIMENTACAO, 0) <> 0 AND
            (V2.CD_MOV_DEPOSITO_AGRUP IS NOT NULL AND Tb_Conciliacao.Total = Tb_Conciliacao.Sim OR
             Tb_Conciliacao.Sim = 0) AND V2.Cd_Mov_ConCor = Tar.Cd_Mov_Pai (+) AND
            (('L' = 'L' AND V2.CD_ESTRUTURAL <> '1.6.1.1.10.3') OR ('L' = 'A')) AND
            V1.CD_BANCO = 341 AND e.cd_multi_empresa = '1' AND V2.CD_PROCESSO = 155 AND decode(v2.dt_estorno, NULL,
                                                                                               DECODE(V2.CD_CHEQUE,
                                                                                                      NULL,
                                                                                                      V2.DT_MOVIMENTACAO,
                                                                                                      decode(
                                                                                                          'C',
                                                                                                          'C', decode(
                                                                                                              v2.dt_compensacao,
                                                                                                              NULL,
                                                                                                              Decode(
                                                                                                                  366,
                                                                                                                  967,
                                                                                                                  v2.dt_compensacao,
                                                                                                                  v2.dt_emissao),
                                                                                                              Decode(
                                                                                                                  366,
                                                                                                                  374,
                                                                                                                  v2.dt_compensacao,
                                                                                                                  v2.dt_movimentacao)),
                                                                                                          v2.dt_emissao)),
                                                                                               v2.dt_estorno) >=
                                                                                        TO_DATE(TO_CHAR('03/08/2016',
                                                                                                        'DD/MM/YYYY') ||
                                                                                                ' 00:00:00',
                                                                                                'DD/MM/YYYY HH24:MI:SS')
            AND decode(v2.dt_estorno, NULL, DECODE(V2.CD_CHEQUE, NULL, V2.DT_MOVIMENTACAO, decode('C', 'C',
                                                                                                  decode(
                                                                                                      v2.dt_compensacao,
                                                                                                      NULL, Decode(
                                                                                                          366,
                                                                                                          967,
                                                                                                          v2.dt_compensacao,
                                                                                                          v2.dt_emissao),
                                                                                                      Decode(
                                                                                                          366,
                                                                                                          374,
                                                                                                          v2.dt_compensacao,
                                                                                                          v2.dt_movimentacao)),
                                                                                                  v2.dt_emissao)),
                       v2.dt_estorno) <=
                TO_DATE(TO_CHAR('03/08/2016', 'DD/MM/YYYY') || ' 23:59:59', 'DD/MM/YYYY HH24:MI:SS')
      GROUP BY 'Empresa ' || e.cd_multi_empresa || ' - ' || e.ds_multi_empresa, e.cd_multi_empresa,
        e.cd_multi_empresa || ' - ' || e.ds_multi_empresa, V2.Cd_Mov_Deposito_Agrup,
        decode(V2.TP_OPERACAO_SALDO_CONTA, '+',
               Decode(v2.CD_MOV_CAIXA, NULL, 'CRED. AGRUP. MVSAUDE', 'DEP. AGRUP. CAIXA'), '-',
               Decode(v2.cd_bordero, NULL, 'AGRUPADO', 'PGTO BORD. ' || To_Char(v2.cd_bordero))),
        V2.TP_OPERACAO_SALDO_CONTA, 'AG-' || To_Char(V2.Cd_Mov_Deposito_Agrup), V1.CD_BANCO, V1.DS_BANCO, V1.CD_CON_COR,
        V1.DS_CON_COR, V1.CD_AGENCIA, V1.DS_AGENCIA, V1.NR_CONTA, V1.VL_SLD_ATUAL_CONTA, V1.VL_CHQ_DEP_NAO_COMP,
        V1.CD_CHQ_DEP_NAO_COMP, V1.VL_CHQ_EMI_NAO_COMP, V1.CD_CHQ_EMI_NAO_COMP, V1.SN_CONTA_ESPECIAL,
        V1.VL_LIMITE_CHEQUE_ESPECIAL, decode(v2.dt_estorno, NULL, DECODE(V2.CD_CHEQUE, NULL, V2.DT_MOVIMENTACAO,
                                                                         decode('C', 'C',
                                                                                decode(v2.dt_compensacao, NULL,
                                                                                       Decode(366, 967,
                                                                                              v2.dt_compensacao,
                                                                                              v2.dt_emissao),
                                                                                       Decode(366, 374,
                                                                                              v2.dt_compensacao,
                                                                                              v2.dt_movimentacao)),
                                                                                v2.dt_emissao)), v2.dt_estorno)
ORDER BY 1 ASC, 2 ASC, 4 ASC, 7 ASC, 9 ASC, 3 ASC, 5 ASC, 6 ASC, 8 ASC, 10 ASC, 11 ASC, 16 ASC, 17 ASC, 13 ASC, 15 ASC,
  12 ASC, 14 ASC, 18 ASC, DS_CON_COR, DS_AGENCIA, NR_DOCUMENTO_IDENTIFICACAO
