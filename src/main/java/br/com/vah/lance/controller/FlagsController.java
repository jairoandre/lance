package br.com.vah.lance.controller;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by jairoportela on 22/03/2016.
 */
@SuppressWarnings("serial")
@Named
@ViewScoped
public class FlagsController implements Serializable {

  private Boolean editingCurrency = false;

  public Boolean getEditingCurrency() {
    return editingCurrency;
  }

  public void setEditingCurrency(Boolean editingCurrency) {
    this.editingCurrency = editingCurrency;
  }
}
