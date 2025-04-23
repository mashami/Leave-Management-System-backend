package com.springbootsecurity.utils;

public class ErrorResponse {
  private boolean error;
  private String message;

  public ErrorResponse(boolean error, String message) {
    this.error = error;
    this.message = message;
  }

  // Getters and Setters
  public boolean isError() {
    return error;
  }

  public void setError(boolean error) {
    this.error = error;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
