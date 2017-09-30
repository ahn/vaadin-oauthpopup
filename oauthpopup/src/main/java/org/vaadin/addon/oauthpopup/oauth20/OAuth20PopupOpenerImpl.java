package org.vaadin.addon.oauthpopup.oauth20;

import org.vaadin.addon.oauthpopup.base.OAuthPopupConfigAbstract;
import org.vaadin.addon.oauthpopup.base.OAuthPopupOpenerAbstract;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.oauth.OAuth20Service;

/**
 * Component extension that opens an OAuth authorization popup window when the extended component is
 * clicked.
 *
 * @author Bryson Dunn
 *
 */
@SuppressWarnings("serial")
public class OAuth20PopupOpenerImpl extends OAuthPopupOpenerAbstract<OAuth20Service, DefaultApi20> {

  /**
   * Create a new OAuth popup opener for an OAuth 2.0 service.
   *
   * @param api The ScribeJava OAuth 2.0 API singleton instance.
   * @param apiKey The client API key for the OAuth service.
   * @param apiSecret The client API secret for the OAuth service.
   */
  public OAuth20PopupOpenerImpl(DefaultApi20 api, String apiKey, String apiSecret) {
    this(api, OAuth20PopupConfigImpl.getStandardOAuthConfig(apiKey, apiSecret));
  }

  /**
   * Create a new OAuth popop opener for an OAuth 2.0 service.
   *
   * @param api The ScribeJava OAuth 2.0 API singleton instance.
   * @param config OAuth configuration for the particular service.
   */
  public OAuth20PopupOpenerImpl(DefaultApi20 api, OAuthPopupConfigAbstract config) {
    super(api, new OAuth20DataImpl(api, config), OAuth20PopupUIImpl.class);
  }

  public OAuth20PopupOpenerImpl(DefaultApi20 api, OAuthPopupConfigAbstract config, String url) {
    super(api, new OAuth20DataImpl(api, config), url);
  }

}
