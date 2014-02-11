package com.bericotech.fallwizard.auth;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.config.Environment;
import org.eclipse.jetty.server.session.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * This is used to inject the UserDetails object into REST
 * endpoint functions when the @Auth annotation is used.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class SpringSecurityAuthProvider implements InjectableProvider<Auth, Parameter> {

    private static final Logger logger = LoggerFactory.getLogger(SpringSecurityAuthProvider.class);

	ApplicationContext applicationContext;
	
	public SpringSecurityAuthProvider(ApplicationContext applicationContext){
		
		this.applicationContext = applicationContext;
	}
	
	public void registerProvider(Environment environment){
		
		environment.setSessionHandler(new SessionHandler());
		
		registerSpringSecurityFilters(environment);
		
		environment.addProvider(this);
	}
	
	protected void registerSpringSecurityFilters(Environment environment){

        Map<String, FilterChainProxy> proxies = applicationContext.getBeansOfType(FilterChainProxy.class);

        if (proxies != null){

            for (Map.Entry<String, FilterChainProxy> entry : proxies.entrySet()){

                logger.info("Found FilterChainProxy with Bean ID: {}", entry.getKey());

                FilterChainProxy proxy = entry.getValue();

                environment.addFilter(proxy, "/*").setName(entry.getKey());
            }
        }
        else {

            logger.warn("No FilterChainProxy found, using DelegatingFilterProxy instead.");

            environment.addFilter(DelegatingFilterProxy.class, "/*").setName("springSecurityFilterChain");
        }
	}
	
	@Override
	public Injectable<UserDetails> getInjectable(ComponentContext componentContext, Auth auth, Parameter parameter) {
		
		return new SpringSecurityInjectable(auth.required());
	}
	
	@Override
	public ComponentScope getScope() {
		
		return ComponentScope.PerRequest;
	}
	
	private static class SpringSecurityInjectable extends AbstractHttpContextInjectable<UserDetails> {

		private final boolean authenticationRequired;
		
		public SpringSecurityInjectable(boolean authenticationRequired){
			
			this.authenticationRequired = authenticationRequired;
		}
		
		@Override
		public UserDetails getValue(HttpContext context) {
			
			UserDetails userDetails = null;
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			
			if (!(auth instanceof AnonymousAuthenticationToken)) {
			    
				userDetails =
						 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			}
			
			if (userDetails == null && authenticationRequired){
				
				throw new WebApplicationException(
					Response
						.status(Response.Status.UNAUTHORIZED)
                        .header("WWW-Authenticate", "Basic realm=\"innovationgateway.com\"")
						.entity("Credentials are required to access this resource.")
	                    .type(MediaType.TEXT_PLAIN_TYPE)
	                    .build());
			} 
			
			if (userDetails == null ) {
					
				userDetails = new AnonymousUserDetails();
			}
			
			return userDetails;
		}
	
	}

}
