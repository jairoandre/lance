
  CREATE OR REPLACE FORCE VIEW "USRDBVAH"."VW_VDIC_ATENDI_PRONT_PRES" ("CD_UNID_INT", "UNIDADE_INTERNACAO", "DT_REFERENCIA", "CD_ATENDIMENTO", "COD_ITEM_PRONTUARIO", "TIPO_ITEM_PRONTUARIO", "NOME_DOCUMENTO", "CD_CONSELHO_PROF", "MN_PROFISSIONAL", "DH_CRIACAO", "DH_IMPRESSAO") AS 
  select DISTINCT UI.cd_unid_int,
       UI.DS_UNID_INT           unidade_internacao,
       vp.dt_referencia,
       doc.cd_atendimento,
       doc.codigo cod_item_prontuario,
       doc.tipo_item_prontuario,
       doc.ds_documento         nome_documento,
       doc.cd_conselho_prof,
       doc.mn_profissional,
       doc.dt_registro as dh_criacao,
       doc.dt_registro as dh_impressao
  from dbamv.vdic_vali_presc_teste_at vp --usrdbvah.tb_validade_prescricao vp
  join (select rd.cd_atendimento,
               rd.cd_registro_documento codigo,
               'Documento de Prontuário' tipo_item_prontuario,
               d.ds_documento,
               rd.dt_registro,
               CON.DS_CONSELHO cd_conselho_prof,
               pr.nm_prestador mn_profissional
          from dbamv.registro_documento rd
          join dbamv.documento d
            on (d.cd_documento = rd.cd_documento)
           and (d.cd_documento not in (53,84,122,130,148,69))
          left join dbasgu.usuarios u
            on (u.cd_usuario = rd.nm_usuario)
          left join dbamv.prestador pr
            on (pr.cd_prestador = u.cd_prestador)
          left join dbamv.conselho con
            on (con.cd_conselho = pr.cd_conselho)
         where rd.sn_impresso = 'S'
           and rd.cd_multi_empresa = 1) doc
    on (doc.dt_registro between vp.inicio_validade and vp.fim_validade)
   and (doc.cd_atendimento = vp.cd_atendimento)
  join dbamv.unid_int ui
    on (ui.CD_UNID_INT = vp.cd_unid_int)

union all

-- Evolução / Anotação de enfermagem
select DISTINCT
       ui.cd_unid_int,
       ui.DS_UNID_INT,
       vp.dt_referencia,
       eve.cd_atendimento,
       eve.codigo cod_item_prontuario,
       eve.tipo_item_prontuario,
       null,
       eve.cd_conselho_prof,
       eve.mn_profissional,
       eve.dh_criacao, eve.dh_impressao
  from dbamv.vdic_vali_presc_teste_at vp --usrdbvah.tb_validade_prescricao vp
  join (select ee.cd_atendimento,
               'Evolução/Anotação' tipo_item_prontuario,
               ee.cd_evo_enf codigo,
               to_date(to_char(ee.dt_evo_enf, 'dd/mm/yyyy') || ' ' ||
                       to_char(ee.hr_evo_enf, 'hh24:mi'),
                       'dd/mm/yyyy hh24:mi') dh_evo_enf,
               CON.DS_CONSELHO cd_conselho_prof,
               p.nm_prestador mn_profissional,
               to_date(to_char(ee.dt_evo_enf, 'dd/mm/yyyy') || ' ' ||
                       to_char(ee.hr_evo_enf, 'hh24:mi'),
                       'dd/mm/yyyy hh24:mi') as dh_criacao, ee.dt_impressao dh_impressao
          from dbamv.evo_enf ee
          join dbamv.prestador p
            on (p.cd_prestador = ee.cd_prestador)
          left join dbamv.conselho con
            on (con.cd_conselho = p.cd_conselho)
         where ee.dt_impressao is not null
           and ee.ds_evo_enf is not null) eve
    on (eve.dh_evo_enf between vp.inicio_validade and vp.fim_validade)
   and (eve.cd_atendimento = vp.cd_atendimento)
  join dbamv.unid_int ui
    on (ui.CD_UNID_INT = vp.cd_unid_int)

union all

