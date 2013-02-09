# Dropwizard Utils

Essential utilities for the Dropwizard Framework, including Spring Security integration.

## Spring Security Integration

We needed certificated-based client authentication, but didn't want to force BasicAuth when we were already using SSL just so we could inject an object (i.e. `@Auth`) in our JAX-RS controllers.

We also wanted the rich features of Spring Security, which includes support for numerous authentication mechanisms.

### Configure Spring Security in your applicationContext.xml

Here is an example for Certificate-base authentication

```xml
<security:http>
  	<security:intercept-url pattern="/*" access="ROLE_USER" />
    <security:intercept-url pattern="/admin/*" access="ROLE_ADMIN" />
		<security:x509 subject-principal-regex="CN=(.*?)," />
	</security:http>
 
	<security:authentication-manager>
	   <security:authentication-provider>
	       <security:user-service>
		   		<security:user name="Super Awesome Client" authorities="ROLE_USER" />
           <security:user name="The Boss" authorities="ROLE_USER, ROLE_ADMIN" />
	       </security:user-service>
	   </security:authentication-provider>
	</security:authentication-manager>
```

### Initialize your Spring Application Context.  

Initialize your Spring Application Context in your Dropwizard `Service` class.  We explicitly require the location of the `applicationContext` in our Dropwizard `Configuration` class.

```java
ApplicationContext applicationContext = 
  		new FileSystemXmlApplicationContext(
				configuration.getSpringApplicationContext());
```

### Register Spring Security with the DropWizard `Environment`.

```java
@Override
public void run(BlahBlahConfiguration configuration, Environment environment) throws Exception {
		
	ApplicationContext applicationContext = 
		new FileSystemXmlApplicationContext(
			configuration.getSpringApplicationContext());
	
	new SpringSecurityAuthProvider(applicationContext).registerProvider(environment);
}
```

### Use `@Auth UserDetails userDetails` in your JAX-RS controllers.

We're going to inject the Spring Security `UserDetails` context into your controllers (why invent a new `User` object?).

```java
@GET
@Timed
@Path("/chitty-chat/{topic}")
public ChittyChat getChittyChatOnTopic(@Auth UserDetails user, @PathParam("topic") String topic){
   // ... get Chitty-Chat ...
}

```

### Enjoy a cold beer, you deserve it.

You're done.  Muck with the Spring ApplicationContext "outside" of Dropwizard.
