package org.vaadin.addon.oauthpopup;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.oauth.OAuthService;
import com.vaadin.server.VaadinRequest;

/**
 * Thread-safe class for storing OAuth data.
 * <p>
 * Works as an intermediary between two browser windows:
 * the OAuthPopup window and the Vaadin window containing the OAuthPopupButton.
 */
/**
 * @author Bryson Dunn
 *
 */
public class OAuthData {
	
	static long latestId = 0;
	synchronized public String nextId() {
		return ""+(++latestId);
	}
	
	private static final String CALLBACK_ID_PARAMETER_NAME = "callback_id";
	
	private List<OAuthListener> listeners = new CopyOnWriteArrayList<OAuthListener>();
	
	private final DefaultApi10a api10a;
	private final DefaultApi20 api20;
	private final OAuthPopupConfig config;

	private final String id;
	private Token accessToken;
	private OAuthService service;
	
	protected OAuthData(DefaultApi10a api, OAuthPopupConfig config) {
		this.id = nextId();
		this.api10a = api;
		this.api20 = null;
		this.config = config;
		injectCallbackId();
	}
	
	protected OAuthData(DefaultApi20 api, OAuthPopupConfig config) {
		this.id = nextId();
		this.api20 = api;
		this.api10a = null;
		this.config = config;
		injectCallbackId();
	}
	
	private void injectCallbackId() {
		String callback = config.getCallbackUrl();
		callback = config.getCallbackInjector().injectParameterToCallback(callback, CALLBACK_ID_PARAMETER_NAME, getId());
		config.setCallbackUrl(callback);
	}
	
	/**
	 * Returns the gloabl UID of this OAuthData object.
	 * 
	 * @return Global UID
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Returns a unique identifer used to store and retrieve this object from Vaadin session storage.
	 * 
	 * @return Identifier string
	 */
	public String getSessionAttributeName() {
		return OAuthData.class.getCanonicalName()+".DATA."+getId();
	}
	
	/**
	 * Indicates whether this instance is being used for an OAuth 1.0a API service.
	 * 
	 * @return true if 1.0a, otherwise false
	 */
	public boolean isOAuth10a() {
		return api10a != null;
	}
	
	/**
	 * Retrieves the OAuth 1.0a API instance.
	 * 
	 * @return {@link DefaultApi10a} instance if {@link #isOAuth10a()}, otherwise null.
	 */
	public DefaultApi10a getOAuth10aApi() {
		return api10a;
	}
	
	/**
	 * Indicates whether this instance is being used for an OAuth 2.0 API service.
	 * 
	 * @return true if 2.0, otherwise false
	 */
	public boolean isOAuth20() {
		return api20 != null;
	}

	/**
	 * Retrieves the OAuth 2.0 API instance.
	 * 
	 * @return {@link DefaultApi10a} instance if {@link #isOAuth20()}, otherwise null.
	 */
	public DefaultApi20 getOAuth20Api() {
		return api20;
	}
	
	/**
	 * Retrieves OAuth configuration.
	 * 
	 * @return OAuth configuration
	 */
	public OAuthPopupConfig getOAuthPopupConfig() {
		return config;
	}
	
	/**
	 * Add an OAuth authentication listener
	 * 
	 * @param listener OAuth authentication listener to add
	 */
	public void addOAuthListener(OAuthListener listener) {
		listeners.add(listener);
	}

