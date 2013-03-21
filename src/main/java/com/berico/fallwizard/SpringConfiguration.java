package com.berico.fallwizard;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

public class SpringConfiguration extends Configuration {

	@NotEmpty
	@JsonProperty
	private String[] applicationContext;

	public String[] getApplicationContext() {
		
		return applicationContext;
	}

	@JsonProperty
	private boolean useSpringSecurity = false;

	public boolean shouldUseSpringSecurity() {
		
		return useSpringSecurity;
	}
}
