package org.vaadin.addon.oauthpopup.base;

import java.util.LinkedList;

import com.github.scribejava.core.builder.api.BaseApi;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.oauth.OAuthService;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.ui.UI;

/**
 * Component extension that opens an OAuth authorization popup window when the extended component is
 * clicked.
 *
 * @author Bryson Dunn
 *
 */
@SuppressWarnings("serial")
public abstract class OAuthPopupOpenerAbstract<S extends OAuthService, T extends BaseApi<S>>
    extends BrowserWindowOpener {

  private final LinkedList<OAuthListener> listeners = new LinkedList<>();

  private final OAuthDataAbstract<S, T> data;
  private OAuthListener dataListener;

  /**
   * Create a new OAuth popop opener for an OAuth 1.0a service.
   *
   * @param api The ScribeJava OAuth 1.0a API singleton instance.
   * @param config OAuth configuration for the particular service.
   */
  public OAuthPopupOpenerAbstract(T api, OAuthDataAbstract<S, T> oAuthData,
      Class<? extends UI> uiClass) {
    super(uiClass);
    this.data = oAuthData;
  }

  public OAuthPopupOpenerAbstract(T api, OAuthDataAbstract<S, T> oAuthData, String url) {
    super(url);
    this.data = oAuthData;
  }

  /**
   * Retrives the OAuth configuration in use by this widget.
   *
   * @return OAuth configuration
   */
  public OAuthPopupConfigAbstract getOAuthPopupConfig() {
    return data.getOAuthPopupConfig();
  }

  /**
   * IMPORTANT: listener events originate from a different window, not from the usual Vaadin
   * server-visit thread. That's why the UI is not updated automatically, UNLESS server push is
   * enabled. So, it's good idea to enable {@code @Push} in the UI class.
   *
   * @param listener The OAuth authorization event listener.
   */
  public void addOAuthListener(OAuthListener listener) {
    listeners.add(listener);
  }

  /**
   * IMPORTANT: listener events originate from a different window, not from the usual Vaadin
   * server-visit thread. That's why the UI is not updated automatically, UNLESS server push is
   * enabled. So, it's good idea to enable {@code @Push} in the UI class.
   *
   * @param listener The OAuth authorization event listener.
   */
  public void removeOAuthListener(OAuthListener listener) {
    listeners.remove(listener);
  }

  @Override
  public void attach() {
    super.attach();

    // Adding the session attribute.
    final String attr = data.getSessionAttributeName();
    getSession().setAttribute(attr, data);
    setParameter(OAuthPopupUIAbstract.DATA_PARAM_NAME, attr);

    dataListener = new OAuthListener() {
      @Override
      public void authSuccessful(Token token) {
        fireAuthSuccessful(token);
      }

      @Override
      public void authDenied(String reason) {
        fireAuthFailed(reason);
      }
    };

    data.addOAuthListener(dataListener);
  }

  @Override
  public void detach() {
    super.detach();

    data.removeOAuthListener(dataListener);

    // Deleting the session attribute.
    getSession().setAttribute(data.getSessionAttributeName(), null);
  }

  protected void fireAuthSuccessful(final Token token) {
    // Coming from different thread than the usual Vaadin server visit.
    // That's why we have to call access.
    // Doing this here so our listeners don't need to.
    getUI().access(() -> {
      for (final OAuthListener li : listeners) {
        li.authSuccessful(token);
      }
      getUI().push();
    });
  }

  protected void fireAuthFailed(final String reason) {
    getUI().access(() -> {
      for (final OAuthListener li : listeners) {
        li.authDenied(reason);
      }
      getUI().push();
    });
  }
}
