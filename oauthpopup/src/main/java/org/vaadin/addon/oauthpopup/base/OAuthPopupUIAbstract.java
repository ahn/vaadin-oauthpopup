package org.vaadin.addon.oauthpopup.base;

import com.github.scribejava.core.builder.api.BaseApi;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.oauth.OAuthService;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

/**
 * UI that redirects the user to OAuth authorization url.
 * <p>
 * Should always be opened by {@link OAuthPopupButtonAbstract}.
 * <p>
 * Reads the {@link OAuthData} instance from session. The name of the session variable must be given
 * as URI parameter named "data".
 */
@SuppressWarnings("serial")
public abstract class OAuthPopupUIAbstract<S extends OAuthService, T extends BaseApi<S>>
    extends UI {

  public static final String DATA_PARAM_NAME = "data";

  private OAuthCallbackRequestHandlerAbstract<S, T> callbackHandler;

  @SuppressWarnings("unchecked")
  @Override
  protected void init(VaadinRequest request) {

    String attr;
    OAuthDataAbstract<S, T> data;
    Token requestToken = null;
    if ((attr = request.getParameter(DATA_PARAM_NAME)) == null) {
      throw new IllegalStateException(
          String.format("No URI parameter named \"%s\".\n", DATA_PARAM_NAME)
              + "Please use OAuthPopupButton or a subclass to open OAuthPopup.");
    } else if ((data = (OAuthDataAbstract<S, T>) getSession().getAttribute(attr)) == null) {
      throw new IllegalStateException(
          String.format("No session attribute named \"%s\" found.\n", attr)
              + "Please use OAuthPopupButton or a subclass to open OAuthPopup.");
    } else {
      requestToken = data.createNewRequestToken();
    }

    addCallbackHandler(requestToken, data);
    goToAuthorizationUrl(requestToken, data);
  }

  @Override
  public void detach() {
    super.detach();
    // The session may have been already cleaned up by requestHandler,
    // not always though.
    // Doing it again doesn't do harm (?).
    callbackHandler.cleanUpSession(getSession());
  }

  private void addCallbackHandler(Token requestToken, OAuthDataAbstract<S, T> data) {
    callbackHandler = getOAuthCallbackRequestHandler(requestToken, data);
    getSession().addRequestHandler(callbackHandler);
  }

  protected abstract OAuthCallbackRequestHandlerAbstract<S, T> getOAuthCallbackRequestHandler(
      Token requestToken, OAuthDataAbstract<S, T> data);

  public void removeCallbackHandler() {
    if (callbackHandler != null) {
      getSession().removeRequestHandler(callbackHandler);
      callbackHandler = null;
    }
  }

  private void goToAuthorizationUrl(Token requestToken, OAuthDataAbstract<S, T> data) {
    final String authUrl = data.getAuthorizationUrl(requestToken);
    // Logger.getGlobal().log(Level.INFO, "Navigating to authorization URL: " + authUrl);
    Page.getCurrent().setLocation(authUrl);
  }
}
