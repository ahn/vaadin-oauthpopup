package org.vaadin.addon.oauthpopup.base;

import com.github.scribejava.core.builder.api.BaseApi;
import com.github.scribejava.core.oauth.OAuthService;
import com.vaadin.ui.Button;

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
public abstract class OAuthPopupButton<S extends OAuthService, T extends BaseApi<S>>
    extends Button {

  private static final long serialVersionUID = -3227617699696740673L;

  private final OAuthPopupOpener<S, T> opener;


  /**
   * Create a new OAuth popup button with an already created {@link OAuthPopupOpener}
   *
   * @param opener
   */
  public OAuthPopupButton(OAuthPopupOpener<S, T> popupOpener) {
    opener = popupOpener;
    opener.extend(this);
  }

  /**
   * Retrives the OAuth configuration in use by this widget.
   *
   * @return OAuth configuration
   */
  public OAuthPopupConfig getOAuthPopupConfig() {
    return opener.getOAuthPopupConfig();
  }

  /**
   * IMPORTANT: listener events originate from a different window, not from the usual Vaadin
   * server-visit thread. That's why the UI is not updated automatically, UNLESS server push is
   * enabled. So, it's good idea to enable {@code @Push} in the UI class.
   *
   * @param listener The OAuth authorization event listener.
   */
  public void addOAuthListener(OAuthListener listener) {
    opener.addOAuthListener(listener);
  }

  /**
   * IMPORTANT: listener events originate from a different window, not from the usual Vaadin
   * server-visit thread. That's why the UI is not updated automatically, UNLESS server push is
   * enabled. So, it's good idea to enable {@code @Push} in the UI class.
   *
   * @param listener The OAuth authorization event listener.
   */
  public void removeListener(OAuthListener listener) {
    opener.removeOAuthListener(listener);
  }

  /**
   * <p>
   * Set the features given to the {@link com.vaadin.server.BrowserWindowOpener}.
   * </p>
   * <p>
   * See here for feature names: https://vaadin.com/book/vaadin7/-/page/advanced.html
   * </p>
   *
   * @param features Comma separated list of features.
   */
  public void setPopupWindowFeatures(String features) {
    opener.setFeatures(features);
  }
}
