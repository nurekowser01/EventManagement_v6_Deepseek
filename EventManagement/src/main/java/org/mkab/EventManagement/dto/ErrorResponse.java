package org.mkab.EventManagement.dto;

//package: org.mkab.EventManagement.dto (or anywhere appropriate)

public class ErrorResponse {
 private String error;

 public ErrorResponse() {}

 public ErrorResponse(String error) {
     this.error = error;
 }

 public String getError() {
     return error;
 }

 public void setError(String error) {
     this.error = error;
 }
}

