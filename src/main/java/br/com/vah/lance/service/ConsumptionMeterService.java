package br.com.vah.lance.service;

import br.com.vah.lance.entity.ConsumptionMeter;

import javax.ejb.Stateless;

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

}
