package org.vaadin.addon.oauthpopup.oauth20;

import org.vaadin.addon.oauthpopup.base.OAuthCallbackRequestHandler;
import org.vaadin.addon.oauthpopup.base.OAuthDataAbstract;
import org.vaadin.addon.oauthpopup.base.OAuthPopupButton;
import org.vaadin.addon.oauthpopup.base.OAuthPopupUI;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.vaadin.server.Page;

/**
 * UI that redirects the user to OAuth authorization url.
 * <p>
 * Should always be opened by {@link OAuthPopupButton}.
 * <p>
 * Reads the {@link OAuthData} instance from session. The name of the session variable must be given
 * as URI parameter named "data".
 */
@SuppressWarnings("serial")
public class OAuth20PopupUIImpl extends OAuthPopupUI<OAuth20Service, DefaultApi20> {

  private OAuth20CallbackRequestHandlerImpl callbackHandler;

  protected OAuthCallbackRequestHandler<OAuth20Service, DefaultApi20> getOAuthCallbackRequestHandler(
      Token requestToken, OAuthDataAbstract<OAuth20Service, DefaultApi20> data) {
    return new OAuth20CallbackRequestHandlerImpl(data);
  }

  @Override
  protected void redirectToAuthorization(OAuthDataAbstract<OAuth20Service, DefaultApi20> data) {
    addCallbackHandler((OAuth20DataImpl) data);
    goToAuthorizationUrl((OAuth20DataImpl) data);
  }

  private void addCallbackHandler(OAuth20DataImpl data) {
    callbackHandler = new OAuth20CallbackRequestHandlerImpl(data);
    getSession().addRequestHandler(callbackHandler);
  }

  private void goToAuthorizationUrl(OAuth20DataImpl data) {
    final String authUrl = data.getAuthorizationUrl();
    // Logger.getGlobal().log(Level.INFO, "Navigating to authorization URL: " + authUrl);
    Page.getCurrent().setLocation(authUrl);
  }

  @Override
  protected void cleanUpSession() {
    // The session may have been already cleaned up by requestHandler,
    // not always though.
    // Doing it again doesn't do harm (?).
    callbackHandler.cleanUpSession(getSession());
  }

}
