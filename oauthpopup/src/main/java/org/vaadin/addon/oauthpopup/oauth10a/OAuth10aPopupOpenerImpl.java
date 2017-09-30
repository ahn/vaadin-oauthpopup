package org.vaadin.addon.oauthpopup.oauth10a;

import org.vaadin.addon.oauthpopup.base.OAuthPopupConfigAbstract;
import org.vaadin.addon.oauthpopup.base.OAuthPopupOpenerAbstract;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.oauth.OAuth10aService;

/**
 * Component extension that opens an OAuth authorization popup window when the extended component is
 * clicked.
 *
 * @author Bryson Dunn
 *
 */
@SuppressWarnings("serial")
public class OAuth10aPopupOpenerImpl
    extends OAuthPopupOpenerAbstract<OAuth10aService, DefaultApi10a> {

  /**
   * Create a new OAuth popup opener for an OAuth 1.0a service.
   *
   * @param api The ScribeJava OAuth 1.0a API singleton instance.
   * @param apiKey The client API key for the OAuth service.
   * @param apiSecret The client API secret for the OAuth service.
   */
  public OAuth10aPopupOpenerImpl(DefaultApi10a api, String apiKey, String apiSecret) {
    this(api, OAuth10aPopupConfigImpl.getStandardOAuthConfig(apiKey, apiSecret));
  }

  /**
   * Create a new OAuth popop opener for an OAuth 1.0a service.
   *
   * @param api The ScribeJava OAuth 1.0a API singleton instance.
   * @param config OAuth configuration for the particular service.
   */
  public OAuth10aPopupOpenerImpl(DefaultApi10a api, OAuthPopupConfigAbstract config) {
    super(api, new OAuth10aDataImpl(api, config), OAuth10aPopupUIImpl.class);
  }

}
