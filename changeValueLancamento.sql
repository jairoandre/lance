select * from TB_LANCA_LANCAMENTO_VALORES where id = 8211;

update tb_lanca_lancamento_valores set VL_VALOR_A = 11300.27, VL_VALOR_B = 11300.27 where id = 8211;

select * from dbamv.con_rec where cd_con_rec = 132868;
select * from dbamv.itcon_rec where cd_con_rec = 132868;
select * from dbamv.rat_conrec where cd_con_rec = 132868;

update dbamv.con_rec set vl_previsto = 11300.27 where cd_con_rec = 134440;
update dbamv.itcon_rec set vl_duplicata = 11300.27, vl_moeda = 11300.27 where cd_con_rec = 134440;
update dbamv.rat_conrec set vl_rateio = 11300.27 where cd_con_rec = 134440;
update tb_lanca_lancamento set vl_total = 64674.96 where id = 346;

select * from TB_LANCA_LANCAMENTO where id = 346;

