package br.com.vah.lance.service;

import br.com.vah.lance.entity.dbamv.Movimentacao;
import br.com.vah.lance.entity.dbamv.Recebimento;

import javax.ejb.Stateless;

/**
 * Created by Jairoportela on 08/08/2016.
 */
@Stateless
public class MovimentacaoService extends DataAccessService<Movimentacao> {

  public MovimentacaoService() {
    super(Movimentacao.class);
  }

}
