package project.backend.user.infra.security.jwt.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import project.backend.user.infra.security.oauth.KakaoUserDetails;

@Slf4j
@Component
public class TokenProvider {

  private static final String AUTH_ID = "ID";
  private static final String AUTH_KEY = "AUTHORITY";
  private static final String AUTH_EMAIL = "EMAIL";
  private Key key;
  private final String secretKey;
  private final long accessExpirations;
  private final long refreshExpirations;

  public TokenProvider(
      @Value("${jwt.secret_key}") String secretKey,
      @Value("${jwt.access_expirations}") long accessExpirations,
      @Value("${jwt.refresh_expirations}") long refreshExpirations
  ) {
    this.secretKey = secretKey;
    this.accessExpirations = accessExpirations * 1000;
    this.refreshExpirations = refreshExpirations * 1000;
  }

  @PostConstruct
  public void initializeKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.key = new SecretKeySpec(keyBytes, "HmacSHA256");
  }

  public TokenResponse createToken(String userId, String email, String role) {
    long now = (new Date()).getTime();

    Date accessValidity = new Date(now + this.accessExpirations);
    Date refreshValidity = new Date(now + this.refreshExpirations);

    String accessToken = Jwts.builder()
                             .addClaims(Map.of(AUTH_ID, userId))
                             .addClaims(Map.of(AUTH_EMAIL, email))
                             .addClaims(Map.of(AUTH_KEY, role))
                             .signWith(key, SignatureAlgorithm.HS256)
                             .setExpiration(accessValidity)
                             .compact();

    String refreshToken = Jwts.builder()
                              .addClaims(Map.of(AUTH_ID, userId))
                              .addClaims(Map.of(AUTH_EMAIL, email))
                              .addClaims(Map.of(AUTH_KEY, role))
                              .signWith(key, SignatureAlgorithm.HS256)
                              .setExpiration(refreshValidity)
                              .compact();

    return TokenResponse.of(accessToken, refreshToken);
  }

  public Authentication getAuthentication(String token) {
    Claims claims = Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

    List<String> authorities = Arrays.asList(claims.get(AUTH_KEY)
                                                   .toString()
                                                   .split(","));

    List<? extends GrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                                                                           .map(SimpleGrantedAuthority::new)
                                                                           .toList();

    KakaoUserDetails principal = new KakaoUserDetails(
        Long.parseLong((String) claims.get(AUTH_ID)),
        (String) claims.get(AUTH_EMAIL),
        simpleGrantedAuthorities,
        Map.of()
    );

    return new UsernamePasswordAuthenticationToken(principal, token, simpleGrantedAuthorities);
  }

  public boolean validate(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (SecurityException | MalformedJwtException e) {
      return false;
    } catch (UnsupportedJwtException e) {
      return false;
    } catch (IllegalArgumentException e) {
      return false;
    } catch (ExpiredJwtException e) {
      return true;
    }
  }

  public boolean validateExpired(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (ExpiredJwtException e) {
      return false;
    }
  }
}
