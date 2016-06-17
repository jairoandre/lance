package br.com.vah.lance.service;

import br.com.vah.lance.constant.TipoServicoEnum;
import br.com.vah.lance.entity.usrdbvah.MedidorConsumo;

import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author jairoportela
 *
 */
@Stateless
public class MedidorConsumoService extends DataAccessService<MedidorConsumo> {

	public MedidorConsumoService(){
		super(MedidorConsumo.class);
	}

	public List<MedidorConsumo> findByType(TipoServicoEnum type ) {
		Map<String, Object> params = new HashMap<>();
		params.put("type", type);
		return findWithNamedQuery(MedidorConsumo.BY_TYPE, params);
	}

}
