package br.com.vah.lance.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.vah.lance.entity.mv.MvClient;
import br.com.vah.lance.entity.mv.MvSector;

/**
 * 
 * @author jairoportela
 *
 */
@Stateless
public class ClientService extends DataAccessService<MvClient> {

	private @Inject SectorService sectorService;

	public ClientService() {
		super(MvClient.class);
	}

	public List<String> verifySectors(MvClient item) {
		List<String> errors = new ArrayList<String>();
		for (MvSector sector : item.getSectors()) {
			MvSector attachedSector = sectorService.find(sector.getId());
			Set<MvClient> relatedClients = attachedSector.getClients();
			if (relatedClients.size() > 0) {
				MvClient client = (MvClient) relatedClients.toArray()[0];
				errors.add(String.format("Setor [%s] já relacionado com o cliente [%s].", sector.getTitle(), client.getTitle()));
			}
		}
		return errors;
	}

}
