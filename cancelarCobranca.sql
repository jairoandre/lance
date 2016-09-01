select * from dbamv.con_rec where cd_con_rec in (select c.cd_con_rec from USRDBVAH.TB_LANCA_COBRANCA_CONTA c where c.ID_COBRANCA = ?);

delete from usrdbvah.TB_LANCA_COBRANCA_CONTA where CD_CON_REC = ?;
delete from usrdbvah.TB_LANCA_ITEM_COBRANCA where ID_COBRANCA = ?;
delete from usrdbvah.TB_LANCA_COBRANCA where id = ?;

delete from dbamv.itcon_rec where cd_con_rec = ?;
delete from dbamv.rat_conrec where cd_con_rec = ?;
delete from dbamv.con_rec where cd_con_rec = ?;