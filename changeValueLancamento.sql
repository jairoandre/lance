select * from TB_LANCA_LANCAMENTO_VALORES where id = ?;

update tb_lanca_lancamento_valores set VL_VALOR_A = ?, VL_VALOR_B = ? where id = ?;

select * from dbamv.con_rec where cd_con_rec = ?;
select * from dbamv.itcon_rec where cd_con_rec = ?;
select * from dbamv.rat_conrec where cd_con_rec = ?;

update dbamv.con_rec set vl_previsto = ? where cd_con_rec = ?;
update dbamv.itcon_rec set vl_soma_recebido = ?, tp_situacao = 'Q' where cd_con_rec = ?;

update dbamv.rat_conrec set vl_rateio = ? where cd_con_rec = ?;

update usrdbvah.tb_lanca_lancamento set vl_total = ? where id = ?;

select * from TB_LANCA_LANCAMENTO where id = ?;


update dbamv.itcon_rec set vl_soma_recebido = ?, tp_situacao = 'Q' where cd_con_rec = ?;

