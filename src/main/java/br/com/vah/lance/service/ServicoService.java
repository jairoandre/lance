package br.com.vah.lance.service;

import br.com.vah.lance.entity.usrdbvah.Servico;
import br.com.vah.lance.entity.usrdbvah.ServicoValor;
import br.com.vah.lance.util.VahUtils;

import javax.ejb.Stateless;

@Stateless
public class ServicoService extends DataAccessService<Servico> {

  public ServicoService() {
    super(Servico.class);
  }

  public boolean canAddServiceValue(Servico servico, ServicoValor servicoValorToAdd) {
    for (ServicoValor servicoValor : servico.getValues()) {
      if (VahUtils.checkBetween(
          servicoValorToAdd.getBeginDate(),
          servicoValor.getBeginDate(),
          servicoValor.getEndDate())) {
        return false;
      }
    }
    return true;
  }

}
