package com.berico.fallwizard.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AnonymousUserDetails implements UserDetails {
	
	private static final long serialVersionUID = -7347714058551005612L;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return new ArrayList<GrantedAuthority>();
	}

	@Override
	public String getPassword() {
		
		return null;
	}

	@Override
	public String getUsername() {
		
		return "anonymous";
	}

	@Override
	public boolean isAccountNonExpired() {
		
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		
		return false;
	}

	@Override
	public boolean isEnabled() {
		
		return false;
	}

}
