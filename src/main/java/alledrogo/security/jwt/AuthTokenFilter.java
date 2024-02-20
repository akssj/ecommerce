package alledrogo.security.jwt;

import alledrogo.security.service.UserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Configuration
public class AuthTokenFilter extends OncePerRequestFilter {
  @Autowired
  private JwtUtils jwtUtils;
  @Autowired
  private UserDetailsService userDetailsService;
  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
    try {
      String token = parseJwt(request);
      if (token != null && jwtUtils.validateJwtToken(token)) {
        String username = jwtUtils.getUserNameFromJwtToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } else {
        logger.error("Invalid or missing JWT token");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing JWT token");
        return;
      }
    } catch (Exception e) {
      logger.error("Cannot set user authentication: ", e);
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot set user authentication: " + e.getMessage());
      return;
    }
    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String path = request.getRequestURI();
    return path.startsWith("/static") ||
            path.startsWith("/images") ||
            path.equals("/main") ||
            path.matches("/category/\\w+") ||
            path.equals("/auth/login") ||
            path.equals("/auth/signup") ||
            path.equals("/product/forSale") ||
            path.equals("/product/categories") ||
            path.matches("/product/\\w+/category");
  }

  private String parseJwt(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if ("token".equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }
    return null;
  }
}
