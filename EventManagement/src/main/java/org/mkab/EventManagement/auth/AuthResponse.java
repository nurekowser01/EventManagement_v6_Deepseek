package org.mkab.EventManagement.auth;

//AuthResponse.java

import lombok.Data;

@Data
public class AuthResponse {
 private String token;
 private Long id;  // Add this field

public String getToken() {
	return token;
}

public AuthResponse(String token, Long id) {
	super();
	this.token = token;
    this.id = id;

}

public void setToken(String token) {
	this.token = token;
}
}
