package com.bericotech.fallwizard.configuration;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

/**
 * Spring Configuration, which determines whether to use
 * Spring Security, the location of the Application
 * Context files, and what Bean Profiles to use.
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
	protected String[] beanProfiles;
	
	public String[] getBeanProfiles(){
		
		return this.beanProfiles;
	}
	
	public void setBeanProfiles(String[] beanProfiles){
		
		this.beanProfiles = beanProfiles;
	}
	
	@JsonProperty
	protected boolean useSpringSecurity = false;

	public boolean shouldUseSpringSecurity() {
		
		return useSpringSecurity;
	}
	
	public void setShouldUseSpringSecurity(boolean shouldUse){
		
		this.useSpringSecurity = shouldUse;
	}

    @JsonProperty("propertyPlaceholderConfigurer")
    protected SpringPropertyPlaceholderConfigurerConfiguration placeholderConfiguration;

    public SpringPropertyPlaceholderConfigurerConfiguration getPlaceholderConfiguration() {
        return placeholderConfiguration;
    }
}
