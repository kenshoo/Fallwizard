package com.berico.dropwizard.auth.ssl;

public class SslCredentials {
	
	private String DN;

	public SslCredentials(String dN) {
		DN = dN;
	}

	public String getDN() {
		return DN;
	}

}
