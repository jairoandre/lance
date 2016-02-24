package br.com.vah.lance.service;

import br.com.vah.lance.entity.Service;
import br.com.vah.lance.entity.ServiceValue;
import br.com.vah.lance.util.LanceUtils;

import javax.ejb.Stateless;

@Stateless
public class ServiceService extends DataAccessService<Service> {

  public ServiceService() {
    super(Service.class);
  }

  public boolean canAddServiceValue(Service service, ServiceValue serviceValueToAdd) {
    for (ServiceValue serviceValue : service.getValues()) {
      if (LanceUtils.checkBetween(
          serviceValueToAdd.getBeginDate(),
          serviceValue.getBeginDate(),
          serviceValue.getEndDate())) {
        return false;
      }
    }
    return true;
  }

}