-- Prescrição

select DISTINCT
       vp.cd_unid_int,
       vp.DS_UNID_INT,
       vp.dt_referencia,
       prc.cd_atendimento,
       prc.codigo cod_item_prontuario,
       prc.tipo_item_prontuario,
       null,
       prc.cd_conselho_prof,
       prc.mn_profissional,
       prc.dh_criacao, prc.dh_impressao
  from dbamv.vdic_vali_presc_teste_at vp --usrdbvah.tb_validade_prescricao vp
  join (select pm.cd_atendimento,
               pm.cd_pre_med codigo,
               'Prescrição' tipo_item_prontuario,
               pm.dt_referencia,
               pm.cd_unid_int,
               CON.DS_CONSELHO cd_conselho_prof,
               p.nm_prestador mn_profissional,
               pm.dh_criacao, pm.dh_impressao
          from dbamv.pre_med pm
          join dbamv.prestador p
            on (p.cd_prestador = pm.cd_prestador)
          left join dbamv.conselho con
            on (con.cd_conselho = p.cd_conselho)
         where pm.dh_impressao is not null
           and (select count(ipm.cd_pre_med)
                  from dbamv.itpre_med ipm
                 where ipm.cd_pre_med = pm.cd_pre_med) > 0) prc
    on (prc.cd_atendimento = vp.cd_atendimento)
   --and (prc.dt_referencia = vp.dt_referencia)
   /*and (prc.dh_impressao between vp.inicio_validade and vp.fim_validade
        OR ((prc.dh_impressao +1) between vp.inicio_validade and vp.fim_validade)
   )*/
   and (prc.dt_referencia = vp.dt_referencia)
   and (prc.cd_unid_int = vp.cd_unid_int)

union all

-- Evolução

select DISTINCT vp.cd_unid_int,
       vp.DS_UNID_INT,
       vp.dt_referencia,
       ev.cd_atendimento,
       ev.codigo cod_item_prontuario,
       ev.tipo_item_prontuario,
       null,
       ev.cd_conselho_prof,
       ev.mn_profissional_medico,
       ev.dh_criacao, ev.dh_impressao
  from dbamv.vdic_vali_presc_teste_at vp --usrdbvah.tb_validade_prescricao vp
  join (select t.cd_atendimento,
               'Evolução' tipo_item_prontuario,
               t.cd_pre_med codigo,
               t.dt_referencia,
               t.cd_unid_int,
               CON.DS_CONSELHO cd_conselho_prof,
               p.nm_prestador mn_profissional_medico,
               t.dh_criacao, t.dh_impressao
          from dbamv.pre_med t
          join dbamv.prestador p
            on (p.cd_prestador = t.cd_prestador)
          left join dbamv.conselho con
            on (con.cd_conselho = p.cd_conselho)
         where t.dh_impressao is not null
           and t.ds_evolucao is not null
           --and t.cd_pre_med = 1417032
       ) ev
    on (ev.cd_atendimento = vp.cd_atendimento)
   --and ((ev.dt_referencia = vp.dt_referencia))
   --and (ev.dh_impressao between vp.inicio_validade and vp.fim_validade)
         --or (ev.dt_referencia = vp.dt_referencia)
   and ((ev.dt_referencia = vp.dt_referencia))
   and (ev.cd_unid_int = vp.cd_unid_int)


---Prescrição/Evolução feita antes da primeira prescrição cuja data de referência seja menor que a primeira -----------------------------

union all

-- Prescrição

