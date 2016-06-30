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

  private Boolean editingField = false;

  public Boolean getEditingField() {
    return editingField;
  }

  public void settingTrue() {
    this.editingField = true;
  }

  public void settingFalse() {
    this.editingField = false;
  }

  public void setEditingField(Boolean editingField) {
    this.editingField = editingField;
  }
}