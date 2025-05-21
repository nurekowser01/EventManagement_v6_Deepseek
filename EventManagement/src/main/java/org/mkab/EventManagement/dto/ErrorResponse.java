package org.mkab.EventManagement.dto;

import lombok.Data;

//package: org.mkab.EventManagement.dto (or anywhere appropriate)
@Data
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

