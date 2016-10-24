select cd_con_rec, cd_itcon_rec, vl_duplicata, tp_quitacao, vl_soma_recebido from dbamv.itcon_rec where cd_con_rec in (
  select cd_con_rec from usrdbvah.TB_LANCA_COBRANCA c left join usrdbvah.TB_LANCA_COBRANCA_CONTA ic on c.id = ic.id_cobranca where c.id = 361
);

select * from dbamv.RECCON_REC where cd_itcon_rec in (
  select cd_itcon_rec from dbamv.itcon_rec where cd_con_rec in (
    select cd_con_rec from usrdbvah.TB_LANCA_COBRANCA c left join usrdbvah.TB_LANCA_COBRANCA_CONTA ic on c.id = ic.id_cobranca where c.id = 288
  )
);

update dbamv.itcon_rec set vl_soma_recebido = 485.54, tp_quitacao = 'Q' where cd_con_rec = 133139;