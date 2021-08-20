package com.taskmanager.app.filter;

import com.taskmanager.app.core.dto.UserPrinciple;
import com.taskmanager.app.util.DateUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
  private String secretKey = "TaskManagerSecret";
  private Long expireHour = Long.valueOf("5");

  public String generateToken(Authentication authentication, HttpServletRequest request) {
    UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
    Date now = new Date();
    return Jwts.builder()
        .setId(UUID.randomUUID().toString())
        .claim("username", userPrinciple.getUsername())
        .claim("ip", request.getRemoteAddr())
        //                .claim("role",
        // userPrinciple.getAuthorities().stream().map(grantedAuthority -> ))
        .setSubject(String.valueOf(userPrinciple.getId()))
        .setIssuedAt(now)
        .setExpiration(DateUtils.getExpirationTime(expireHour))
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .compact();
  }

  public Long getUserIdFromToken(String token) {
    Claims claims = getClaims(token);
    return Long.valueOf(claims.getSubject());
  }

  public String extractUsername(String token) {
    Claims claims = getClaims(token);
    return (String) claims.get("username");
  }

  public String extractIP(String token) {
    Claims claims = getClaims(token);
    return (String) claims.get("ip");
  }

  private Claims getClaims(String token) {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
  }

  public Boolean isValidateToken(String token, HttpServletRequest request) {
    try {
      Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        return !request.getRemoteAddr().isEmpty() && request.getRemoteAddr()
            .equals(extractIP(token));
    } catch (Exception e) {
      return false;
    }
  }
}
