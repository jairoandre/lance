package br.com.vah.lance.controller;

import br.com.vah.lance.constant.EstadoLancamentoEnum;
import br.com.vah.lance.entity.usrdbvah.Comentario;
import br.com.vah.lance.entity.usrdbvah.Lancamento;
import br.com.vah.lance.exception.LanceBusinessException;
import br.com.vah.lance.service.ContaReceberService;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.LancamentoService;
import br.com.vah.lance.util.VahUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ValidarCondominialCtrl extends AbstractController<Lancamento> {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  LancamentoService service;

  private
  @Inject
  ContaReceberService contaReceberService;

  private
  @Inject
  SessionCtrl sessionCtrl;

  private List<Lancamento> lancamentos;

  private List<Lancamento> lancamentosSelecionados;

  private List<Lancamento> oldEntries;

  private BigDecimal totalLancamentos = BigDecimal.ZERO;

  private Date dtLancamentoConRec;

  private Date dtVencConRec;

  private String comentario;

  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created.");
    setItem(createNewItem());
    lancamentos = service.recuperarLancamentosCondominial();
    dtLancamentoConRec = new Date();
    dtVencConRec = VahUtils.calcNextMonthDate(dtLancamentoConRec, 8);
  }

  public List<Lancamento> getLancamentos() {
    return lancamentos;
  }

  public void setLancamentos(List<Lancamento> lancamentos) {
    this.lancamentos = lancamentos;
  }

  @Override
  public DataAccessService<Lancamento> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public Lancamento createNewItem() {
    return new Lancamento();
  }

  @Override
  public String path() {
    return "validarCondominial";
  }

  @Override
  public String getEntityName() {
    return "lançamento";
  }

  public void onSelect() {
    totalLancamentos = BigDecimal.ZERO;
    if (lancamentosSelecionados != null) {
      for (Lancamento lancamento : lancamentosSelecionados) {
        totalLancamentos = totalLancamentos.add(lancamento.getTotalValue());
      }
    }
  }

  public void updateDtVencimento() {
    dtVencConRec = VahUtils.calcNextMonthDate(dtLancamentoConRec, 8);
  }

  public void doValidateSave() {
    try {
      service.validarLancamentosAgrupados(lancamentosSelecionados, dtLancamentoConRec);
      lancamentos = service.recuperarLancamentosCondominial();
      lancamentosSelecionados = null;
    } catch (LanceBusinessException lbe) {
      addMsg(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro de negócio", lbe.getMsg()), false);
    } catch (Exception e) {
      addMsg(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro inesperado", e.getMessage()), false);
    }
  }

  public void indeferir(Lancamento lancamento) {
    try {
      lancamento.setStatus(EstadoLancamentoEnum.P);
      service.update(lancamento);
      lancamentos = service.recuperarLancamentosCondominial();
      lancamentosSelecionados = null;
      addMsg(new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Lançamento indeferido"), false);
    } catch (Exception e) {
      addMsg(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage()), false);
    }
  }


  public void carregarLancamentosAnteriores(Lancamento lancamento) {
    setItem(lancamento);
    oldEntries = service.listOldEntries(lancamento.getServico());
  }

  public List<Lancamento> getLancamentosSelecionados() {
    return lancamentosSelecionados;
  }

  public void setLancamentosSelecionados(List<Lancamento> lancamentosSelecionados) {
    this.lancamentosSelecionados = lancamentosSelecionados;
  }

  public BigDecimal getTotalLancamentos() {
    return totalLancamentos;
  }

  public Date getDtVencConRec() {
    return dtVencConRec;
  }

  public void setDtVencConRec(Date dtVencConRec) {
    this.dtVencConRec = dtVencConRec;
  }

  public Date getDtLancamentoConRec() {
    return dtLancamentoConRec;
  }

  public void setDtLancamentoConRec(Date dtLancamentoConRec) {
    this.dtLancamentoConRec = dtLancamentoConRec;
  }

  public List<Lancamento> getOldEntries() {
    return oldEntries;
  }

  public String getComentario() {
    return comentario;
  }

  public void setComentario(String comentario) {
    this.comentario = comentario;
  }

  public void salvarNovoComentario() {
    Comentario comentario = new Comentario();
    comentario.setAuthor(sessionCtrl.getUser());
    comentario.setCreatedOn(new Date());
    comentario.setLancamento(getItem());
    comentario.setDetails(this.comentario);
    getItem().getComentarios().add(comentario);
    this.comentario = null;
    try {
      service.update(getItem());
      addMsg(new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Comentário adicionado"), false);
    } catch (Exception e) {
      addMsg(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Problema na persistência do comentário"), false);
    }
  }
}
