package com.taskmanager.app.controller;

import com.taskmanager.app.security.TokenProvider;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class AuthenticationController {

  private final TokenProvider jwtProvider;
  private final UserDetailsService userDetailsService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationController(
      TokenProvider jwtProvider,
      UserDetailsService userDetailsService,
      AuthenticationManager authenticationManager) {
    this.jwtProvider = jwtProvider;
    this.userDetailsService = userDetailsService;
    this.authenticationManager = authenticationManager;
  }

  @RequestMapping(value = "auth", produces = "application/json", method = RequestMethod.POST)
  public ResponseEntity<?> authenticationToken(
      @RequestBody Map<String, String> authenticationRequest) throws AuthenticationException {

    Map<String, String> token = new HashMap<>();
    try {
      final Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  authenticationRequest.get("username"), authenticationRequest.get("password")));

      SecurityContextHolder.getContext().setAuthentication(authentication);
      final UserDetails userDetails =
          userDetailsService.loadUserByUsername(authenticationRequest.get("username"));

      final String accessToken = jwtProvider.generateToken(userDetails);
      final String refreshToken = jwtProvider.generateRefreshToken(userDetails);
      token.put("accessToken", accessToken);
      token.put("refreshToken", refreshToken);
      token.put("username", userDetails.getUsername());
      return ResponseEntity.ok(token);
    } catch (BadCredentialsException bce) {
      token.put("error", "invalid username or password");
      return ResponseEntity.ok(token);
    } catch (DisabledException de) {
      token.put("error", "Account is not activated");
      return ResponseEntity.ok(token);
    } catch (Exception e) {
      return new ResponseEntity<HttpStatus>(HttpStatus.EXPECTATION_FAILED);
    }
  }

  @RequestMapping(value = "auth/refreshtoken", method = RequestMethod.POST)
  public ResponseEntity<?> refreshAndGetAuthenticationToken(
      @RequestBody Map<String, String> tokenReq) {

    Map<String, String> token = new HashMap<>();
    if (tokenReq.get("refreshToken") == null) {
      token.put("accessToken", null);
      token.put("refreshToken", null);
      return ResponseEntity.ok(token);
    }

    String username = jwtProvider.getUsernameFromToken(tokenReq.get("refreshToken"));
    final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    if (userDetails != null) {
      try {
        final String accessToken = jwtProvider.generateToken(userDetails);
        final String refreshToken = jwtProvider.generateRefreshToken(userDetails);
        token.put("accessToken", accessToken);
        token.put("refreshToken", refreshToken);
        token.put("username", userDetails.getUsername());
        return ResponseEntity.ok(token);

      } catch (Exception ex) {
        return new ResponseEntity<HttpStatus>(HttpStatus.EXPECTATION_FAILED);
      }

    } else {
      token.put("accessToken", null);
      token.put("refreshToken", null);
      return ResponseEntity.ok(token);
    }
  }
}
