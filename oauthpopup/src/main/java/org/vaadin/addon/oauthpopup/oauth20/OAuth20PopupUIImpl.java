package org.vaadin.addon.oauthpopup.oauth20;

import org.vaadin.addon.oauthpopup.base.OAuthCallbackRequestHandlerAbstract;
import org.vaadin.addon.oauthpopup.base.OAuthDataAbstract;
import org.vaadin.addon.oauthpopup.base.OAuthPopupButtonAbstract;
import org.vaadin.addon.oauthpopup.base.OAuthPopupUIAbstract;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.oauth.OAuth20Service;

/**
 * UI that redirects the user to OAuth authorization url.
 * <p>
 * Should always be opened by {@link OAuthPopupButtonAbstract}.
 * <p>
 * Reads the {@link OAuthData} instance from session. The name of the session variable must be given
 * as URI parameter named "data".
 */
@SuppressWarnings("serial")
public class OAuth20PopupUIImpl extends OAuthPopupUIAbstract<OAuth20Service, DefaultApi20> {

  @Override
  protected OAuthCallbackRequestHandlerAbstract<OAuth20Service, DefaultApi20> getOAuthCallbackRequestHandler(
      Token requestToken, OAuthDataAbstract<OAuth20Service, DefaultApi20> data) {
    return new OAuth20CallbackRequestHandlerImpl(data);
  }
}
