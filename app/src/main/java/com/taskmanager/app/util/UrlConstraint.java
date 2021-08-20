package com.taskmanager.app.util;

public final class UrlConstraint {
  private UrlConstraint() {}

  private static final String VERSION = "/v1";
  private static final String API = "/api";

  public static class RoleManagement {
    public static final String ROOT = VERSION + API + "/roles";
    public static final String DELETE = "/{roleId}";
    public static final String GET = "/{roleId}";
    public static final String PUT = "/{roleId}";
  }

  public static class UserManagement {
    public static final String ROOT = VERSION + API + "/users";
    public static final String DELETE = "/{id}";
    public static final String GET = "/{id}";
    public static final String PUT = "/{id}";
  }

  public static class AuthManagement {
    public static final String ROOT = VERSION + API + "/auth";
    public static final String LOGIN = "/login";
  }
}
