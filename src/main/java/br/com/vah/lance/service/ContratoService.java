package br.com.vah.lance.service;

import br.com.vah.lance.entity.usrdbvah.Contrato;
import br.com.vah.lance.exception.LanceBusinessException;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.HashSet;
import java.util.List;

@Stateless
public class ContratoService extends DataAccessService<Contrato> {

  public ContratoService() {
    super(Contrato.class);
  }

  public Contrato validateAndCreate(Contrato contrato) throws LanceBusinessException {
    Query query = getEm().createNamedQuery(Contrato.BY_CONTRATANTE_PERIOD);
    query.setParameter("contratante", contrato.getContratante());
    query.setParameter("begin", contrato.getBeginDate());
    query.setParameter("end", contrato.getEndDate());
    List<Contrato> contratos = query.getResultList();
    if (!contratos.isEmpty()) {
      throw new LanceBusinessException("Já existe contrato para o período informado.");
    }
    return create(contrato);
  }

  public Contrato initializeLists(Long id) {
    Contrato contrato = find(id);
    new HashSet<>(contrato.getSetores());
    new HashSet<>(contrato.getServicos());
    return contrato;
  }
}
