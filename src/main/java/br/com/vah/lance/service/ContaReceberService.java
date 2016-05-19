package br.com.vah.lance.service;

import br.com.vah.lance.entity.mv.MvContaReceber;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author jairoportela
 *
 */
@Stateless
public class ContaReceberService extends DataAccessService<MvContaReceber> {

	public ContaReceberService() {
		super(MvContaReceber.class);
	}

	public List<MvContaReceber> createList(List<MvContaReceber> list) {
		List<MvContaReceber> persistedList = new ArrayList<>();
		for(MvContaReceber conRec : list) {
			persistedList.add(create(conRec));
		}
		return persistedList;
	}


}
