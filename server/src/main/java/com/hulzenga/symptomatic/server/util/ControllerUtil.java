package com.hulzenga.symptomatic.server.util;

import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;

/**
 * Created by jouke on 11/18/14.
 */
public class ControllerUtil {

  public static Principal getPrincipal() {
    return SecurityContextHolder.getContext().getAuthentication();
  }
}
