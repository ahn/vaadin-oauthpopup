package org.vaadin.addon.oauthpopup.oauth10a;

import org.vaadin.addon.oauthpopup.base.OAuthDataAbstract;
import org.vaadin.addon.oauthpopup.base.OAuthPopupButtonAbstract;
import org.vaadin.addon.oauthpopup.base.OAuthPopupUIAbstract;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.vaadin.server.Page;

/**
 * UI that redirects the user to OAuth authorization url.
 * <p>
 * Should always be opened by {@link OAuthPopupButtonAbstract}.
 * <p>
 * Reads the {@link OAuthData} instance from session. The name of the session variable must be given
 * as URI parameter named "data".
 */
@SuppressWarnings("serial")
public class OAuth10aPopupUIImpl extends OAuthPopupUIAbstract<OAuth10aService, DefaultApi10a> {

  private OAuth10aCallbackRequestHandlerImpl callbackHandler;


  @Override
  protected void redirectToAuthorization(OAuthDataAbstract<OAuth10aService, DefaultApi10a> data) {
    final Token requestToken = ((OAuth10aDataImpl) data).createNewRequestToken();
    addCallbackHandler(requestToken, (OAuth10aDataImpl) data);
    goToAuthorizationUrl(requestToken, (OAuth10aDataImpl) data);
  }

  @Override
  protected void cleanUpSession() {
    // The session may have been already cleaned up by requestHandler,
    // not always though.
    // Doing it again doesn't do harm (?).
    callbackHandler.cleanUpSession(getSession());
  }

  private void addCallbackHandler(Token requestToken, OAuth10aDataImpl data) {
    callbackHandler = new OAuth10aCallbackRequestHandlerImpl(requestToken, data);
    getSession().addRequestHandler(callbackHandler);
  }


  private void goToAuthorizationUrl(Token requestToken, OAuth10aDataImpl data) {
    final String authUrl = data.getAuthorizationUrl(requestToken);
    // Logger.getGlobal().log(Level.INFO, "Navigating to authorization URL: " + authUrl);
    Page.getCurrent().setLocation(authUrl);
  }
}
