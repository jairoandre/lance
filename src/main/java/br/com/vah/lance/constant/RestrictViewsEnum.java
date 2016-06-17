package br.com.vah.lance.constant;

import java.util.Set;

import static br.com.vah.lance.constant.RolesEnum.*;

/**
 * Estados do lançamento.
 *
 * @author jairoportela
 */
public enum RestrictViewsEnum {
  USER_LIST("/admin/user/edit.xhtml", ADMINISTRATOR),
  USER_EDIT("/admin/user/list.xhtml", ADMINISTRATOR, ACCOUNTANT),
  CLIENT_EDIT("/pages/fornecedor/edit.xhtml", ADMINISTRATOR, ACCOUNTANT, SUPERVISOR),
  SERVICO_EDIT("/pages/servico/edit.xhtml", ADMINISTRATOR, ACCOUNTANT, SUPERVISOR),
  SERVICO_CURRENCY("/pages/servico/currency.xhtml", ADMINISTRATOR, MANAGER),
  CONTRACT_EDIT("/pages/contrato/edit.xhtml", ADMINISTRATOR, ACCOUNTANT),
  LANCAMENTO_EDIT("/pages/lancamento/edit.xhtml", ADMINISTRATOR, MANAGER, REGISTER),
  LANCAMENTO_VALIDATE("/pages/lancamento/validate.xhtml", ADMINISTRATOR, SUPERVISOR);

  private String view;

  private RolesEnum[] roles;

  RestrictViewsEnum(String view, RolesEnum... roles) {
    this.view = view;
    this.roles = roles;
  }

  public String getView() {
    return view;
  }

  public static RestrictViewsEnum getByView(String view) {
    for (RestrictViewsEnum viewEnum : RestrictViewsEnum.values()) {
      if (viewEnum.getView().equals(view)) {
        return viewEnum;
      }
    }
    return null;
  }

  public boolean checkRoles(Set<RolesEnum> roles) {
    boolean atLeastOne = false;
    for (RolesEnum thisRole : this.roles) {
      for (RolesEnum theirRole : roles) {
        if (theirRole.equals(thisRole)) {
          atLeastOne = true;
          break;
        }
      }
      if (atLeastOne) {
        break;
      }
    }
    return atLeastOne;
  }

}
