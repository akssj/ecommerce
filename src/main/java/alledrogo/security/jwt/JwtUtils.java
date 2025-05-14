package alledrogo.security.jwt;

import alledrogo.security.service.UserDetailsImpl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
  @Value("${alledrogo.app.jwtExpirationMs}")
  private int jwtExpirationMs;
  @Value("${alledrogo.app.jwt.secret-key-file}")
  private String secretKeyFilePath;
  @Value("${alledrogo.app.jwt.public-key-file}")
  private String publicKeyFilePath;

  private PrivateKey readPrivateKey(String filePath) throws IOException, GeneralSecurityException {
    byte[] keyBytes = readKeyFile(filePath);
    PKCS8EncodedKeySpec  spec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    return kf.generatePrivate(spec);
  }

  private PublicKey readPublicKey(String filePath) throws IOException, GeneralSecurityException {
    byte[] keyBytes = readKeyFile(filePath);
    X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    return kf.generatePublic(spec);
  }

  private byte[] readKeyFile(String filePath) throws IOException {
    ClassPathResource resource = new ClassPathResource(filePath);
    return FileCopyUtils.copyToByteArray(resource.getInputStream());
  }

  public String generateJwtToken(Authentication authentication) {
    try {
      UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

      return Jwts.builder()
              .setSubject((userPrincipal.getUsername()))
              .claim("email", userPrincipal.getEmail())
              .setIssuedAt(new Date())
              .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
              .signWith(readPrivateKey(secretKeyFilePath), SignatureAlgorithm.RS256)
              .compact();

    }catch (IOException | GeneralSecurityException e){
      logger.error("Exception: {}", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  public String getUserNameFromJwtToken(String token) {
    try {
      return Jwts.parserBuilder().setSigningKey(readPublicKey(publicKeyFilePath)).build()
              .parseClaimsJws(token).getBody().getSubject();
    }catch (JwtException | IOException | GeneralSecurityException e){
      logger.error("Exception: {}", e.getMessage());
      return null;
    }
  }
  public String getEmailFromJwtToken(String token) {
    try{
      return Jwts.parserBuilder().setSigningKey(readPublicKey(publicKeyFilePath)).build()
              .parseClaimsJws(token).getBody().get("email", String.class);
    }catch (JwtException | IOException | GeneralSecurityException e){
      logger.error("Exception: {}", e.getMessage());
      return null;
    }
  }

  public boolean validateJwtToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(readPublicKey(publicKeyFilePath)).build().parseClaimsJws(token);
      return true;
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    } catch (SignatureException e) {
      logger.error("JWT signature validation failed: {}", e.getMessage());
    } catch (GeneralSecurityException | IOException e) {
      logger.error("JWT error: {}", e.getMessage());
    }
    return false;
  }
}
