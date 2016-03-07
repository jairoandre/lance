package br.com.vah.lance.controller;

import br.com.vah.lance.entity.ConsumptionMeter;
import br.com.vah.lance.entity.SectorConsumptionMeter;
import br.com.vah.lance.entity.mv.MvSector;
import br.com.vah.lance.service.ConsumptionMeterService;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.SectorService;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ConsumptionMeterController extends AbstractController<ConsumptionMeter> {

	private @Inject transient Logger logger;

	private @Inject
	ConsumptionMeterService service;

	private MvSector sector;

	private ConsumptionMeterController itemToRemove;

	private Set<MvSector> sectors = new LinkedHashSet<>();

  public static final String[] RELATIONS = {"sectors"};

	@PostConstruct
	public void init() {
		logger.info(this.getClass().getSimpleName() + " created.");
		setItem(createNewItem());
		initLazyModel(service, RELATIONS);
	}

	@Override
	public DataAccessService<ConsumptionMeter> getService() {
		return service;
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public ConsumptionMeter createNewItem() {
		return new ConsumptionMeter();
	}

	@Override
	public String path() {
		return "consumptionMeter";
	}

	@Override
	public String getEntityName() {
		return "medidor";
	}

	public void setService(ConsumptionMeterService service) {
		this.service = service;
	}

	public MvSector getSector() {
		return sector;
	}

	public void setSector(MvSector sector) {
		this.sector = sector;
	}

	public Set<MvSector> getSectors() {
		return sectors;
	}

	public void setSectors(Set<MvSector> sectors) {
		this.sectors = sectors;
	}

	public ConsumptionMeterController getItemToRemove() {
		return itemToRemove;
	}

	public void setItemToRemove(ConsumptionMeterController itemToRemove) {
		this.itemToRemove = itemToRemove;
	}

	@Override
	public String search() {
		getLazyModel().getSearchParams().getParams().put("id", null);
		try {
			Long convertedValue = Long.valueOf(getSearchTerm());
			getLazyModel().getSearchParams().getParams().put("id", convertedValue);
		} catch (Exception e) {
			/**
			 * Cannot convert to integer
			 */
		}
		return super.search();
	}

	public void toggle() {
		if (sectors.contains(sector)) {
			sectors.remove(sector);
			SectorConsumptionMeter toRemove = null;
			for (SectorConsumptionMeter sectorConsumptionMeter : getItem ().getSectors()) {
				if(sectorConsumptionMeter.getSector().equals(sector)){
					toRemove = sectorConsumptionMeter;
					break;
				}
			}
			getItem().getSectors().remove(toRemove);
		} else {
			sectors.add(sector);
			SectorConsumptionMeter toAdd = new SectorConsumptionMeter();
			toAdd.setConsumptionMeter(getItem());
			toAdd.setSector(sector);
			getItem().getSectors().add(toAdd);
		}
	}

	public void removeSector () {
		getItem().getSectors().remove(itemToRemove);
		sectors.remove(itemToRemove.getSector());
	}

}
