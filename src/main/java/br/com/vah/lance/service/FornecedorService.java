package br.com.vah.lance.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.vah.lance.constant.TipoSetorEnum;
import br.com.vah.lance.entity.dbamv.Fornecedor;
import br.com.vah.lance.entity.dbamv.Setor;
import br.com.vah.lance.entity.usrdbvah.SetorDetalhe;

/**
 * 
 * @author jairoportela
 *
 */
@Stateless
public class FornecedorService extends DataAccessService<Fornecedor> {

	private @Inject
	SetorService setorService;

	public FornecedorService() {
		super(Fornecedor.class);
	}

	public List<String> verifySetores(Fornecedor item) {
		List<String> errors = new ArrayList<>();
		for (Setor setor : item.getSetores()) {
			Setor attachedSetor = setorService.find(setor.getId());
			Set<Fornecedor> relatedClients = attachedSetor.getClients();
			if (relatedClients.size() > 0) {
				SetorDetalhe setorDetalhe = attachedSetor.getSetorDetalhe();
				if(setorDetalhe != null && TipoSetorEnum.VAH.equals(setorDetalhe.getType())) {
					continue;
				}
				for (Fornecedor iter : relatedClients){
					if(!iter.getId().equals(item.getId())){
						Fornecedor client = (Fornecedor) relatedClients.toArray()[0];
						errors.add(String.format("Setor [%s] já relacionado com o cliente [%s]. Para o setor ser selecionado por múltiplos clientes configure o mesmo com o tipo VAH.", setor.getTitle(), client.getTitle()));
					}
				}
			}
		}
		return errors;
	}

}
