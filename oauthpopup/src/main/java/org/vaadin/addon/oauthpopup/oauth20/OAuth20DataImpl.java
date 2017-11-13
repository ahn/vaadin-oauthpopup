package org.vaadin.addon.oauthpopup.oauth20;

import java.io.IOException;

import org.vaadin.addon.oauthpopup.base.OAuthDataAbstract;
import org.vaadin.addon.oauthpopup.base.OAuthPopupConfig;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.oauth.OAuth20Service;

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
public class OAuth20DataImpl extends OAuthDataAbstract<OAuth20Service, DefaultApi20> {

  protected OAuth20DataImpl(DefaultApi20 api, OAuthPopupConfig config) {
    super(api, config);
  }

  @Override
  protected Token getServiceAccessToken(Token requestToken, String verifier) throws IOException {
    return ((OAuth20Service) getService()).getAccessToken(verifier);
  }

  protected synchronized String getAuthorizationUrl() {
    // OAuth20Service automatically injects "redirect_uri" parameter based on OAuthConfig
    return ((OAuth20Service) getService())
        .getAuthorizationUrl(getOAuthPopupConfig().getAdditionalParams());
  }

}
