package org.vaadin.addon.oauthpopup.oauth10a;

import org.vaadin.addon.oauthpopup.base.OAuthCallbackInjector;
import org.vaadin.addon.oauthpopup.base.OAuthPopupConfig;

import com.github.scribejava.core.model.SignatureType;

/**
 * <p>
 * This object is used to store OAuth1.0a configuration
 * <p>
 *
 * @author Joao Martins
 *
 */
public class OAuth10aPopupConfigImpl extends OAuthPopupConfig {

  protected OAuth10aPopupConfigImpl(String apiKey, String apiSecret) {
    super(apiKey, apiSecret);
  }

  /**
   * Create a pre-configured {@code OAuthPopupConfig} instance with standard OAuth 1.0a settings.
   *
   * @param apiKey The client API key for the OAuth service.
   * @param apiSecret The client API secret for the OAuth service.
   * @return A pre-configured OAuth configuration object.
   */
  public static OAuthPopupConfig getStandardOAuthConfig(String apiKey,
      String apiSecret) {
    return new OAuth10aPopupConfigImpl(apiKey, apiSecret).setCallbackParameterName("oauth_callback")
        .setVerifierParameterName("oauth_verifier").setSignatureType(SignatureType.QueryString)
        .setCallbackInjector(OAuthCallbackInjector.QUERY_INJECTOR);
  }

}
