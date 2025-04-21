package org.mkab.EventManagement.auth;

//AuthResponse.java

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
 private String token;

public String getToken() {
	return token;
}

public AuthResponse(String token) {
	super();
	this.token = token;
}

public void setToken(String token) {
	this.token = token;
}
}
