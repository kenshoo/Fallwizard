package com.berico.fallwizard;

/**
 * If you have no need to supply custom configuration or functionality
 * to your container, just use this guy as your entry point.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class Fallwizard extends SpringService<SpringConfiguration> {

	public static void main( String[] args ) throws Exception
    {
    		new Fallwizard().run(args);
    }
}