select DISTINCT
       vp.cd_unid_int,
       vp.DS_UNID_INT,
       vp.dt_referencia,
       prc.cd_atendimento,
       prc.codigo cod_item_prontuario,
       prc.tipo_item_prontuario,
       null,
       prc.cd_conselho_prof,
       prc.mn_profissional,
       prc.dh_criacao, prc.dh_impressao
  from dbamv.vdic_vali_presc_teste_at vp --usrdbvah.tb_validade_prescricao vp
  join (select pm.cd_atendimento,
               pm.cd_pre_med codigo,
               'Prescrição' tipo_item_prontuario,
               pm.dt_referencia,
               pm.cd_unid_int,
               CON.DS_CONSELHO cd_conselho_prof,
               p.nm_prestador mn_profissional,
               pm.dh_criacao, pm.dh_impressao
          from dbamv.pre_med pm
          join dbamv.prestador p
            on (p.cd_prestador = pm.cd_prestador)
          left join dbamv.conselho con
            on (con.cd_conselho = p.cd_conselho)
         where pm.dh_impressao is not null
           and (select count(ipm.cd_pre_med)
                  from dbamv.itpre_med ipm
                 where ipm.cd_pre_med = pm.cd_pre_med) > 0
           --and pm.cd_pre_med = 1452256
           ) prc
    on (prc.cd_atendimento = vp.cd_atendimento)
   and ((prc.dt_referencia < vp.dt_referencia) AND (prc.dh_impressao between vp.inicio_validade and vp.fim_validade))
   and (prc.cd_unid_int = vp.cd_unid_int)
   --and (prc.dh_impressao between vp.inicio_validade and vp.fim_validade)
   --and (prc.dh_impressao between vp.inicio_validade and vp.fim_validade
     /*and (prc.dt_referencia = vp.dt_referencia
        OR ((prc.dh_impressao) between vp.inicio_validade and vp.fim_validade))*/


union all

-- Evolução

select DISTINCT vp.cd_unid_int,
       vp.DS_UNID_INT,
       vp.dt_referencia,
       ev.cd_atendimento,
       ev.codigo cod_item_prontuario,
       ev.tipo_item_prontuario,
       null,
       ev.cd_conselho_prof,
       ev.mn_profissional_medico,
       ev.dh_criacao, ev.dh_impressao
  from dbamv.vdic_vali_presc_teste_at vp --usrdbvah.tb_validade_prescricao vp
  join (select t.cd_atendimento,
               'Evolução' tipo_item_prontuario,
               t.cd_pre_med codigo,
               t.dt_referencia,
               t.cd_unid_int,
               CON.DS_CONSELHO cd_conselho_prof,
               p.nm_prestador mn_profissional_medico,
               t.dh_criacao, t.dh_impressao
          from dbamv.pre_med t
          join dbamv.prestador p
            on (p.cd_prestador = t.cd_prestador)
          left join dbamv.conselho con
            on (con.cd_conselho = p.cd_conselho)
         where t.dh_impressao is not null
           and t.ds_evolucao is not null
           --and t.cd_pre_med = 1442539
       ) ev
    on (ev.cd_atendimento = vp.cd_atendimento)
   --and ((ev.dt_referencia = vp.dt_referencia))
   and ((ev.dt_referencia < vp.dt_referencia) AND (ev.dh_impressao between vp.inicio_validade and vp.fim_validade))
   and (ev.cd_unid_int = vp.cd_unid_int)
   --and (ev.dh_impressao between vp.inicio_validade and vp.fim_validade)
   --and (ev.dh_impressao between vp.inicio_validade and vp.fim_validade
    /*and (ev.dt_referencia = vp.dt_referencia
        OR ((ev.dh_impressao+1) between vp.inicio_validade and vp.fim_validade)
   )*/
         --or (ev.dt_referencia = vp.dt_referencia)


----------------- Fim ::: Prescrição/Evolução feita antes da primeira prescrição cuja data de referência seja menor que a primeira ::: ----------

 union all

-- Descrição cirurgica

select distinct dc.cd_unid_int, dc.DS_UNID_INT,
       vp.dt_referencia,
       dc.cd_atendimento,
       cd_aviso_cirurgia cod_item_prontuario,
       'Descrição Cirurgica' tipo_item_prontuario,
       null,
       null,
       null,
       null dh_criacao,
       null
  from dbamv.vdic_vali_presc_teste_at vp --usrdbvah.tb_validade_prescricao vp
  join (select s.Cd_Setor cd_unid_int,
               s.nm_setor DS_UNID_INT,
               trunc(c.dt_inicio_cirurgia) dt_referencia,
               c.cd_atendimento,
               c.cd_aviso_cirurgia,
               'Descrição Cirurgica' tipo_item_prontuario,
               decode (trunc(a.dt_atendimento),trunc(c.dt_inicio_cirurgia),
               to_date((to_char(a.dt_atendimento, 'dd/mm/yyyy') || ' ' || to_char(a.hr_atendimento, 'hh24:mi')), 'dd/mm/yyyy hh24:mi'),
               trunc(c.dt_inicio_cirurgia)) inicio_validade,
               trunc(c.dt_inicio_cirurgia) + 1 - 1 / 24 / 60 / 60 fim_validade
          from dbamv.tb_aviso_cirurgia c
          join dbamv.sal_cir sc on (sc.cd_sal_cir = c.cd_sal_cir)
          join dbamv.tb_setor s on (s.cd_setor = sc.cd_setor)
          join dbamv.tb_atendime a on (a.cd_atendimento = c.cd_atendimento)
          where c.tp_situacao = 'R'
            and a.tp_atendimento = 'I'
            --and a.cd_atendimento = 918688
       ) dc
    on (dc.cd_atendimento = vp.cd_atendimento)
   --and (dc.dt_referencia = vp.dt_referencia)
   and (dc.inicio_validade between vp.inicio_validade and vp.fim_validade)

union all

-- Folha anestésica

select distinct dc.cd_unid_int, dc.DS_UNID_INT,
       vp.dt_referencia,
       dc.cd_atendimento,
       dc.cd_aviso_cirurgia cod_item_prontuario,
       'Folha Anestésica' tipo_item_prontuario,
       null,
       null,
       null,
       null dh_criacao,
       null
  from dbamv.vdic_vali_presc_teste_at vp --usrdbvah.tb_validade_prescricao vp
  join (select s.Cd_Setor cd_unid_int,
               s.nm_setor DS_UNID_INT,
               trunc(c.dt_inicio_cirurgia) dt_referencia,
               c.cd_atendimento,
               c.cd_aviso_cirurgia,
               'Folha Anestésica' tipo_item_prontuario,
               decode (trunc(a.dt_atendimento),trunc(c.dt_inicio_cirurgia),
               to_date((to_char(a.dt_atendimento, 'dd/mm/yyyy') || ' ' || to_char(a.hr_atendimento, 'hh24:mi')), 'dd/mm/yyyy hh24:mi'),
               trunc(c.dt_inicio_cirurgia)) inicio_validade,
               trunc(c.dt_inicio_cirurgia) + 1 - 1 / 24 / 60 / 60 fim_validade
          from dbamv.tb_aviso_cirurgia c
          join dbamv.sal_cir sc on (sc.cd_sal_cir = c.cd_sal_cir)
          join dbamv.tb_setor s on (s.cd_setor = sc.cd_setor)
          join dbamv.tb_atendime a on (a.cd_atendimento = c.cd_atendimento)
          where c.tp_situacao = 'R'
            and a.tp_atendimento = 'I'
            and exists (SELECT DISTINCT 'X'
                          FROM DBAMV.PRESTADOR_AVISO PRESTADOR_AVISO,
                               DBAMV.ATI_MED         ATI_MED,
                               DBAMV.PRESTADOR       PRESTADOR,
                               DBAMV.CIRURGIA_AVISO  CIRURGIA_AVISO
                         WHERE (PRESTADOR_AVISO.CD_PRESTADOR = PRESTADOR.CD_PRESTADOR AND
                               PRESTADOR_AVISO.CD_ATI_MED = ATI_MED.CD_ATI_MED AND
                               TP_FUNCAO = 'N' AND
                               CIRURGIA_AVISO.CD_AVISO_CIRURGIA = PRESTADOR_AVISO.CD_AVISO_CIRURGIA AND
                               CIRURGIA_AVISO.CD_CIRURGIA = PRESTADOR_AVISO.CD_CIRURGIA) AND
                               CIRURGIA_AVISO.CD_AVISO_CIRURGIA = c.cd_aviso_cirurgia)
            --and a.cd_atendimento = 918688
       ) dc
    on (dc.cd_atendimento = vp.cd_atendimento)
   --and (dc.dt_referencia = vp.dt_referencia)
   and (dc.inicio_validade between vp.inicio_validade and vp.fim_validade)
 order by cd_atendimento, dt_referencia, dh_criacao
;
 
