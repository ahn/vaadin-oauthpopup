package org.vaadin.addon.oauthpopup.oauth10a;

import org.vaadin.addon.oauthpopup.base.OAuthCallbackRequestHandlerAbstract;
import org.vaadin.addon.oauthpopup.base.OAuthDataAbstract;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.oauth.OAuth10aService;

/**
 * Handles the callback from the OAuth10a authorization url.
 * <p>
 * When done, closes the window and removes this handler.
 *
 */
@SuppressWarnings("serial")
public class OAuth10aCallbackRequestHandlerImpl
    extends OAuthCallbackRequestHandlerAbstract<OAuth10aService, DefaultApi10a> {

  private final Token requestToken;

  /**
   * Only handles request that match the data id.
   *
   * @param requestToken is required for OAuth10a
   * @param data
   */
  public OAuth10aCallbackRequestHandlerImpl(Token requestToken,
      OAuthDataAbstract<OAuth10aService, DefaultApi10a> data) {
    super(data);
    this.requestToken = requestToken;
  }

  @Override
  protected void setDataVerifier(String verifier) {
    data.setVerifier(requestToken, verifier);
  }

}
