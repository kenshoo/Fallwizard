package com.berico.fallwizard;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

/**
 * Spring Configuration, which determines whether to use
 * Spring Security and the location of the Application
 * Context files.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class SpringConfiguration extends Configuration {

	@NotEmpty
	@JsonProperty
	protected String[] applicationContext;

	public String[] getApplicationContext() {
		
		return applicationContext;
	}
	
	public void setApplicationContext(String[] contextFiles){
		
		this.applicationContext = contextFiles;
	}

	@JsonProperty
	protected boolean useSpringSecurity = false;

	public boolean shouldUseSpringSecurity() {
		
		return useSpringSecurity;
	}
	
	public void setShouldUseSpringSecurity(boolean shouldUse){
		
		this.useSpringSecurity = shouldUse;
	}
}
