package br.com.vah.lance.controller;

import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.vah.lance.entity.User;
import br.com.vah.lance.service.UserService;
import br.com.vah.lance.util.DateUtility;

/**
 * Login Controller class allows only authenticated users to log in to the web
 * application.
 *
 * @author Emre Simtay <emre@simtay.com>
 */
@SuppressWarnings("serial")
@Named
@SessionScoped
public class LoginController implements Serializable {

	private @Inject transient Logger logger;

	private @Inject UserService userService;

	private String username;
	private String password;
	private User user;

	/**
	 * Creates a new instance of LoginController
	 */
	public LoginController() {
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
	 *
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 *
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 *
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
			// Checks if username and password are valid if not throws a
			// ServletException
			request.login(username, password);
			// gets the user principle and navigates to the appropriate page
			this.user = userService.findByLogin(username);
			Principal principal = request.getUserPrincipal();
			if (!request.isUserInRole("administrator")) {
				navigateString = "/admin/home.xhtml";
			}
			try {
				logger.log(Level.INFO, "User ({0}) loging in #" + DateUtility.getCurrentDateTime(),
						request.getUserPrincipal().getName());
				context.getExternalContext().redirect(request.getContextPath() + navigateString);
			} catch (IOException ex) {
				logger.log(Level.SEVERE, "IOException, Login Controller" + "Username : " + principal.getName(), ex);
				context.addMessage(null, new FacesMessage("Error!", "Exception occured"));
			}
		} catch (ServletException e) {
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
}
