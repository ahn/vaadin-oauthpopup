package org.vaadin.addon.oauthpopup.oauth10a;

import org.vaadin.addon.oauthpopup.base.OAuthListener;
import org.vaadin.addon.oauthpopup.base.OAuthPopupButtonAbstract;
import org.vaadin.addon.oauthpopup.base.OAuthPopupConfigAbstract;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.oauth.OAuth10aService;

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
public class OAuth10aPopupButtonImpl
    extends OAuthPopupButtonAbstract<OAuth10aService, DefaultApi10a> {
  private static final long serialVersionUID = 1L;


  /**
   * Create a new OAuth popup button for an OAuth 1.0a service.
   *
   * @param api The ScribeJava OAuth 1.0a API singleton instance.
   * @param apiKey The client API key for the OAuth service.
   * @param apiSecret The client API secret for the OAuth service.
   */
  public OAuth10aPopupButtonImpl(DefaultApi10a api, String apiKey, String apiSecret) {
    this(api, OAuth10aPopupConfigImpl.getStandardOAuthConfig(apiKey, apiSecret));
  }

  /**
   * Create a new OAuth popup button for an OAuth 1.0a service.
   *
   * @param api The ScribeJava OAuth 1.0a API singleton instance.
   * @param oAuthPopupConfigAbstract OAuth configuration for the particular service.
   */
  public OAuth10aPopupButtonImpl(DefaultApi10a api,
      OAuthPopupConfigAbstract oAuthPopupConfigAbstract) {
    super(new OAuth10aPopupOpenerImpl(api, oAuthPopupConfigAbstract));
  }

}
