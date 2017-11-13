package org.vaadin.addon.oauthpopup.oauth10a;

import java.io.IOException;

import org.vaadin.addon.oauthpopup.base.OAuthDataAbstract;
import org.vaadin.addon.oauthpopup.base.OAuthPopupConfig;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.oauth.OAuth10aService;

/**
 * Thread-safe class for storing OAuth data.
 * <p>
 * Works as an intermediary between two browser windows: the OAuthPopup window and the Vaadin window
 * containing the OAuthPopupButton.
 */
/**
 * @author Joao Martins
 *
 */
public class OAuth10aDataImpl extends OAuthDataAbstract<OAuth10aService, DefaultApi10a> {

  protected OAuth10aDataImpl(DefaultApi10a api, OAuthPopupConfig config) {
    super(api, config);
  }

  /**
   * Generates the intial request token for OAuth 1.0a authorization requests.
   *
   * @return The OAuth 1.0a request token if {@link #isOAuth10a()}, otherwise null.
   */
  public Token createNewRequestToken() {
    try {
      synchronized (this) {
        return ((OAuth10aService) getService()).getRequestToken();
      }
    } catch (OAuthException | IOException e) {
      throw createException("Getting request token failed.", e);
    }
  }

  @Override
  protected Token getServiceAccessToken(Token requestToken, String verifier) throws IOException {
    return ((OAuth10aService) getService()).getAccessToken((OAuth1RequestToken) requestToken,
        verifier);
  }

  protected synchronized String getAuthorizationUrl(Token requestToken) {
    String url;
    url = ((OAuth10aService) getService()).getAuthorizationUrl((OAuth1RequestToken) requestToken);
    url = config.getCallbackInjector().injectParameterToCallback(url,
        config.getCallbackParameterName(), config.getCallbackUrl());
    return url;
  }


}
