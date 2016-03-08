package br.com.vah.lance.service;

import br.com.vah.lance.constant.ServiceTypesEnum;
import br.com.vah.lance.entity.ConsumptionMeter;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author jairoportela
 *
 */
@Stateless
public class ConsumptionMeterService extends DataAccessService<ConsumptionMeter> {

	public ConsumptionMeterService(){
		super(ConsumptionMeter.class);
	}

	public List<ConsumptionMeter> findByType(ServiceTypesEnum type ) {
		Map<String, Object> params = new HashMap<>();
		params.put("type", type);
		return findWithNamedQuery(ConsumptionMeter.BY_TYPE, params);
	}

}
