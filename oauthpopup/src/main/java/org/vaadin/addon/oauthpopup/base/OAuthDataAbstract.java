package org.vaadin.addon.oauthpopup.base;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.github.scribejava.core.builder.api.BaseApi;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.oauth.OAuthService;
import com.vaadin.server.VaadinRequest;

/**
 * Thread-safe class for storing OAuth data.
 * <p>
 * Works as an intermediary between two browser windows: the OAuthPopup window and the Vaadin window
 * containing the OAuthPopupButton.
 */
/**
 * @author Bryson Dunn
 *
 */
public abstract class OAuthDataAbstract<S extends OAuthService, T extends BaseApi<S>> {

  static long latestId = 0;

  synchronized public String nextId() {
    return "" + (++latestId);
  }

  private static final String CALLBACK_ID_PARAMETER_NAME = "callback_id";

  private final List<OAuthListener> listeners = new CopyOnWriteArrayList<>();

  protected final T api;
  protected final OAuthPopupConfig config;

  private final String id;
  private Token accessToken;
  protected OAuthService service;

  protected OAuthDataAbstract(T api, OAuthPopupConfig config) {
    this.id = nextId();
    this.api = api;
    this.config = config;
    injectCallbackId();
  }

  private void injectCallbackId() {
    String callback = config.getCallbackUrl();
    callback = config.getCallbackInjector().injectParameterToCallback(callback,
        CALLBACK_ID_PARAMETER_NAME, getId());
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
    return OAuthDataAbstract.class.getCanonicalName() + ".DATA." + getId();
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
   * <p>
   * Set the verifier token after successful user authorization. This initiates a request to the
   * OAuth service to exchange the verifier token with an access token to complete the OAuth
   * authorization flow.
   * </p>
   * <p>
   * OAuth 2.0 does not require the intial authorization request token to retrieve the access token,
   * so {@code requestToken} should be null if {@link #isOAuth20()}.
   * </p>
   *
   * @param requestToken OAuth request token if version 1.0a, otherwise null
   * @param verifier OAuth verifier token
   */
  public void setVerifier(Token requestToken, String verifier) {
    try {
      Token at;
      synchronized (this) {
        at = getServiceAccessToken(requestToken, verifier);
        setAccessToken(at);
      }
      fireSuccess(at);
    } catch (OAuthException | IOException e) {
      throw createException("Getting access token failed.", e);
    }
  }

  protected abstract Token getServiceAccessToken(Token requestToken, String verifier)
      throws IOException;

  /**
   * <p>
   * Retrieves the OAuth access token. This will return null until the entire OAuth authorization
   * flow has finished successfully.
   * <p>
   * <p>
   * Depending on the OAuth API version this method will return an instance of either
   * {@link OAuth1AccessToken} or {@link OAuth2AccessToken}.
   * </p>
   *
   * @return The OAuth access token, or null if authorization has not been finalized.
   */
  synchronized public Token getAccessToken() {
    return accessToken;
  }

  /**
   * Set the access token for the OAuth authorization flow. {@code accessToken} should be an
   * instance of either {@link OAuth1AccessToken} or {@link OAuth2AccessToken}, depending on the
   * OAuth API version.
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
    return new OAuthException(msg + "\nUsing Scribe API: " + api.getClass().getSimpleName() + "\n",
        e);
  }

  /**
   * Trigger {@link OAuthListener#authDenied(String)} for current listeners upon authorization
   * failure.
   *
   * @param reason Cause of authorization failure
   */
  public void setDenied(String reason) {
    fireFailure(reason);
  }

  private void fireSuccess(Token token) {
    for (final OAuthListener li : listeners) {
      li.authSuccessful(token);
    }
  }

  private void fireFailure(String reason) {
    for (final OAuthListener li : listeners) {
      li.authDenied(reason);
    }
  }

  /**
   * Analyzes the {@code request} to determine if it is a callback from the OAuth API service. The
   * global identifier of this object returned by {@link #getId()} is used to decide this.
   *
   * @param request Vaadin request to check
   * @return true if the request is an OAuth callback for this authorization flow, otherwise false.
   */
  public boolean isCallbackForMe(VaadinRequest request) {
    return getId().equals(config.getCallbackInjector().extractParameterFromCallbackRequest(request,
        CALLBACK_ID_PARAMETER_NAME));
  }

  /**
   * Retrieves the {@link OAuthService} ScribeJava service.
   *
   * @return the ScribeJava OAuth service.
   */
  public OAuthService getService() {
    if (service == null) {
      service = config.createScribeServiceBuilder().build(api);
    }
    return service;
  }

}
