package com.berico.fallwizard;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import net.sourceforge.argparse4j.inf.Namespace;

import com.yammer.dropwizard.cli.ConfiguredCommand;
import com.yammer.dropwizard.config.Bootstrap;

public abstract class SpringConfiguredCommand extends ConfiguredCommand<SpringConfiguration> {

	protected SpringConfiguredCommand(String name, String description) {
		
		super(name, description);
	}

	ApplicationContext context;
	
	@Override
	protected void run(
		Bootstrap<SpringConfiguration> bootstrap, Namespace namespace,
		SpringConfiguration configuration) throws Exception {
		
		// Initialize the Spring Context from the files in the configuration.
		context = new FileSystemXmlApplicationContext(
				configuration.getApplicationContext());
		
	}
	
	
	

}
