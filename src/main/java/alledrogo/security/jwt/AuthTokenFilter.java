package alledrogo.security.jwt;

import alledrogo.security.service.UserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
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
  protected void doFilterInternal(@NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {
    String token = parseJwt(request);
    if (token == null || !jwtUtils.validateJwtToken(token)) {
      logger.error("Invalid or missing JWT token");

      String acceptHeader = request.getHeader("Accept");
      if (acceptHeader != null && acceptHeader.contains("text/html")) {
        logger.info("Unauthorized request for HTML content");
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpServletResponse.SC_UNAUTHORIZED);
        request.getRequestDispatcher("/error").forward(request, response);
      } else {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Unauthorized: You need to log in to perform this action.");
      }
      return;
    }
    try {
      String username = jwtUtils.getUserNameFromJwtToken(token);
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
          userDetails.getAuthorities());
      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authentication);
      filterChain.doFilter(request, response);
    } catch (Exception e) {
      logger.error("Cannot set user authentication: ", e);
      request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      request.getRequestDispatcher("/error").forward(request, response);
    }
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
      String path = request.getRequestURI();
  
      return path.equals("/") ||
             path.equals("/index.html") ||
             path.equals("/search.html") ||
             path.startsWith("/css/") ||
             path.startsWith("/js/") ||
             path.startsWith("/component/") ||
             path.startsWith("/static/") ||             // jeśli używasz /static/**
             path.equals("/favicon.ico") ||
             path.startsWith("/error") ||
              path.equals("/auth/refresh-token") ||
             path.equals("/auth/login") ||
             path.equals("/auth/signup") ||
             path.equals("/product/forSale") ||
             path.equals("/product/categories") ||
             path.matches("^/product/[^/]+/category$") || // dokładne dopasowanie
              path.matches("^/product/[^/]+/name$") || // dokładne dopasowanie
             path.matches("^/category/[^/]+$");             // np. /category/Elektronika
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
