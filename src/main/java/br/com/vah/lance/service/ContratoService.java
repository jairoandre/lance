package br.com.vah.lance.service;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.vah.lance.entity.usrdbvah.Contrato;
import br.com.vah.lance.exception.LanceBusinessException;
import org.hibernate.Session;

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
}
