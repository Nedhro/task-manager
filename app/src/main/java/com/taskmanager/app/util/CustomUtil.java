package com.taskmanager.app.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

public class CustomUtil {

  public static Map<String, Set<String>> permissions = new HashMap<>();

  public static String getAppUrl(HttpServletRequest request) {
    return "http://"
        + request.getServerName()
        + ":"
        + request.getServerPort()
        + request.getContextPath();
  }
}
