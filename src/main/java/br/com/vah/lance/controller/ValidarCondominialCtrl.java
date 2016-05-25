package br.com.vah.lance.controller;

import br.com.vah.lance.constant.EntryStatusEnum;
import br.com.vah.lance.entity.Comment;
import br.com.vah.lance.entity.Entry;
import br.com.vah.lance.exception.LanceBusinessException;
import br.com.vah.lance.service.ContaReceberService;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.EntryService;

import javax.annotation.PostConstruct;
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
public class ValidarCondominialCtrl extends AbstractController<Entry> {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  EntryService service;

  private
  @Inject
  ContaReceberService contaReceberService;

  private
  @Inject
  LoginController loginController;

  private List<Entry> lancamentos;

  private List<Entry> lancamentosSelecionados;

  private List<Entry> oldEntries;

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
    dtVencConRec = service.getDataVencimento(getItem(), dtLancamentoConRec);
  }

  public List<Entry> getLancamentos() {
    return lancamentos;
  }

  public void setLancamentos(List<Entry> lancamentos) {
    this.lancamentos = lancamentos;
  }

  @Override
  public DataAccessService<Entry> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public Entry createNewItem() {
    return new Entry();
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
      for (Entry entry : lancamentosSelecionados) {
        totalLancamentos = totalLancamentos.add(entry.getTotalValue());
      }
    }
  }

  public void updateDtVencimento() {
    dtVencConRec = service.getDataVencimento(getItem(), dtLancamentoConRec);
  }

  public void doValidateSave() {
    try {
      service.validarLancamentosAgrupados(lancamentosSelecionados, dtLancamentoConRec);
      lancamentos = service.recuperarLancamentosCondominial();
      lancamentosSelecionados = null;
    } catch (LanceBusinessException lbe) {
      addErrorMsg("Erro de negócio", lbe.getMsg(), false);
    } catch (Exception e) {
      addErrorMsg("Erro inesperado", e.getMessage(), false);
    }
  }

  public void indeferir(Entry lancamento) {
    try {
      lancamento.setStatus(EntryStatusEnum.P);
      service.update(lancamento);
      lancamentos = service.recuperarLancamentosCondominial();
      lancamentosSelecionados = null;
      addInfoMsg("Aviso", "Lançamento indeferido", false);
    } catch (Exception e) {
      addErrorMsg("Erro", e.getMessage(), false);
    }
  }


  public void carregarLancamentosAnteriores(Entry entry) {
    setItem(entry);
    oldEntries = service.listOldEntries(entry.getService());
  }

  public List<Entry> getLancamentosSelecionados() {
    return lancamentosSelecionados;
  }

  public void setLancamentosSelecionados(List<Entry> lancamentosSelecionados) {
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

  public List<Entry> getOldEntries() {
    return oldEntries;
  }

  public String getComentario() {
    return comentario;
  }

  public void setComentario(String comentario) {
    this.comentario = comentario;
  }

  public void salvarNovoComentario() {
    Comment comment = new Comment();
    comment.setAuthor(loginController.getUser());
    comment.setCreatedOn(new Date());
    comment.setEntry(getItem());
    comment.setDetails(comentario);
    getItem().getComments().add(comment);
    comentario = null;
    try {
      service.update(getItem());
      addInfoMsg("Sucesso", "Comentário adicionado", false);
    } catch (Exception e) {
      addErrorMsg("Erro", "Problema na persistência do comentário", false);
    }
  }
}
