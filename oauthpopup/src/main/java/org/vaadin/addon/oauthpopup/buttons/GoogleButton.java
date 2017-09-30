package org.vaadin.addon.oauthpopup.buttons;

import org.vaadin.addon.oauthpopup.base.OAuthPopupButtonAbstract;
import org.vaadin.addon.oauthpopup.oauth20.OAuth20PopupButtonImpl;
import org.vaadin.addon.oauthpopup.oauth20.OAuth20PopupConfigImpl;

import com.github.scribejava.apis.GoogleApi20;
import com.vaadin.server.ClassResource;

/**
 * @author Bryson Dunn
 *
 */
@SuppressWarnings("serial")
public class GoogleButton extends OAuth20PopupButtonImpl {

  /**
   * Vaadin {@link OAuthPopupButtonAbstract} used to initiate OAuth authorization of Google API
   * services.
   * 
   * @param key Google API client ID.
   * @param secret Google API client secret.
   * @param scope Google API scope.
   */
  public GoogleButton(String key, String secret, String scope) {
    super(GoogleApi20.instance(),
        OAuth20PopupConfigImpl.getStandardOAuthConfig(key, secret).setScope(scope));

    setIcon(new ClassResource("/org/vaadin/addon/oauthpopup/icons/google16.png"));
    setCaption("Google");
  }
}
