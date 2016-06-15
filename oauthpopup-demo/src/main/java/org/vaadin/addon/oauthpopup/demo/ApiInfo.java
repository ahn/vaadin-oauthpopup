package org.vaadin.addon.oauthpopup.demo;

public class ApiInfo {
	public final String name;
	public final Object scribeApi;
	public final String apiKey;
	public final String apiSecret;
	public final String exampleGetRequest;
	public ApiInfo(String name, Object scribeApi, String apiKey, String apiSecret, String exampleGetRequest) {
		super();
		this.name = name;
		this.scribeApi = scribeApi;
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
		this.exampleGetRequest = exampleGetRequest;
	}
}
