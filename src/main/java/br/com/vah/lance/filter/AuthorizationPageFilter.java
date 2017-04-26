package br.com.vah.lance.filter;

import br.com.vah.lance.constant.RestrictViewsEnum;
import br.com.vah.lance.controller.SessionCtrl;
import br.com.vah.lance.entity.usrdbvah.User;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * To prevent user from going back to Login page if the user already logged in
 *
 * @author Emre Simtay <emre@simtay.com>
 */
public class AuthorizationPageFilter implements Filter {

  private
  @Inject
  SessionCtrl sessionCtrl;

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    if (request.getUserPrincipal() != null) {

      User user = sessionCtrl.getUser();

      String[] splitPath = request.getRequestURI().split(request.getContextPath());

      String view = splitPath[0];

      if (splitPath.length > 1) {
        view = splitPath[1];
      }

      /**
       * Verifica se a view requisitada se encontra na enumeração de views restritas
       */
      RestrictViewsEnum restrictView = RestrictViewsEnum.getByView(view);

      if (restrictView == null || restrictView.checkRoles(user.getRoles())) {
        filterChain.doFilter(servletRequest, servletResponse);
      } else {
        response.sendRedirect(request.getContextPath() + "/error-access-denied.xhtml");
      }
    } else {
      // Usuário não logado
      /*
      response.addHeader("requestURI", request.getRequestURI());
      Map<String, String[]> paramMap = request.getParameterMap();
      if (paramMap != null) {
        response.addHeader("paramMapSize", String.valueOf(paramMap.size()));
        int mapIterator = 0;
        for (String key : paramMap.keySet()) {
          response.addHeader(String.format("paramMapKey[%d]", mapIterator), key);
          String[] values = paramMap.get(key);
          response.addHeader(String.format("paramMapValuesSize[%d]", mapIterator), String.valueOf(values == null ? 0 : values.length));
          if (values != null) {
            int valueIterator = 0;
            for (String value : values) {
              response.addHeader(String.format("paramMapValue[%d][%d]", mapIterator, valueIterator), value);
            }
          }
        }
      }
      */
      response.sendRedirect(request.getContextPath() + "/login.xhtml");
    }
  }

  @Override
  public void destroy() {
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }
}