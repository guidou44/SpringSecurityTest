package com.ken3d.threedfy.domain.user.security;

import com.ken3d.threedfy.domain.user.Authority;
import com.ken3d.threedfy.domain.user.UserAuthDetails;
import com.ken3d.threedfy.domain.user.exceptions.AuthorityWithoutLevelException;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private final Log logger = LogFactory.getLog(this.getClass());
  private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

  @Override
  public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, Authentication authentication)
      throws IOException {
    setSessionAuthAttribute(httpServletRequest, httpServletResponse);
    handle(httpServletRequest, httpServletResponse, authentication);
  }

  private void setSessionAuthAttribute(HttpServletRequest request, HttpServletResponse response) {
    HttpSession session = request.getSession();
    UserAuthDetails authUser = (UserAuthDetails) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();
    session.setAttribute("username", authUser.getUsername());
    session.setAttribute("authorities", authUser.getAuthorities());
    response.setStatus(HttpServletResponse.SC_OK);
  }


  private void handle(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication
  ) throws IOException {

    String targetUrl = determineTargetUrl(authentication);

    if (response.isCommitted()) {
      logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
      return;
    }

    redirectStrategy.sendRedirect(request, response, targetUrl);
  }

  private String determineTargetUrl(final Authentication authentication) {

    Map<Integer, String> authorityTargetUrl = new HashMap<>();
    authorityTargetUrl.put(0, "/dashboard");

    final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Authority highestAuthority = authorities.stream().map(Authority.class::cast)
        .max(Comparator.comparing(Authority::getAuthorityLevel)).orElseThrow(
            AuthorityWithoutLevelException::new);
    int nearestLevel = authorityTargetUrl
        .keySet().stream().filter(k -> k <= highestAuthority.getAuthorityLevel())
        .max(Comparator.comparing(Integer::valueOf))
        .orElseThrow(AuthorityWithoutLevelException::new);

    return authorityTargetUrl.get(nearestLevel);
  }
}