	/**
	 * Remove an OAuth authentication listener
	 * 
	 * @param listener OAuth authentication listener to remove
	 */
	public void removeOAuthListener(OAuthListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Generates the intial request token for OAuth 1.0a authorization requests.
	 * OAuth 2.0 does not utilize request tokens. This method returns null when accessing 
	 * an OAuth 2.0 API.
	 * 
	 * @return The OAuth 1.0a request token if {@link #isOAuth10a()}, otherwise null.
	 */
	public OAuth1RequestToken createNewRequestToken() {
		if (isOAuth20()) {
			return null;
		}
		try {
			synchronized (this) {
				return ((OAuth10aService) getService()).getRequestToken();
			}
		}
		catch (OAuthException | IOException e) {
			throw createException("Getting request token failed.", e);
		}
	}
	
	/**
	 * <p>Set the verifier token after successful user authorization. This initiates a request
	 * to the OAuth service to exchange the verifier token with an access token to complete 
	 * the OAuth authorization flow.</p>
	 * <p>OAuth 2.0 does not require the intial authorization request token to retrieve the 
	 * access token, so {@code requestToken} should be null if {@link #isOAuth20()}.</p>
	 * 
	 * @param requestToken OAuth request token if version 1.0a, otherwise null
	 * @param verifier OAuth verifier token
	 */
	public void setVerifier(OAuth1RequestToken requestToken, String verifier) {
		try {
			Token at;
			synchronized (this) {
				if (isOAuth20()) {
					at = ((OAuth20Service) getService()).getAccessToken(verifier);
				} else {
					at = ((OAuth10aService) getService()).getAccessToken(requestToken, verifier);
					setAccessToken(at);
				}
			}
			fireSuccess(at);
		}
		catch (OAuthException | IOException e) {
			throw createException("Getting access token failed.", e);
		}
	}
	
	/**
	 * <p>Retrieves the OAuth access token. This will return null until the entire OAuth
	 * authorization flow has finished successfully.<p>
	 * <p>Depending on the OAuth API version this method will return an instance of either 
	 * {@link OAuth1AccessToken} or {@link OAuth2AccessToken}.</p>
	 * 
	 * @return The OAuth access token, or null if authorization has not been finalized.
	 */
	synchronized public Token getAccessToken() {
		return accessToken;
	}
	
	/**
	 * Set the access token for the OAuth authorization flow. {@code accessToken} should be 
	 * an instance of either {@link OAuth1AccessToken} or {@link OAuth2AccessToken}, depending
	 * on the OAuth API version.
	 * 
	 * @param accessToken The OAuth access token.
	 */
	synchronized public void setAccessToken(Token accessToken) {
		this.accessToken = accessToken;
	}
	
	/**
	 * Create a new OAuth exception with additional information about the API in use.
	 * 
	 * @param msg Error message
	 * @param e Cause
	 * @return Enriched exception
	 */
	public OAuthException createException(String msg, Exception e) {
		return new OAuthException(msg
				+ "\nUsing Scribe API: " + (isOAuth20() ? api20.getClass().getSimpleName() : api10a.getClass().getSimpleName())
				+ "\n", e);
	}
	
	/**
	 * Trigger {@link OAuthListener#authDenied(String)} for current listeners upon authorization failure.
	 * 
	 * @param reason Cause of authorization failure
	 */
	public void setDenied(String reason) {
		fireFailure(reason);
	}

	private void fireSuccess(Token token) {
		final boolean isOAuth20 = isOAuth20();
		for (OAuthListener li : listeners) {
			li.authSuccessful(token, isOAuth20);
		}
	}
	
	private void fireFailure(String reason) {
		for (OAuthListener li : listeners) {
			li.authDenied(reason);
		}
	}
	
	/**
	 * Analyzes the {@code request} to determine if it is a callback from the OAuth API service. The global
	 * identifier of this object returned by {@link #getId()} is used to decide this.
	 * 
	 * @param request Vaadin request to check
	 * @return true if the request is an OAuth callback for this authorization flow, otherwise false.
	 */
	public boolean isCallbackForMe(VaadinRequest request) {
		return getId().equals(config.getCallbackInjector().extractParameterFromCallbackRequest(request, CALLBACK_ID_PARAMETER_NAME));
	}
	
	/**
	 * Retrieves the {@link OAuth20Service} or {@link OAuth10aService} ScribeJava service, depending on the
	 * OAuth API version being used.
	 * 
	 * @return the ScribeJava OAuth service.
	 */
	private OAuthService getService() {
		if (service == null) {
			if (isOAuth20()) {
				service = config.createScribeServiceBuilder().build(api20);
			} else {
				service = config.createScribeServiceBuilder().build(api10a);
			}
		}
		return service;
	}
	
	/**
	 * Returns the OAuth 1.0a or 2.0 authorization URL.
	 * 
	 * @param requestToken OAuth 1.0a request token or null if OAuth 2.0
	 * @return An OAuth authorization URL
	 */
	public synchronized String getAuthorizationUrl(OAuth1RequestToken requestToken) {
		String url;
		if (isOAuth20()) {
			// OAuth20Service automatically injects "redirect_uri" parameter based on OAuthConfig
			url = ((OAuth20Service) getService()).getAuthorizationUrl();
		} else {
			url = ((OAuth10aService) getService()).getAuthorizationUrl(requestToken);
			url = config.getCallbackInjector().injectParameterToCallback(url, config.getCallbackParameterName(), config.getCallbackUrl());
		}
		return url;
	}
}
