package com.berico.dropwizard.auth.ssl;

import java.security.NoSuchAlgorithmException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.net.ssl.SSLContext;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;

/**
 * Utilize the DN from the SSL Certificate as the name of the Client.
 * - Modeled after Dropwizard's Basic Authenticator.
 * 
 * @author Richard Clayton (Berico Technologies)
 *
 * @param <T> The Desired Principal Type
 */
public class SslAuthProvider<T> implements InjectableProvider<Auth, Parameter>  {

	private static Logger logger = LoggerFactory.getLogger(SslAuthProvider.class);
	
	private static class SslAuthInjectable<T> extends AbstractHttpContextInjectable<T> {

		private final Authenticator<SslCredentials, T> authenticator;
		private final String realm; 
		private final boolean required;
		
		private SslAuthInjectable(Authenticator<SslCredentials, T> authenticator, String realm, boolean required) {
            this.authenticator = authenticator;
            this.realm = realm;
            this.required = required;
        }
		
		@Override
		public T getValue(HttpContext httpContext) {
			
			SslCredentials credentials = new SslCredentials("app02");
			
			dumpHttpContext(httpContext);
			
			try {
				
				final Optional<T> result = authenticator.authenticate(credentials);
				
				if (result.isPresent()) {
					
                    return result.get();
                }
				
			} catch (AuthenticationException e) {
				
                logger.warn("Error authenticating credentials", e);
                throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
			}
			
			if (required) {
                throw new WebApplicationException(
                		Response.status(Response.Status.UNAUTHORIZED)
                			.entity("A valid certificate is required to access this resource.")
                			.type(MediaType.TEXT_PLAIN_TYPE)
                			.build());
			}
			
			return null;
		}
		
		private void dumpHttpContext(HttpContext context){
			
			logger.info("Request: {}", context.getRequest());
			logger.info("Request.authScheme: {}", context.getRequest().getAuthenticationScheme());
			logger.info("Request.userPrincipal: {}", context.getRequest().getUserPrincipal());
			logger.info("Request.headers: {}", context.getRequest().getRequestHeaders());
			logger.info("Response: {}", context.getResponse());
		}
		
		
	}
	
	private Authenticator<SslCredentials, T> authenticator;
	private String realm;
	
	public SslAuthProvider(Authenticator<SslCredentials, T> authenticator, String realm) {
        this.authenticator = authenticator;
        this.realm = realm;
    }
	
	@Override
	public Injectable<?> getInjectable(ComponentContext componentContext, Auth auth, Parameter parameter) {
		
		return new SslAuthInjectable<T>(authenticator, realm, auth.required());
	}

	@Override
	public ComponentScope getScope() {
		
		return ComponentScope.PerRequest;
	}	
}
