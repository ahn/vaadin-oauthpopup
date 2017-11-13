package org.vaadin.addon.oauthpopup.base;

import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.SignatureType;
import com.vaadin.server.Page;

/**
 * <p>
 * This object is used to store OAuth configuration that may change between particular API
 * implementations. Pre-configured objects for standard OAuth 1.0a and OAuth 2.0 APIs can be
 * retrieved with {@link #getStandardOAuthConfig(String, String)} and
 * {@link #getStandardOAuth20Config(String, String)}.
 * <p>
 *
 * <p>
 * This addon reserves the {@code callback} and {@code state} settings for internal use.
 * </p>
 *
 * @author Bryson Dunn
 *
 */
public abstract class OAuthPopupConfig {

  // Set by user, or defaulted
  private final String apiKey;
  private final String apiSecret;
  private String scope;
  private String responseType;
  private String callbackParameterName;
  private String callbackUrl;
  private String verifierParameterName;
  private SignatureType signatureType;
  private OutputStream debugStream;
  private Integer connectTimeout;
  private Integer readTimeout;
  private String userAgent;
  private OAuthCallbackInjector callbackInjector;
  private String errorParameterName;

  private final Map<String, String> additionalParams = new HashMap<>();


  // Set by OAuthData
  private String state;

  protected OAuthPopupConfig(String apiKey, String apiSecret) {
    this.apiKey = apiKey;
    this.apiSecret = apiSecret;
    initializeCallbackUrl();
  }

  private void initializeCallbackUrl() {
    String url;
    final URI u = Page.getCurrent().getLocation();
    url = u.getScheme() + "://" + u.getAuthority() + u.getPath();
    setCallbackUrl(url);
  }

  public ServiceBuilder createScribeServiceBuilder() {
    final ServiceBuilder sb = new ServiceBuilder().apiKey(getApiKey()).apiSecret(getApiSecret());
    if (getCallbackUrl() != null && !getCallbackUrl().isEmpty()) {
      sb.callback(getCallbackUrl());
    }
    if (getSignatureType() != null) {
      sb.signatureType(getSignatureType());
    }
    if (getScope() != null && !getScope().isEmpty()) {
      sb.scope(getScope());
    }
    if (getDebugStream() != null) {
      sb.debugStream(getDebugStream());
    }
    if (getConnectTimeout() != null) {
      sb.connectTimeout(getConnectTimeout());
    }
    if (getReadTimeout() != null) {
      sb.readTimeout(getReadTimeout());
    }
    if (getUserAgent() != null && !getUserAgent().isEmpty()) {
      sb.userAgent(getUserAgent());
    }
    if (getState() != null && !getState().isEmpty()) {
      sb.state(getState());
    }
    if (getResponseType() != null && !getResponseType().isEmpty()) {
      sb.responseType(getResponseType());
    }
    return sb;
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
   * Set the "scope" parameter to be used for OAuth authorization. This is typically a
   * space-separated list of string values used with OAuth 2.0 services.
   *
   * @param scope The OAuth scope parameter.
   * @return The {@code OAuthPopupConfig} instance.
   */
  public OAuthPopupConfig setScope(String scope) {
    this.scope = scope;
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
   * Set the "response_type" parameter to be used for OAuth authorization. Should typically be set
   * to "authorization_code" for OAuth 2.0 services.
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
   * Set the query parameter name used to identify the authorization callback. This is typically
   * "redirect_uri" for OAuth 2.0 and "oauth_callback" for OAuth 1.0a.
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
   * Retrieves the callback URL which will be requested upon successful OAuth authorization.
   *
   * @return The callback URL.
   */
  public String getCallbackUrl() {
    return callbackUrl;
  }

  /**
   * Set the callback URL which will be requested upon successful OAuth authorization.
   *
   * @param callbackUrl The callback URL.
   * @return The {@code OAuthPopupConfig} instance.
   */
  public OAuthPopupConfig setCallbackUrl(String callbackUrl) {
    this.callbackUrl = callbackUrl;
    return this;
  }

  /**
   * Set the query parameter name used to identify the verifier token. This is typically "code" for
   * OAuth 2.0 and "oauth_verifier" for OAuth 1.0a.
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
   * Set the number of milliseconds to wait for a response from the OAuth authorization request.
   *
   * @param connectTimeout The connection timeout value.
   * @return The {@code OAuthPopupConfig} instance.
   */
  public OAuthPopupConfig setConnectTimeout(Integer connectTimeout) {
    this.connectTimeout = connectTimeout;
    return this;
  }

  /**
   * Retrieve the number of milliseconds to wait for a callback from the OAuth authorization flow.
   *
   * @return The read timeout value.
   */
  public Integer getReadTimeout() {
    return readTimeout;
  }

  /**
   * Set the number of milliseconds to wait for a callback from the OAuth authorization flow.
   *
   * @param connectTimeout The read timeout value.
   * @return The {@code OAuthPopupConfig} instance.
   */
  public OAuthPopupConfig setReadTimeout(Integer readTimeout) {
    this.readTimeout = readTimeout;
    return this;
  }

  /**
   * Retrieve the "User-Agent" header to use for HTTP calls
   *
   * @return The user agent value.
   */
  public String getUserAgent() {
    return userAgent;
  }

  /**
   * Set the "User-Agent" header to use for HTTP calls
   *
   * @param connectTimeout The user agent value.
   * @return The {@code OAuthPopupConfig} instance.
   */
  public OAuthPopupConfig setUserAgent(String userAgent) {
    this.userAgent = userAgent;
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
   * Retrieves the injector used to add key/value pairs to the callback URL for later use. Two
   * standard injectors ({@link OAuthCallbackInjector#PATH_INJECTOR} and
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
   * Retrieve the "state" parameter to be used for OAuth authorization.
   *
   * @return The OAuth state parameter.
   */
  public String getState() {
    return state;
  }

  /**
   * Set the "state" parameter to be used for OAuth authorization. This is typically used for OAuth
   * 2.0 services, and should be set by {@link OAuthData}.
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
  public OutputStream getDebugStream() {
    return debugStream;
  }

  /**
   * Set the debug output stream to be used by the ScribeJava library.
   *
   * @param outputStream An output stream.
   * @return The {@code OAuthPopupConfig} instance.
   */
  public OAuthPopupConfig setDebugStream(OutputStream debugStream) {
    this.debugStream = debugStream;
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
   * Set the error parameter name which is used to derive the cause for OAuth authorization failure.
   * A typical value for this is "error" or "error_description".
   *
   * @param errorParameterName
   * @return
   */
  public OAuthPopupConfig setErrorParameterName(String errorParameterName) {
    this.errorParameterName = errorParameterName;
    return this;
  }

  public Map<String, String> getAdditionalParams() {
    return additionalParams;
  }

}
