package br.com.vah.lance.controller;

import br.com.vah.lance.constant.RolesEnum;
import br.com.vah.lance.entity.usrdbvah.User;
import br.com.vah.lance.service.UserService;
import br.com.vah.lance.util.DateUtility;
import br.com.vah.lance.util.ViewUtils;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Login Controller class allows only authenticated users to log in to the web
 * application.
 *
 * @author Emre Simtay <emre@simtay.com>
 */
@SuppressWarnings("serial")
@Named
@SessionScoped
public class SessionCtrl implements Serializable {

  private
  transient Logger logger = Logger.getLogger("SessionCtrl");

  private
  @Inject
  UserService userService;

  private String username;
  private String password;
  private User user;

  /**
   * Creates a new instance of SessionCtrl
   */
  public SessionCtrl() {
  }

  // Getters and Setters

  /**
   * @return username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @return the user
   */
  public User getUser() {
    return user;
  }

  /**
   * @param username
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @return password
   */
  public String getPassword() {
    return password;
  }

  /**
   * @param password
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Listen for button clicks on the #{loginController.login} action,
   * validates the username and password entered by the user and navigates to
   * the appropriate page.
   *
   * @param actionEvent
   */
  public void login(ActionEvent actionEvent) {

    FacesContext context = FacesContext.getCurrentInstance();
    HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
    try {
      String navigateString = "/pages/index.xhtml";
      /**
       * Realiza autenticação
       */
      this.username = this.username.toLowerCase();

      request.login(username, password);

      /**
       * Verifica se o usuário já está configurado no sistema
       */
      user = userService.findByLogin(username);

      /**
       * Primeiro acesso
       */
      if (user == null) {
        user = new User();
        user.setLogin(username);
        user.getRoles().add(RolesEnum.USER);
        userService.create(user);
        ViewUtils.addMsg(new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Solicite configuração de acesso ao administrador do sistema"), true);
      }

      // gets the user principle and navigates to the appropriate page

      Principal principal = request.getUserPrincipal();

      try {
        logger.log(Level.INFO, "User ({0}) loging in #" + DateUtility.getCurrentDateTime(),
            request.getUserPrincipal().getName());
        context.getExternalContext().redirect(request.getContextPath() + navigateString);
      } catch (IOException ex) {
        logger.log(Level.SEVERE, "IOException, Login Controller" + "Username : " + principal.getName(), ex);
        context.addMessage(null, new FacesMessage("Error!", "Exception occured"));
      }
    } catch (ServletException e) {
      e.printStackTrace();
      logger.log(Level.SEVERE, e.toString());
      context.addMessage(null, new FacesMessage("Erro!", "Usuário ou senha inválida."));
    }
  }

  /**
   * Listen for logout button clicks on the #{loginController.logout} action
   * and navigates to login screen.
   */
  public void logout() {

    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
        .getRequest();
    logger.log(Level.INFO, "User ({0}) loging out #" + DateUtility.getCurrentDateTime(),
        request.getUserPrincipal().getName());
    if (session != null) {
      session.invalidate();
    }
    FacesContext.getCurrentInstance().getApplication().getNavigationHandler()
        .handleNavigation(FacesContext.getCurrentInstance(), null, "/login.xhtml?faces-redirect=true");
  }

  public boolean isUserInRoles(String roles) {
    boolean atLeastOneRole = false;
    for (String role : roles.split(",")) {
      if (user.getRoles().contains(RolesEnum.valueOf(role))) {
        atLeastOneRole = true;
        break;
      }
    }
    return atLeastOneRole;
  }
}
