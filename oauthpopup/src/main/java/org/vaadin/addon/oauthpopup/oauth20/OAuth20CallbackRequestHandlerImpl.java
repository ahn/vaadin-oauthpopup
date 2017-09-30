package org.vaadin.addon.oauthpopup.oauth20;

import org.vaadin.addon.oauthpopup.base.OAuthCallbackRequestHandlerAbstract;
import org.vaadin.addon.oauthpopup.base.OAuthDataAbstract;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.oauth.OAuth20Service;

/**
 * Handles the callback from the OAuth10a authorization url.
 * <p>
 * When done, closes the window and removes this handler.
 *
 */
@SuppressWarnings("serial")
public class OAuth20CallbackRequestHandlerImpl
    extends OAuthCallbackRequestHandlerAbstract<OAuth20Service, DefaultApi20> {

  /**
   * Only handles request that match the data id.
   *
   * @param data
   */
  public OAuth20CallbackRequestHandlerImpl(OAuthDataAbstract<OAuth20Service, DefaultApi20> data) {
    super(data);
  }

  @Override
  protected void setDataVerifier(String verifier) {
    data.setVerifier(null, verifier);
  }

}
