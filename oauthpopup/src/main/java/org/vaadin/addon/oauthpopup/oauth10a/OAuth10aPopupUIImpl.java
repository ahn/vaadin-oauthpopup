package org.vaadin.addon.oauthpopup.oauth10a;

import org.vaadin.addon.oauthpopup.base.OAuthCallbackRequestHandlerAbstract;
import org.vaadin.addon.oauthpopup.base.OAuthDataAbstract;
import org.vaadin.addon.oauthpopup.base.OAuthPopupButtonAbstract;
import org.vaadin.addon.oauthpopup.base.OAuthPopupUIAbstract;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.oauth.OAuth10aService;

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

  @Override
  protected OAuthCallbackRequestHandlerAbstract<OAuth10aService, DefaultApi10a> getOAuthCallbackRequestHandler(
      Token requestToken, OAuthDataAbstract<OAuth10aService, DefaultApi10a> data) {
    return new OAuth10aCallbackRequestHandlerImpl(requestToken, data);
  }
}
