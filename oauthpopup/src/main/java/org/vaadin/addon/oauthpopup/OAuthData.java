package org.vaadin.addon.oauthpopup;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.DefaultApi20;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

/**
 * Thread-safe class for storing OAuth data.
 * <p>
 * Works as an intermediary between two browser windows:
 * the OAuthPopup window and the Vaadin window containing the OAuthPopupButton.
 */
public class OAuthData {
	
	static long latestId = 0;
	synchronized public String nextId() {
		return ""+(++latestId);
	}
	
	private List<OAuthListener> listeners = new CopyOnWriteArrayList<OAuthListener>();
	
	private final String id;
	private final Class<? extends Api> apiClass;
	private final String apiKey;
	private final String apiSecret;
	
	private String callback;
	private String scope;
	
	private String verifierParameterName;
	private List<String> errorParameterNames;
	
	private Token accessToken;

	private OAuthService service;
	
	public OAuthData(Class<? extends Api> apiClass, String apiKey, String apiSecret) {
		this.id = nextId();
		this.apiClass = apiClass;
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
		setVerifierParameterNameToDefault();
		setDefaultErrorParameterNames();
	}

	public String getId() {
		return id;
	}
	
	public String getSessionAttributeName() {
		return OAuthData.class.getCanonicalName()+".DATA."+getId();
	}
	
	public void addListener(OAuthListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(OAuthListener listener) {
		listeners.remove(listener);
	}
	
	public Class<? extends Api> getApiClass() {
		return apiClass;
	}
	
	public String getApiKey() {
		return apiKey;
	}
	
	public String getApiSecret() {
		return apiSecret;
	}
	
	public boolean isOauth2() {
		return DefaultApi20.class.isAssignableFrom(getApiClass());
	}
	
	synchronized public String getCallback() {
		return callback;
	}
	
	synchronized public void setCallback(String callback) {
		try {
			this.callback = appendIdToCallback(callback);
		} catch (URISyntaxException e) {
			throw new OAuthException("Invalid callback URI syntax: " + callback, e);
		}
	}
	
	/**
	 * Appends the "/oauthpopupcallback/ID" to the callback path
	 * so that the {@link OAuthCallbackRequestHandler} knows that it
	 * should handle the callback.
	 */
	private String appendIdToCallback(String callback) throws URISyntaxException {
		URI old = new URI(callback);
		String oldPath = old.getPath();
		String idPath = OAuthCallbackRequestHandler.CALLBACK_PATH+"/"+getId();
		String newPath = oldPath.endsWith("/") ? oldPath+idPath : oldPath+"/"+idPath;
		URI newUri = new URI(old.getScheme(), old.getAuthority(), newPath, old.getQuery(), old.getFragment());
		return newUri.toASCIIString();
	}
	
	synchronized public String getScope() {
		return scope;
	}
	
	synchronized public void setScope(String scope) {
		this.scope = scope;
	}
	
	synchronized public String getVerifierParameterName() {
		return verifierParameterName;
	}
	
	synchronized public void setVerifierParameterName(String verifierParameterName) {
		this.verifierParameterName = verifierParameterName;
	}
	
	public void setVerifierParameterNameToDefault() {
		// XXX does this make sense?
		setVerifierParameterName(isOauth2() ? "code" : "oauth_verifier");
	}
	
	synchronized public List<String> getErrorParameterNames() {
		return errorParameterNames;
	}
	
	synchronized public void setErrorParameterNames(List<String> errorParameterNames) {
		this.errorParameterNames = errorParameterNames;
	}
	
	public void setDefaultErrorParameterNames() {
		setErrorParameterNames(Arrays.asList(new String[] {"error","denied","oauth_problem"}));
	}
	
	public Token createNewRequestToken() {
		// OAuth2 doesn't use request token (?)
		// With OAuth2, Scribe uses null in place of request token,
		// that's why we return null.
		if (isOauth2()) {
			return null;
		}
		try {
			synchronized (this) {
				return getService().getRequestToken();
			}
		}
		catch (OAuthException e) {
			throw createException("Getting request token failed.", e);
		}
	}
	
	public void setVerifier(Token requestToken, Verifier verifier) {
		try {
			Token at;
			synchronized (this) {
				at = getService().getAccessToken(requestToken, verifier);
				setAccessToken(at);
			}
			fireSuccess(at);
		}
		catch (OAuthException e) {
			throw createException("Getting access token failed.", e);
		}
	}
	
	public OAuthException createException(String msg, OAuthException e) {
		return new OAuthException(msg
				+ "\nUsing Scribe API: " + getApiClass().getSimpleName()
				+ "\n", e);
	}
	
	public void setDenied(String reason) {
		fireFailure(reason);
	}
	
	synchronized public Token getAccessToken() {
		return accessToken;
	}
	
	synchronized public void setAccessToken(Token accessToken) {
		this.accessToken = accessToken;
	}

	synchronized public OAuthConfig asConfig() {
		return new OAuthConfig(apiKey, apiSecret, callback, SignatureType.Header, scope, null);
	}
	
	private OAuthService getService() {
		if (service==null) {
			service = createApiInstance().createService(asConfig());
		}
		return service;
	}
	
	private Api createApiInstance() {
		try {
			return apiClass.newInstance();
		} catch (InstantiationException e) {
			throw new OAuthException("Error while creating the Api object", e);
		} catch (IllegalAccessException e) {
			throw new OAuthException("Error while creating the Api object", e);
		}
	}

	private void fireSuccess(Token at) {
		for (OAuthListener li : listeners) {
			li.authSuccessful(at.getToken(), at.getSecret());
		}
	}
	
	private void fireFailure(String reason) {
		for (OAuthListener li : listeners) {
			li.authDenied(reason);
		}
	}

	public synchronized String getAuthorizationUrl(Token requestToken) {
		return getService().getAuthorizationUrl(requestToken);
	}
	
}
