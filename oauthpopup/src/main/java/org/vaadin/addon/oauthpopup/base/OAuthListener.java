package org.vaadin.addon.oauthpopup.base;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Token;

/**
 * Called on OAuth success/failure.
 * <p>
 * In the case that the user doesn't browse back to our application from the authorization url,
 * neither of the methods are called.
 *
 */
public interface OAuthListener {

  /**
   * Called on successful OAuth.
   *
   * @param token The OAuth access token. This will be an {@link OAuth1AccessToken} or
   *        {@link OAuth2AccessToken}, depending on the OAuth version of the API.
   */
  public void authSuccessful(Token token);

  /**
   * Called when the OAuth was denied. The {@code reason} for authorization failure is the value of
   * the URI query parameter value related to the key specified by
   * {@link OAuthPopupConfigAbstract#getErrorParameterName()}. A generic error message is retured if
   * the query parameter was not found.
   *
   * @param reason The reason for OAuth authorization failure.
   */
  public void authDenied(String reason);
}
