# Fallwizard

We love Spring Framework because it simplified Enterprise Java.  We love Dropwizard because it simplified App Servers.  What if these two frameworks made sweet-sweet love and had a child?  Well, you would get Fallwizard.

Ok seriously...

Fallwizard is the integration of Spring with Dropwizard.  This gives us the following benefits we were missing when using pure Dropwizard:

* Ability to support complex configurations outside of the yaml mechanism (i.e. we need more dynamicism).
* Better support for complex authentication and authorization environments.

> This is not a condemnation of Dropwizard.  Most organizations don't need the flexibility we need in configuration and security.

## Features

* Define Resources, Healthchecks, Tasks, etc. in Spring, and have them automatically bootstrapped in Dropwizard.
* Utilize Spring Security (out of the box) and have it applied to Dropwizard routes.
* Automatically inject a Spring Security UserDetails object into REST methods using Dropwizard's `@Auth` annotation.


## Using Fallwizard

### Configuring Fallwizard




### Spring Security Integration

We needed certificated-based client authentication, but didn't want to force BasicAuth when we were already using SSL just so we could inject an object (i.e. `@Auth`) in our JAX-RS controllers.

We also wanted the rich features of Spring Security, which includes support for numerous authentication mechanisms.

### Configure Spring Security in your applicationContext.xml

Here is an example for Certificate-based authentication

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

### Register Spring Security with the Dropwizard `Environment`.

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
public ChittyChat getChittyChatOnTopic(@Auth UserDetails userDetails, @PathParam("topic") String topic){
   // ... get Chitty-Chat ...
}
```

### Enjoy a cold beer, you deserve it.

You're done.  Muck with the Spring ApplicationContext "outside" of Dropwizard.
