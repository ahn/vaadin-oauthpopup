package org.vaadin.addon.oauthpopup;

import java.io.OutputStream;

import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.SignatureType;

/**
 * <p>This object is used to store OAuth configuration that may change between particular API 
 * implementations. Pre-configured objects for standard OAuth 1.0a and OAuth 2.0 APIs can 
 * be retrieved with {@link #getStandardOAuth10aConfig(String, String)} and 
 * {@link #getStandardOAuth20Config(String, String)}.<p>
 * 
 * <p>This addon reserves the {@code callback} and {@code state} settings for 
 * internal use.</p>
 * 
 * @author Bryson Dunn
 *
 */
public class OAuthPopupConfig {
	
	// Set by user, or defaulted
	private String apiKey;
	private String apiSecret;
	private String scope;
	private String grantType;
	private String responseType;
	private String callbackParameterName;
	private String verifierParameterName;
	private SignatureType signatureType;
	private OutputStream outputStream;
	private Integer connectTimeout;
	private Integer readTimeout;
	private OAuthCallbackInjector callbackInjector;
	private String errorParameterName;
	
	// Set by OAuthData
	private String callback;
	private String state;
	
	protected OAuthPopupConfig(String apiKey, String apiSecret) { 
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
	}
	
	/**
	 * Create a pre-configured {@code OAuthPopupConfig} instance with standard OAuth 1.0a settings.
	 * 
	 * @param apiKey The client API key for the OAuth service.
	 * @param apiSecret The client API secret for the OAuth service.
	 * @return A pre-configured OAuth configuration object.
	 */
	public static OAuthPopupConfig getStandardOAuth10aConfig(String apiKey, String apiSecret) {
		return new OAuthPopupConfig(apiKey, apiSecret)
				.setCallbackParameterName("oauth_callback")
				.setVerifierParameterName("oauth_verifier")
				.setSignatureType(SignatureType.QueryString)
				.setCallbackInjector(OAuthCallbackInjector.QUERY_INJECTOR);
	}

	/**
	 * <p>Create a pre-configured {@code OAuthPopupConfig} instance with standard OAuth 2.0 settings.<p>
	 * 
	 * @param apiKey The client API key for the OAuth service.
	 * @param apiSecret The client API secret for the OAuth service.
	 * @return A pre-configured OAuth configuration object.
	 */
	public static OAuthPopupConfig getStandardOAuth20Config(String apiKey, String apiSecret) {
		return new OAuthPopupConfig(apiKey, apiSecret)
				.setCallbackParameterName("redirect_uri")
				.setVerifierParameterName("code")
				.setResponseType("code")
				.setGrantType("authorization_code")
				.setSignatureType(SignatureType.QueryString)
				.setCallbackInjector(OAuthCallbackInjector.QUERY_INJECTOR)
				.setErrorParameterName("error");
	}
	
	protected OAuthConfig asScribeConfig() {
		return new OAuthConfig(getApiKey(), 
				getApiSecret(), 
				getCallback(),
				getSignatureType(), 
				getScope(), 
				getOutputStream(), 
				getConnectTimeout(), 
				getReadTimeout(), 
				getGrantType(), 
				getState(), 
				getResponseType()
			);
	}
	
	/**
	 * Retrieves the client API key to be used for OAuth authorization.
	 * 
	 * @return The client API key for the OAuth service.
	 */
	public String getApiKey() {
		return apiKey;
	}
	
	/**
	 * Retrieves the client API secret to be used for OAuth authorization.
	 * 
	 * @return The client API secret for the OAuth service.
	 */
	public String getApiSecret() {
		return apiSecret;
	}
	
	/**
	 * Retrieve the "scope" parameter to be used for OAuth authorization.
	 * 
	 * @return The OAuth scope parameter.
	 */
	public String getScope() {
		return scope;
	}
	
	/**
	 * Set the "scope" parameter to be used for OAuth authorization. This is 
	 * typically a space-separated list of string values used with OAuth 2.0 services.
	 * 
	 * @param scope The OAuth scope parameter.
	 * @return The {@code OAuthPopupConfig} instance.
	 */
	public OAuthPopupConfig setScope(String scope) {
		this.scope = scope;
		return this;
	}
	
	/**
	 * Retrieve the "grant_type" parameter to be used for OAuth authorization. 
	 * 
	 * @return The OAuth grant type parameter.
	 */
	public String getGrantType() {
		return grantType;
	}
	
	/**
	 * Set the "grant_type" parameter to be used for OAuth authorization. Should 
	 * typically be set to "authorization_code" for OAuth 2.0 services.
	 * 
	 * @param grantType The OAuth grant type parameter.
	 * @return The {@code OAuthPopupConfig} instance.
	 */
	public OAuthPopupConfig setGrantType(String grantType) {
		this.grantType = grantType;
		return this;
	}
	
	/**
	 * Retrieve the "response_type" parameter to be used for OAuth authorization. 
	 * 
	 * @return The OAuth response type parameter.
	 */
	public String getResponseType() {
		return responseType;
	}
	
	/**
	 * Set the "response_type" parameter to be used for OAuth authorization. Should 
	 * typically be set to "authorization_code" for OAuth 2.0 services.
	 * 
	 * @param responseType The OAuth response type parameter.
	 * @return The {@code OAuthPopupConfig} instance.
	 */
	public OAuthPopupConfig setResponseType(String responseType) {
		this.responseType = responseType;
		return this;
	}

	/**
	 * Retrieves the query parameter name used to identify the authorization callback. 
	 * 
	 * @return The callback query parameter name.
	 */
	public String getCallbackParameterName() {
		return callbackParameterName;
	}

