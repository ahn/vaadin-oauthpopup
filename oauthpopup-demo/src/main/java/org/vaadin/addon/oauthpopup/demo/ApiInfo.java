package org.vaadin.addon.oauthpopup.demo;

import org.scribe.builder.api.Api;

public class ApiInfo {
	public final String name;
	public final Class<? extends Api> scribeApi;
	public final String apiKey;
	public final String apiSecret;
	public final String exampleGetRequest;
	public ApiInfo(String name, Class<? extends Api> scribeApi,
			String apiKey, String apiSecret, String exampleGetRequest) {
		super();
		this.name = name;
		this.scribeApi = scribeApi;
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
		this.exampleGetRequest = exampleGetRequest;
	}
}
