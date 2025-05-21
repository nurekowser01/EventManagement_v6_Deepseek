package org.mkab.EventManagement.Security;

import org.mkab.EventManagement.entity.Admin;
import org.mkab.EventManagement.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

	private final Admin admin;

	public CustomUserDetails(Admin admin) {
		this.admin = admin;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return admin.getRoles().stream().map(Role::getType)
				.map(roleType -> new SimpleGrantedAuthority("ROLE_" + roleType.name())).collect(Collectors.toSet());
	}

	@Override
	public String getPassword() {
		return admin.getPassword();
	}

	@Override
	public String getUsername() {
		return admin.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return admin.getLoginAttempts() < 5 || admin.getIsActive();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return admin.getIsActive();
	}

	public Admin getAdmin() {
		return admin;
	}
}
