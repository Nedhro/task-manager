package com.taskmanager.app.security;

import com.taskmanager.app.core.model.AuthUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class TokenProvider implements Serializable {

  static final String CLAIM_KEY_USERNAME = "sub";
  static final String CLAIM_KEY_ROLE = "permission";
  static final String CLAIM_KEY_AUDIENCE = "audience";
  static final String CLAIM_KEY_CREATED = "created";
  static final String CLAIM_KEY_EXPIRED = "exp";
  static final String AUDIENCE_UNKNOWN = "unknown";
  static final String AUDIENCE_WEB = "web";
  static final String AUDIENCE_MOBILE = "mobile";
  static final String AUDIENCE_TABLET = "tablet";
  private static final long serialVersionUID = -3301605591108950415L;

  @Value("${jwt.token.secret}")
  private String secret;

  @Value("${jwt.expire.sec}")
  private Long accessTokenExpiration;

  @Value("${jwt.refreshTokenExpire.sec}")
  private Long refreshTokenExpiration;

  public String getUsernameFromToken(String token) {
    String username;
    try {
      final Claims claims = getClaimsFromToken(token);
      username = claims.getSubject();
    } catch (Exception e) {
      username = null;
    }
    return username;
  }

  public Date getCreatedDateFromToken(String token) {
    Date created;
    try {
      final Claims claims = getClaimsFromToken(token);
      created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
    } catch (Exception e) {
      created = null;
    }
    return created;
  }

  public Date getExpirationDateFromToken(String token) {
    Date expiration;
    try {
      final Claims claims = getClaimsFromToken(token);
      expiration = claims.getExpiration();
    } catch (Exception e) {
      expiration = null;
    }
    return expiration;
  }

  public String getAudienceFromToken(String token) {
    String audience;
    try {
      final Claims claims = getClaimsFromToken(token);
      audience = (String) claims.get(CLAIM_KEY_AUDIENCE);
    } catch (Exception e) {
      audience = null;
    }
    return audience;
  }

  private Claims getClaimsFromToken(String token) {
    Claims claims;
    try {
      claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    } catch (Exception e) {
      claims = null;
    }
    return claims;
  }

  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
    return (lastPasswordReset != null && created.before(lastPasswordReset));
  }

  /*
   * private String generateAudience(Device device) { String audience =
   * AUDIENCE_UNKNOWN; if (device.isNormal()) { audience = AUDIENCE_WEB; } else if
   * (device.isTablet()) { audience = AUDIENCE_TABLET; } else if
   * (device.isMobile()) { audience = AUDIENCE_MOBILE; } return audience; }
   */

  private Boolean ignoreTokenExpiration(String token) {
    String audience = getAudienceFromToken(token);
    return (AUDIENCE_TABLET.equals(audience) || AUDIENCE_MOBILE.equals(audience));
  }

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
    claims.put(CLAIM_KEY_ROLE, getUniqueAuthorities(userDetails));
    //  claims.put(CLAIM_KEY_AUDIENCE, generateAudience(device));

    final Date createdDate = new Date();
    claims.put(CLAIM_KEY_CREATED, createdDate);

    return doGenerateToken(claims, accessTokenExpiration);
  }

  public String generateRefreshToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
    // claims.put(CLAIM_KEY_AUDIENCE, generateAudience(device));

    final Date createdDate = new Date();
    claims.put(CLAIM_KEY_CREATED, createdDate);

    return doGenerateToken(claims, refreshTokenExpiration);
  }

  private String doGenerateToken(Map<String, Object> claims, long expiration) {
    final Date createdDate = (Date) claims.get(CLAIM_KEY_CREATED);
    final Date expirationDate = new Date(createdDate.getTime() + expiration * 1000);

    System.out.println("doGenerateToken " + createdDate);
    System.out.println("doGenerateToken " + expirationDate);

    return Jwts.builder()
        .setClaims(claims)
        .setExpiration(expirationDate)
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
  }

  public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
    final Date created = getCreatedDateFromToken(token);
    return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
        && (!isTokenExpired(token) || ignoreTokenExpiration(token));
  }

  public String refreshToken(String token) {
    String refreshedToken;
    try {
      final Claims claims = getClaimsFromToken(token);
      claims.put(CLAIM_KEY_CREATED, new Date());
      refreshedToken = doGenerateToken(claims, accessTokenExpiration);
    } catch (Exception e) {
      refreshedToken = null;
    }
    return refreshedToken;
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    AuthUser user = (AuthUser) userDetails;
    final String username = getUsernameFromToken(token);
    final Date created = getCreatedDateFromToken(token);
    // final Date expiration = getExpirationDateFromToken(token);
    return (username.equals(user.getUsername())
        && !isTokenExpired(token)
        && !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate()));
  }

  private Collection<?> getUniqueAuthorities(UserDetails userDetails) {
    Set<GrantedAuthority> authorities = new HashSet<>();
    authorities.addAll(userDetails.getAuthorities());
    return authorities;
  }
}
