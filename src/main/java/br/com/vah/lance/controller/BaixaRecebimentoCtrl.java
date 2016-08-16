package br.com.vah.lance.controller;

import br.com.vah.lance.entity.dbamv.RecebimentoDescAcres;
import br.com.vah.lance.entity.usrdbvah.Cobranca;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jairoportela on 09/08/2016.
 */
@SuppressWarnings("serial")
@Named
@ViewScoped
public class BaixaRecebimentoCtrl implements Serializable {

  private Date dataBaixa;
  private BigDecimal valorBaixa;
  private List<RecebimentoDescAcres> descontosAcrescimos = new ArrayList<>();
  private Cobranca cobranca;

  public Date getDataBaixa() {
    return dataBaixa;
  }

  public void setDataBaixa(Date dataBaixa) {
    this.dataBaixa = dataBaixa;
  }

  public BigDecimal getValorBaixa() {
    return valorBaixa;
  }

  public void setValorBaixa(BigDecimal valorBaixa) {
    this.valorBaixa = valorBaixa;
  }

  public List<RecebimentoDescAcres> getDescontosAcrescimos() {
    return descontosAcrescimos;
  }

  public void setDescontosAcrescimos(List<RecebimentoDescAcres> descontosAcrescimos) {
    this.descontosAcrescimos = descontosAcrescimos;
  }

  public Cobranca getCobranca() {
    return cobranca;
  }

  public void setCobranca(Cobranca cobranca) {
    this.cobranca = cobranca;
  }

  public void preBaixa(Cobranca cobranca) {
    this.cobranca = cobranca;
    this.valorBaixa = cobranca.getValor();
  }
}
