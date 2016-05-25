package br.com.vah.lance.exception;

/**
 * Created by jairoportela on 25/05/2016.
 */
public class LanceBusinessException extends Exception {

  private String msg;

  public LanceBusinessException(String msg, Object... args) {
    super();
    this.msg = String.format(msg, args);
  }

  public String getMsg() {
    return this.msg;
  }
}
