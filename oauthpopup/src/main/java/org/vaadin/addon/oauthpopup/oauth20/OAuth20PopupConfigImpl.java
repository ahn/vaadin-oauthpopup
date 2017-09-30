package org.vaadin.addon.oauthpopup.oauth20;

import org.vaadin.addon.oauthpopup.base.OAuthCallbackInjector;
import org.vaadin.addon.oauthpopup.base.OAuthPopupConfigAbstract;

import com.github.scribejava.core.model.SignatureType;

/**
 * <p>
 * This object is used to store OAuth1.0 configuration.
 * <p>
 *
 * @author Joao Martins
 *
 */
public class OAuth20PopupConfigImpl extends OAuthPopupConfigAbstract {

  protected OAuth20PopupConfigImpl(String apiKey, String apiSecret) {
    super(apiKey, apiSecret);
  }

  /**
   * Create a pre-configured {@code OAuthPopupConfig} instance with standard OAuth 1.0a settings.
   *
   * @param apiKey The client API key for the OAuth service.
   * @param apiSecret The client API secret for the OAuth service.
   * @return A pre-configured OAuth configuration object.
   */
  public static OAuthPopupConfigAbstract getStandardOAuthConfig(String apiKey, String apiSecret) {
    final OAuthPopupConfigAbstract config =
        new OAuth20PopupConfigImpl(apiKey, apiSecret).setCallbackParameterName("redirect_uri")
            .setVerifierParameterName("code").setResponseType("code")
            .setSignatureType(SignatureType.QueryString).setErrorParameterName("error");
    config.setCallbackInjector(new OAuthCallbackInjector.OAuth2StateInjector(config));
    return config;
  }

}