	/**
	 * Set the query parameter name used to identify the authorization callback. This
	 * is typically "redirect_uri" for OAuth 2.0 and "oauth_callback" for OAuth 1.0a.
	 * 
	 * @param callbackParameterName The callback query parameter name.
	 * @return The {@code OAuthPopupConfig} instance.
	 */
	public OAuthPopupConfig setCallbackParameterName(String callbackParameterName) {
		this.callbackParameterName = callbackParameterName;
		return this;
	}

	/**
	 * Retrieves the query parameter name used to identify the verifier token. 
	 * 
	 * @return The callback query parameter name.
	 */
	public String getVerifierParameterName() {
		return verifierParameterName;
	}

	/**
	 * Set the query parameter name used to identify the verifier token. This
	 * is typically "code" for OAuth 2.0 and "oauth_verifier" for OAuth 1.0a.
	 * 
	 * @param verifierParameterName The verifier query parameter name.
	 * @return The {@code OAuthPopupConfig} instance.
	 */
	public OAuthPopupConfig setVerifierParameterName(String verifierParameterName) {
		this.verifierParameterName = verifierParameterName;
		return this;
	}

	/**
	 * Retrive the method used to retrieve the OAuth signature.
	 * 
	 * @return The signature type.
	 */
	public SignatureType getSignatureType() {
		return signatureType;
	}

	/**
	 * Set the method used to retrieve the OAuth signature. This is OAuth service specific.
	 * 
	 * @param signatureType The signature type.
	 * @return The {@code OAuthPopupConfig} instance.
	 */
	public OAuthPopupConfig setSignatureType(SignatureType signatureType) {
		this.signatureType = signatureType;
		return this;
	}

	/**
	 * Retrieve the number of milliseconds to wait for a response from the OAuth authorization 
	 * request.
	 * 
	 * @return The connection timeout value.
	 */
	public Integer getConnectTimeout() {
		return connectTimeout;
	}

	/**
	 * Set the number of milliseconds to wait for a response from the OAuth authorization 
	 * request.
	 * 
	 * @param connectTimeout The connection timeout value.
	 * @return The {@code OAuthPopupConfig} instance.
	 */
	public OAuthPopupConfig setConnectTimeout(Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
		return this;
	}

	/**
	 * Retrieve the number of milliseconds to wait for a callback from the OAuth authorization 
	 * flow.
	 * 
	 * @return The read timeout value.
	 */
	public Integer getReadTimeout() {
		return readTimeout;
	}

	/**
	 * Set the number of milliseconds to wait for a callback from the OAuth authorization 
	 * flow.
	 * 
	 * @param connectTimeout The read timeout value.
	 * @return The {@code OAuthPopupConfig} instance.
	 */
	public OAuthPopupConfig setReadTimeout(Integer readTimeout) {
		this.readTimeout = readTimeout;
		return this;
	}

	/**
	 * Retrieves the injector used to add key/value pairs to the callback URL for later use. 
	 * 
	 * @return The callback injector.
	 */
	public OAuthCallbackInjector getCallbackInjector() {
		return callbackInjector;
	}

	/**
	 * Retrieves the injector used to add key/value pairs to the callback URL for later use. 
	 * Two standard injectors ({@link OAuthCallbackInjector#PATH_INJECTOR} and 
	 * {@link OAuthCallbackInjector#QUERY_INJECTOR}) are available. One of these two implementations
	 * should work for most OAuth services.
	 * 
	 * @return The callback injector.
	 * @return The {@code OAuthPopupConfig} instance.
	 */
	public OAuthPopupConfig setCallbackInjector(OAuthCallbackInjector callbackInjector) {
		this.callbackInjector = callbackInjector;
		return this;
	}

	/**
	 * Retrieves the callback URL which will be requested upon successful OAuth authorization.
	 * 
	 * @return The callback URL.
	 */
	public String getCallback() {
		return callback;
	}

	/**
	 * Set the callback URL which will be requested upon successful OAuth authorization. This value
	 * should normally be set by {@link OAuthData}.
	 * 
	 * @param callback The callback URL.
	 * @return The {@code OAuthPopupConfig} instance.
	 */
	protected OAuthPopupConfig setCallback(String callback) {
		this.callback = callback;
		return this;
	}

	/**
	 * Retrieve the "state" parameter to be used for OAuth authorization. 
	 * 
	 * @return The OAuth state parameter.
	 */
	public String getState() {
		return state;
	}

	/**
	 * Set the "state" parameter to be used for OAuth authorization. This is typically used for 
	 * OAuth 2.0 services, and should be set by {@link OAuthData}.
	 * 
	 * @param state The OAuth state parameter.
	 * @return The {@code OAuthPopupConfig} instance.
	 */
	protected OAuthPopupConfig setState(String state) {
		this.state = state;
		return this;
	}

	/**
	 * Retrieves the output stream to be used by the ScribeJava libarary.
	 * 
	 * @return An output stream.
	 */
	public OutputStream getOutputStream() {
		return outputStream;
	}
	
	/**
	 * Set the output stream to be used by the ScribeJava library. 
	 * 
	 * @param outputStream An output stream.
	 * @return The {@code OAuthPopupConfig} instance.
	 */
	public OAuthPopupConfig setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
		return this;
	}

	/**
	 * Retrieves the error parameter name which is used to derive the cause for OAuth authorization
	 * failure. 
	 * 
	 * @return The error parameter name.
	 */
	public String getErrorParameterName() {
		return errorParameterName;
	}

	/**
	 * Set the error parameter name which is used to derive the cause for OAuth authorization
	 * failure. A typical value for this is "error" or "error_description".
	 * 
	 * @param errorParameterName
	 * @return
	 */
	public OAuthPopupConfig setErrorParameterName(String errorParameterName) {
		this.errorParameterName = errorParameterName;
		return this;
	}
}