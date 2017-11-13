package org.vaadin.addon.oauthpopup.oauth20;

import org.vaadin.addon.oauthpopup.base.OAuthListener;
import org.vaadin.addon.oauthpopup.base.OAuthPopupButton;
import org.vaadin.addon.oauthpopup.base.OAuthPopupConfig;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.oauth.OAuth20Service;

/**
 * <p>
 * A button that opens a OAuth authorization url in a separate browser window, and lets the user to
 * perform the authorization.
 * </p>
 *
 * <p>
 * The success/failure of the authorization can be listened with
 * {@link #addOAuthListener(OAuthListener)}.
 * </p>
 *
 * <p>
 * IMPORTANT: the callback comes from a different window, not from the usual Vaadin server-visit
 * thread. That's why the UI is not updated automatically, UNLESS server push is enabled. So, it's
 * good idea to enable {@code @Push} in the UI class.
 * </p>
 *
 * <p>
 * This class may be subclassed for customization, or used directly by giving the ScribeJava API
 * class and OAuth configuration for the constructor.
 * </p>
 *
 * <p>
 * Pre-configured subclasses for a number of OAuth services are available at
 * {@link org.vaadin.addon.oauthpopup.buttons}.
 * </p>
 *
 */
public class OAuth20PopupButtonImpl extends OAuthPopupButton<OAuth20Service, DefaultApi20> {
  private static final long serialVersionUID = 1L;

  /**
   * Create a new OAuth popup button for an OAuth 2.0 service.
   *
   * @param api The ScribeJava OAuth 2.0 API singleton instance.
   * @param apiKey The client API key for the OAuth service.
   * @param apiSecret The client API secret for the OAuth service.
   */
  public OAuth20PopupButtonImpl(DefaultApi20 api, String apiKey, String apiSecret) {
    this(api, OAuth20PopupConfigImpl.getStandardOAuthConfig(apiKey, apiSecret));
  }

  /**
   * Create a new OAuth popup button for an OAuth 2.0 service.
   *
   * @param api The ScribeJava OAuth 2.0 API singleton instance.
   * @param config OAuth configuration for the particular service.
   */
  public OAuth20PopupButtonImpl(DefaultApi20 api,
      OAuthPopupConfig oAuthPopupConfigAbstract) {
    super(new OAuth20PopupOpenerImpl(api, oAuthPopupConfigAbstract));
  }

  /**
   * Create a new OAuth popup button for an OAuth 2.0 service. Can be used in combination with the
   * Spring Vaadin plugin using a ui annotated with @SpringUI
   *
   * @param api The ScribeJava OAuth 2.0 API singleton instance.
   * @param config OAuth configuration for the particular service.
   * @param url The url/path on which our UI instance is listening
   */
  public OAuth20PopupButtonImpl(DefaultApi20 api, OAuthPopupConfig config, String url) {
    super(new OAuth20PopupOpenerImpl(api, config, url));
  }

}
