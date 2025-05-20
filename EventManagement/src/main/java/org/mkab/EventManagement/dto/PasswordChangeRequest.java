package org.mkab.EventManagement.dto;

import lombok.Data;

@Data
public class PasswordChangeRequest {
	 private String oldPassword;
    private String newPassword;
}
